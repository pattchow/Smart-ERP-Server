package com.eatnote.service.impl;

import com.eatnote.service.LlmService;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.ollama.OllamaEmbeddingModel;
import dev.langchain4j.model.ollama.OllamaStreamingChatModel;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.milvus.MilvusEmbeddingStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class LlmServiceImpl implements LlmService {

    @Override
    public void testEmbeddingModel() {
        try {
            // Initialize embedding model (Ollama)
            EmbeddingModel embeddingModel = OllamaEmbeddingModel.builder()
                    .baseUrl(System.getenv().getOrDefault("OLLAMA_BASE_URL", "http://localhost:11434"))
                    .modelName(System.getenv().getOrDefault("EMBEDDING_MODEL_NAME", "nomic-embed-text:latest"))
                    .build();

            // Test embedding generation
            String testText = "This is a test sentence for embedding generation.";
            Embedding embedding = embeddingModel.embed(testText).content();

            log.info("Embedding model test successful:");
            log.info("- Model: {}", System.getenv().getOrDefault("EMBEDDING_MODEL_NAME", "nomic-embed-text:latest"));
            log.info("- Embedding dimension: {}", embedding.dimension());
            log.info("- Embedding vector length: {}", embedding.vector().length);

        } catch (Exception e) {
            log.error("Embedding model test failed: ", e);
            throw new RuntimeException("Failed to test embedding model", e);
        }
    }

    @Override
    public void ingestData() {
        try {
            // 1. Initialize embedding model (Ollama)
            EmbeddingModel embeddingModel = OllamaEmbeddingModel.builder()
                    .baseUrl("http://localhost:11434")
                    .modelName(System.getenv().getOrDefault("EMBEDDING_MODEL_NAME", "nomic-embed-text:latest"))
                    .build();

            // 2. Load documents from a directory
            String documentDirPath = System.getenv().getOrDefault("DOCUMENT_DIR_PATH", "/Users/zhouhao/develop/RAG");
            List<Document> documents = FileSystemDocumentLoader.loadDocuments(Paths.get(documentDirPath), new TextDocumentParser());

            // 3. Split text (Text Splitter)
            DocumentSplitter splitter = DocumentSplitters.recursive(300, 200); // Chunk size 1000, overlap 200
            List<TextSegment> documentChunks = documents.stream()
                    .flatMap(document -> splitter.split(document).stream())
                    .collect(Collectors.toList());

            // 4. Create vector storage (Milvus)
            EmbeddingStore<TextSegment> embeddingStore = MilvusEmbeddingStore.builder()
                    .host(System.getenv().getOrDefault("MILVUS_HOST", "localhost"))
                    .port(Integer.parseInt(System.getenv().getOrDefault("MILVUS_PORT", "19530")))
                    .collectionName(System.getenv().getOrDefault("COLLECTION_NAME", "private_knowledge_base"))
                    .dimension(embeddingModel.embed("test").content().dimension()) // Get embedding model dimension
                    .build();

            // 5. Store vectors (core step)
            // LangChain4j will automatically call the Ollama API to generate vectors and store them
            List<Embedding> embeddings = embeddingModel.embedAll(documentChunks).content();
            embeddingStore.addAll(embeddings, documentChunks);

            log.info("Data ingestion completed successfully, processed {} documents with {} chunks", documents.size(), documentChunks.size());
        } catch (Exception e) {
            log.error("Error occurred during data ingestion: ", e);
            throw new RuntimeException("Failed to ingest data", e);
        }
    }

    @Override
    public String answerQuestion(String question) {
        try {
            // 1. Initialize embedding model (Ollama)
            EmbeddingModel embeddingModel = OllamaEmbeddingModel.builder()
                    .baseUrl(System.getenv().getOrDefault("OLLAMA_BASE_URL", "http://localhost:11434"))
                    .modelName(System.getenv().getOrDefault("EMBEDDING_MODEL_NAME", "nomic-embed-text"))
                    .build();

            // 2. Create vector storage (Milvus)
            EmbeddingStore<TextSegment> embeddingStore = MilvusEmbeddingStore.builder()
                    .host(System.getenv().getOrDefault("MILVUS_HOST", "localhost"))
                    .port(Integer.parseInt(System.getenv().getOrDefault("MILVUS_PORT", "19530")))
                    .collectionName(System.getenv().getOrDefault("COLLECTION_NAME", "private_knowledge_base"))
                    .dimension(embeddingModel.embed("test").content().dimension()) // Get embedding model dimension
                    .build();

            // 3. Initialize chat model
            ChatModel chatModel = OllamaChatModel.builder()
                    .baseUrl(System.getenv().getOrDefault("OLLAMA_BASE_URL", "http://localhost:11434"))
                    .modelName(System.getenv().getOrDefault("CHAT_MODEL_NAME", "qwen3:8b"))
                    .timeout(Duration.ofMinutes(10))
                    .build();

            // 4. Vectorize the question
            Embedding questionEmbedding = embeddingModel.embed(question).content();

            // 5. Search for similar content in the vector database
            EmbeddingSearchRequest searchRequest = EmbeddingSearchRequest.builder()
                    .queryEmbedding(questionEmbedding)
                    .maxResults(3)
                    .build();
            EmbeddingSearchResult<TextSegment> searchResult = embeddingStore.search(searchRequest);
            List<EmbeddingMatch<TextSegment>> relevantEmbeddings = searchResult.matches();

            String answer;
            // Check if we have relevant embeddings from the knowledge base
            if (!relevantEmbeddings.isEmpty()) {
                // 6. Build context from knowledge base
                StringBuilder contextBuilder = new StringBuilder();
                for (EmbeddingMatch<TextSegment> embeddingMatch : relevantEmbeddings) {
                    contextBuilder.append(embeddingMatch.embedded().text()).append("\n\n");
                }
                String context = contextBuilder.toString();

                // 7. Construct prompt with context
                String prompt = String.format(
                        "请根据以下上下文回答问题。如果上下文中没有相关信息，也请尽力根据你的知识回答问题，同时指出这是基于你自己的知识而非提供的材料。\n\n" +
                                "上下文:\n%s\n\n" +
                                "问题: %s\n\n" +
                                "答案:", context, question);

                // 8. Call the large language model to generate an answer
                answer = chatModel.chat(prompt);
            } else {
                // No relevant context found, ask the model directly
                String prompt = String.format("请回答以下问题: %s", question);
                answer = chatModel.chat(prompt);
            }

            log.info("Question answered successfully");
            return answer;
        } catch (Exception e) {
            log.error("Error occurred while answering question: ", e);
            throw new RuntimeException("Failed to answer question", e);
        }
    }

    @Override
    public void answerQuestionStream(String question, Object responseHandler) {
        StreamingChatResponseHandler handler = (StreamingChatResponseHandler) responseHandler;

        try {
            // 发送开始处理信号
            handler.onPartialResponse("[THINKING] 开始处理问题...\n");

            // 1. 初始化嵌入模型 (Ollama)
            handler.onPartialResponse("[THINKING] 正在初始化嵌入模型...\n");
            EmbeddingModel embeddingModel = OllamaEmbeddingModel.builder()
                    .baseUrl(System.getenv().getOrDefault("OLLAMA_BASE_URL", "http://localhost:11434"))
                    .modelName(System.getenv().getOrDefault("EMBEDDING_MODEL_NAME", "nomic-embed-text"))
                    .build();
            handler.onPartialResponse("[THINKING] 嵌入模型初始化完成\n");

            // 2. 创建向量存储 (Milvus)
            handler.onPartialResponse("[THINKING] 正在连接向量数据库...\n");
            EmbeddingStore<TextSegment> embeddingStore = MilvusEmbeddingStore.builder()
                    .host(System.getenv().getOrDefault("MILVUS_HOST", "localhost"))
                    .port(Integer.parseInt(System.getenv().getOrDefault("MILVUS_PORT", "19530")))
                    .collectionName(System.getenv().getOrDefault("COLLECTION_NAME", "private_knowledge_base"))
                    .dimension(embeddingModel.embed("test").content().dimension()) // Get embedding model dimension
                    .build();
            handler.onPartialResponse("[THINKING] 向量数据库连接成功\n");

            // 3. 初始化流式聊天模型
            handler.onPartialResponse("[THINKING] 正在初始化语言模型...\n");
            StreamingChatModel streamingChatModel = OllamaStreamingChatModel.builder()
                    .baseUrl(System.getenv().getOrDefault("OLLAMA_BASE_URL", "http://localhost:11434"))
                    .modelName(System.getenv().getOrDefault("CHAT_MODEL_NAME", "qwen3:8b"))
                    .timeout(Duration.ofMinutes(10))
                    .build();
            handler.onPartialResponse("[THINKING] 语言模型初始化完成\n");

            // 4. 向量化问题
            handler.onPartialResponse("[THINKING] 正在对问题进行向量化处理...\n");
            Embedding questionEmbedding = embeddingModel.embed(question).content();
            handler.onPartialResponse("[THINKING] 问题向量化完成\n");

            // 5. 在向量数据库中搜索相似内容
            handler.onPartialResponse("[THINKING] 正在知识库中搜索相关信息...\n");
            EmbeddingSearchRequest searchRequest = EmbeddingSearchRequest.builder()
                    .queryEmbedding(questionEmbedding)
                    .maxResults(3)
                    .build();
            EmbeddingSearchResult<TextSegment> searchResult = embeddingStore.search(searchRequest);
            List<EmbeddingMatch<TextSegment>> relevantEmbeddings = searchResult.matches();
            handler.onPartialResponse("[THINKING] 找到 " + relevantEmbeddings.size() + " 条相关资料\n");

            String prompt;
            // Check if we have relevant embeddings from the knowledge base
            if (!relevantEmbeddings.isEmpty()) {
                // 6. 构建上下文
                handler.onPartialResponse("[THINKING] 正在构建回答上下文...\n");
                StringBuilder contextBuilder = new StringBuilder();
                for (EmbeddingMatch<TextSegment> embeddingMatch : relevantEmbeddings) {
                    contextBuilder.append(embeddingMatch.embedded().text()).append("\n\n");
                }
                String context = contextBuilder.toString();

                // 7. 构造提示词
                handler.onPartialResponse("[THINKING] 正在构造提示词模板...\n");
                prompt = String.format(
                        "请根据以下上下文回答问题。如果上下文中没有相关信息，也请尽力根据你的知识回答问题，同时指出这是基于你自己的知识而非提供的材料。\n\n" +
                                "上下文:\n%s\n\n" +
                                "问题: %s\n\n" +
                                "答案:", context, question);
            } else {
                // No relevant context found, ask the model directly
                handler.onPartialResponse("[THINKING] 未找到相关资料，直接向语言模型提问...\n");
                prompt = String.format("请回答以下问题: %s", question);
            }

            // 8. 调用大语言模型生成流式回答
            handler.onPartialResponse("[THINKING] 正在调用大语言模型生成答案...\n");
            streamingChatModel.chat(prompt, handler);

            log.info("Question answered successfully in streaming mode");
        } catch (Exception e) {
            handler.onError(e);
            log.error("Error occurred while answering question in streaming mode: ", e);
            throw new RuntimeException("Failed to answer question in streaming mode", e);
        }
    }
}