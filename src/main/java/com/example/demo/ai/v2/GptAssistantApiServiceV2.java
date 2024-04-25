package com.example.demo.ai.v2;

import com.example.demo.ai.dto.assistant.AssistantCreateRequest;
import com.example.demo.ai.dto.run.CreateThreadAndRunRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange("https://api.openai.com")
public interface GptAssistantApiServiceV2 {

    @PostExchange("/v1/assistants")
    ResponseEntity<String> createAssistant(
            @RequestBody
            AssistantCreateRequest dto);


    @PostExchange("/v1/threads/runs")
    ResponseEntity<String> createThreadAndRun(
            @RequestBody
            CreateThreadAndRunRequest dto
    );

}
