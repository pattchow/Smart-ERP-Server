package com.eatnote.service.impl;

import com.eatnote.service.LlmService;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.ollama.OllamaEmbeddingModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.milvus.MilvusEmbeddingStore;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.ollama.OllamaStreamingChatModel;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import dev.langchain4j.model.chat.response.PartialResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LlmServiceImpl implements LlmService {
    private static final Logger logger = LoggerFactory.getLogger(LlmServiceImpl.class);

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

            logger.info("Embedding model test successful:");
            logger.info("- Model: {}", System.getenv().getOrDefault("EMBEDDING_MODEL_NAME", "nomic-embed-text:latest"));
            logger.info("- Embedding dimension: {}", embedding.dimension());
            logger.info("- Embedding vector length: {}", embedding.vector().length);

        } catch (Exception e) {
            logger.error("Embedding model test failed: ", e);
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

            logger.info("Data ingestion completed successfully, processed {} documents with {} chunks", documents.size(), documentChunks.size());
        } catch (Exception e) {
            logger.error("Error occurred during data ingestion: ", e);
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

            // 6. Build context
            StringBuilder contextBuilder = new StringBuilder();
            for (EmbeddingMatch<TextSegment> embeddingMatch : relevantEmbeddings) {
                contextBuilder.append(embeddingMatch.embedded().text()).append("\n\n");
            }
            String context = contextBuilder.toString();

            // 7. Construct prompt
            String prompt = String.format(
                    "Please answer the question based on the following context. " +
                    "If there is no relevant information in the context, please state that you cannot answer the question based on the provided materials.\n\n" +
                    "Context:\n%s\n\n" +
                    "Question: %s\n\n" +
                    "Answer:", context, question);

            // 8. Call the large language model to generate an answer
            String answer = chatModel.chat(prompt);

            logger.info("Question answered successfully");
            return answer;
        } catch (Exception e) {
            logger.error("Error occurred while answering question: ", e);
            throw new RuntimeException("Failed to answer question", e);
        }
    }
    
    @Override
    public void answerQuestionStream(String question, Object responseHandler) {
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

            // 3. Initialize streaming chat model
            StreamingChatModel streamingChatModel = OllamaStreamingChatModel.builder()
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

            // 6. Build context
            StringBuilder contextBuilder = new StringBuilder();
            for (EmbeddingMatch<TextSegment> embeddingMatch : relevantEmbeddings) {
                contextBuilder.append(embeddingMatch.embedded().text()).append("\n\n");
            }
            String context = contextBuilder.toString();

            // 7. Construct prompt
            String prompt = String.format(
                    "Please answer the question based on the following context. " +
                    "If there is no relevant information in the context, please state that you cannot answer the question based on the provided materials.\n\n" +
                    "Context:\n%s\n\n" +
                    "Question: %s\n\n" +
                    "Answer:", context, question);

            // 8. Call the large language model to generate an answer in streaming mode
            streamingChatModel.chat(prompt, (StreamingChatResponseHandler) responseHandler);

            logger.info("Question answered successfully in streaming mode");
        } catch (Exception e) {
            logger.error("Error occurred while answering question in streaming mode: ", e);
            throw new RuntimeException("Failed to answer question in streaming mode", e);
        }
    }
}