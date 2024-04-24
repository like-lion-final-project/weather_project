package com.example.demo.weather.dto;

import com.example.demo.weather.entity.MidLandAreaCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MidLandDto {
    private String area;
    private String code;

    public static MidLandDto fromEntity(MidLandAreaCode entity) {
        return MidLandDto.builder()
                .area(entity.getArea())
                .code(entity.getCode())
                .build();
    }
}
