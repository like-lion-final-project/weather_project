package com.example.demo.ai.dto.run;


import com.example.demo.ai.dto.messages.MessageRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class CreateThreadAndRunRequestThread {
    private List<MessageRequest> messages;
//    private ToolsResources toolsResources;
//    private Map<String,String> metadata;
}
