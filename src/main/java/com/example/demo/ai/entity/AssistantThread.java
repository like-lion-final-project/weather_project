package com.example.demo.ai.entity;

import com.example.demo.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Entity
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class AssistantThread {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    private Assistant assistant;

    @Column(name = "thread_unique_id")
    private String threadId;

    /**
     * <p>어플리케이션 서버에서 독단적으로 이 값을 제어하지 않도록 주의. api를 통해 실제 OpenAi 서버에서 이 값을 지웠는지 체크 후 값을 제어해야함</p>
     * */
    private boolean isDeleteFromOpenAi;


}
