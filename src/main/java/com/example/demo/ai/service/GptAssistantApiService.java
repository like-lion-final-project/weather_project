package com.example.demo.ai.service;

import com.example.demo.ai.dto.assistant.AssistantCreateRequest;
import com.example.demo.ai.dto.run.CreateThreadAndRunRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange("https://api.openai.com")
public interface GptAssistantApiService {

    @PostExchange("/v1/assistants")
    ResponseEntity<String> createAssistant(
            @RequestBody
            AssistantCreateRequest dto);


    @PostExchange("/v1/threads/runs")
    ResponseEntity<String> createThreadAndRun(
            @RequestBody
            CreateThreadAndRunRequest dto
    );

    @GetExchange("/v1/threads/{threadId}/messages/{messageId}")
    ResponseEntity<String> getMessage(
            @PathVariable("threadId") String threadId,
            @PathVariable("messageId") String messageId
    );

    @GetExchange("/v1/threads/{threadId}/messages")
    ResponseEntity<String> getMessages(@PathVariable("threadId") String threadId);


    @GetExchange("/v1/threads/{threadId}/runs/{runId}")
    ResponseEntity<String> getRuns(
            @PathVariable("threadId") String threadId,
            @PathVariable("runId") String runId
    );
}
