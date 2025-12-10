package com.eatnote.service;

public interface LlmService {

  void ingestData();

  String answerQuestion(String question);

  void testEmbeddingModel();
  
  // 添加流式问答方法
  void answerQuestionStream(String question, Object responseHandler);
}