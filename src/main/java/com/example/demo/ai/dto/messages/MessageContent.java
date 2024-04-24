package com.example.demo.ai.dto.messages;

import com.example.demo.ai.dto.messages.content.Text;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class MessageContent {
    String type;
    Text text;
}