package com.eatnote.controller;

import com.eatnote.common.Result;
import com.eatnote.service.LlmService;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import dev.langchain4j.model.chat.response.PartialResponse;
import dev.langchain4j.model.chat.response.ChatResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1")
public class LlmController {

    @Autowired
    private LlmService llmService;

    @GetMapping("/test")
    public Result<Void> test() {
        llmService.testEmbeddingModel();
        return Result.success(null);
    }

    @GetMapping("/ingestData")
    public Result<Void> ingestData() {
       llmService.ingestData();
       return Result.success();
    }

    @GetMapping("/answerQuestion")
    public Result<String> answerQuestion(@RequestParam  String question) {
        String answer = llmService.answerQuestion(question);
        return Result.success(answer);
    }
    
    @GetMapping("/answerQuestionStream")
    public SseEmitter answerQuestionStream(@RequestParam String question) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        
        // Send an initial message to indicate processing has started
        try {
            emitter.send("Processing your question... Please wait.");
        } catch (Exception e) {
            emitter.completeWithError(e);
            return emitter;
        }
        
        StreamingChatResponseHandler handler = new StreamingChatResponseHandler() {
            @Override
            public void onPartialResponse(String partialResponse) {
                try {
                    if (partialResponse != null && !partialResponse.isEmpty()) {
                        emitter.send(partialResponse);
                    }
                } catch (Exception e) {
                    emitter.completeWithError(e);
                }
            }

            @Override
            public void onCompleteResponse(ChatResponse chatResponse) {
                try {
                    emitter.send("[DONE]");
                } catch (Exception e) {
                    // Ignore
                }
                emitter.complete();
            }

            @Override
            public void onError(Throwable throwable) {
                try {
                    emitter.send("[ERROR]: " + throwable.getMessage());
                } catch (Exception e) {
                    // Ignore
                }
                emitter.completeWithError(throwable);
            }
        };
        
        CompletableFuture.runAsync(() -> {
            try {
                llmService.answerQuestionStream(question, handler);
            } catch (Exception e) {
                try {
                    emitter.completeWithError(e);
                } catch (Exception ex) {
                    // Ignore exception
                }
            }
        });
        
        return emitter;
    }
}