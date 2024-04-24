package com.example.demo.ai.dto.run.v2;


import com.example.demo.ai.dto.assistant.v2.ToolsResources;
import com.example.demo.ai.dto.messages.v2.messages.MessageRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class CreateThreadAndRunRequestThread {
    private List<MessageRequest> messages;
//    private ToolsResources toolsResources;
//    private Map<String,String> metadata;
}
