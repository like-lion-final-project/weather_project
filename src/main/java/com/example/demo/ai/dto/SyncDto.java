package com.example.demo.ai.dto;

import lombok.Builder;
import lombok.Getter;

/**
 * <p>동기화 상태를 구분할 Dto 입니다.</p>
 * */
@Getter
@Builder
public class SyncDto {
    boolean created;
    boolean deleted;
    boolean updated;
}
