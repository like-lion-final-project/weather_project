package com.example.demo.weather.dto;

import com.example.demo.weather.entity.MidTaAreaCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MidTaDto {
    private String area;
    private String code;

    public static MidTaDto fromEntity(MidTaAreaCode entity) {
        return MidTaDto.builder()
                .area(entity.getArea())
                .code(entity.getCode())
                .build();
    }
}
