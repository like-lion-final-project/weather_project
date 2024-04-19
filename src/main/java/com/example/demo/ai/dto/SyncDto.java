package com.example.demo.ai.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>동기화 상태를 구분할 Dto 입니다.</p>
 * <b>created</b> 는 동기화 과정에서 새로운 값을 생성 되었을 때를 나타냅니다. <br>
 * <b>deleted</b> 는 동기화 과정 에서 <b>인자로 받은 식별값에 해당하는 항목</b> 이 삭제 되었을 때를 나타냅니다. <br>
 * <b>updated</b> 는 동기화 과정에서 <b>인자로 받은 식별값에 해당하는 항목</b>이 수정 되었을 때를 나타냅니다.
 *
 * <pre>
 * {@code
 *     @Transactional
 *     public CreateAssistantResDto createAndSyncAssistant(String instructions, String name, String model) {
 *         gptAssistantCoreService.synchronizeAssistants(instructions,name,model);
 *         return gptAssistantCoreService.createAssistant(instructions, name, model);
 *     }
 * }
 * </pre>
 *
 * */
@Getter
@Setter
@Builder
public class SyncDto {
    boolean created = false;
    boolean deleted = false;
    boolean updated = false;

    /**
     * 동기화과정 중 생성, 삭제, 업데이트 중 아무런 작업도 수행하지 않았을 경우 ( 동기화가 되어있는 경우를 말함 ) 이를 체크하는 메서드
     *
     * @return true if no action was performed, otherwise false.
     */
    public boolean isSyncNotPerformed() {
        return !created && !deleted && !updated;
    }

}
