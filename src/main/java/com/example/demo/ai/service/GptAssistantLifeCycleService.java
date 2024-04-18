package com.example.demo.ai.service;

import com.example.demo.ai.dto.assistant.CreateAssistantResDto;
import com.example.demo.ai.entity.Assistant;
import com.example.demo.ai.repo.AssistantRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <p>GPT Assistant api의 결과를 저장하고 라이프 사이클을 관리하는 서비스 입니다.</p>
 * <p>다른 패키지의 서비스에서 마음대로 사용할 수 없도록 패키지로 접근 제한</p>
 * */
@Slf4j
@Service
@RequiredArgsConstructor
class GptAssistantLifeCycleService {
    private final AssistantRepo assistantRepo;

    /**
     * <p>어시스턴트 삭제 메서드 입니다. 같은 패키지 내에서도 사용할 수 없도록 private 접근 제한자 설정</p>
     * */
    private void deleteAssistant(String assistantId){

    }


    public void syncSaveAssistant(CreateAssistantResDto dto){
        Assistant entity = Assistant.builder()
                .assistantId(dto.getId())
                .instructions(dto.getInstructions())
                .name(dto.getName())
                .version(dto.getName().split("_")[1])
                .build();
        try {
            assistantRepo.save(entity);
        }catch (RuntimeException e){
            log.warn(e + "Save Assistant Error");
            log.warn(e + "Try Compensation Transaction");
            deleteAssistant(dto.getId());
        }
    }
    public void saveThread(){
        // TODO: 생성된 스레드 정보를 저장 ( thread_id )
    }
    public void saveMessage(){
        // TODO: 생성된 메시지 정보를 저장 ( 보낸 메시지, 스레드 아이디 등 )
    }
}
