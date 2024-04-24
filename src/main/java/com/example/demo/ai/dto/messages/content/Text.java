package com.example.demo.ai.dto.messages.content;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Text {
    String value;
    List<Annotation> annotations;
}