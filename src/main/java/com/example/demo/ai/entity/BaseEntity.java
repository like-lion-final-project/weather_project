package com.example.demo.ai.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@SuperBuilder // builder 에서 상속받은 값은 제어하지 못하는데 @SuperBuilder를 사용하면 제어가 가능해짐
@MappedSuperclass  // Entity 에서 일반 클래스를 상속 받기 위함
@EntityListeners(AuditingEntityListener.class)  // JPA의 업데이트 등 이벤트를 감지
@NoArgsConstructor
public class BaseEntity {

    @Column(name = "is_delete")
    private boolean isDelete;

    // @CreatedDate  엔티티가 생성되고 DB에 저장될 때 자동으로 생성일이 입력됩니다.
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // @LastModifiedDate 엔티티가 업데이트되고 DB에 저장될 때 자동으로 업데이트 날짜가 입력됩니다.
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
