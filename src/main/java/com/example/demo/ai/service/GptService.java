package com.example.demo.ai.service;

import com.example.demo.ai.AppConstants;
import com.example.demo.ai.dto.assistant.v2.Tool;
import com.example.demo.ai.dto.assistant.v2.ToolsResources;
import com.example.demo.ai.dto.messages.CreateMessageResDto;
import com.example.demo.ai.dto.messages.GetMessagesResDto;
import com.example.demo.ai.dto.run.v2.Run;
import com.example.demo.ai.entity.Assistant;
import com.example.demo.ai.repo.AssistantRepo;
import com.example.demo.ai.service.dto.DailyCodyReqDto;
import com.example.demo.ai.service.dto.DailyCodyResDto;
import com.example.demo.weather.dto.fcst.FcstItem;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GptService {
    private final GptAssistantApiService gptAssistantApiService;
    private final AssistantRepo assistantRepo;
    private final ObjectMapper objectMapper;

    /**
     * <p>오늘의 추천 코디 카테고리를 생성하는 메서드 입니다.</p>
     * <p>당일 예상 일기 예보 n개를 인자로 받습니다.
     * <b>인자의 크기를 최대 10으로 제한</b> 하고 있습니다. 초과한다면 Exception을 발생시키지 않고 뒤에서부터 10개만 사용합니다. </p>
     *
     * @param fcstItems 당일 혹은 예상 일기예보 n개를 인자로 받습니다.
     */
    public DailyCodyResDto generateDailyCodyCategory(List<FcstItem> fcstItems, List<Tool> tools, ToolsResources toolsResources) {
        if (fcstItems.size() > 10) {
            fcstItems = fcstItems.subList(fcstItems.size() - 10, fcstItems.size());
        }

        List<DailyCodyReqDto> dailyCodyReqDtos = new ArrayList<>();
        for (FcstItem item : fcstItems
        ) {
            dailyCodyReqDtos.add(
                    DailyCodyReqDto.builder()
                            .fcstTime(item.getFcstTime())
                            .fcstValue(item.getFcstValue())
                            .build()
            );
        }

        try {

            String fcstItemsToString = objectMapper.writeValueAsString(dailyCodyReqDtos);
            String prompt = fcstItemsToString + AppConstants.MESSAGE_SUFFIX;
            String role = "user";
            Assistant assistant = assistantRepo.findAssistantByName(AppConstants.NAME + "_" + AppConstants.VERSION)
                    .orElseThrow(() -> new RuntimeException("존재하지 않는 어시스턴트 입니다."));
            Run createAssistantResDto = gptAssistantApiService.oneStepRun(role, prompt, assistant.getAssistantId(),tools,toolsResources);
            System.out.println(createAssistantResDto.getThreadId() + "스레드 아이디");

            Integer count = 0;
            /**
             * 반환되는 상태값은 아래와 같습니다.
             * queued, in_progress,
             * requires_action,
             * cancelling,
             * cancelled,
             * failed,
             * completed, or expired
             **/
            DailyCodyResDto dailyCodyResDto;

            label:
            while (count <= 30) {
                count += 1;
                Run runs = gptAssistantApiService.getRun(createAssistantResDto.getThreadId(), createAssistantResDto.getId());
                System.out.println(runs.getStatus() + "상태 값");
                switch (runs.getStatus()) {
                    case "completed":
                        GetMessagesResDto getMessagesResDto = gptAssistantApiService.getMessagesAPI(runs.getThreadId());
                        CreateMessageResDto getMessageResDto = gptAssistantApiService.getMessageAPI(runs.getThreadId(), getMessagesResDto.getFirstId());
                        if (getMessageResDto.getContent().stream().findFirst().isPresent()) {
                            CreateMessageResDto.Content item = getMessageResDto.getContent().stream().findFirst().get();
                            try {
                                DailyCodyResDto response = objectMapper.readValue(item.getText().getValue(), DailyCodyResDto.class);
                                return DailyCodyResDto.builder()
                                        .categories(response.getCategories())
                                        .build();
                            } catch (JsonProcessingException e) {
                                throw new RuntimeException(e);
                            }
                        }

                        break label;
                    case "cancelling", "cancelled", "failed":
                        break label;
                }

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Thread Interrupted Exception");
                }
            }
        } catch (JsonProcessingException e) {
            System.out.println("에러메시지: "+ e.getMessage());
            throw new RuntimeException("Json Processing Exception");
        } catch (Exception e){
            System.out.println("에러메시지: "+ e.getMessage());
            throw new RuntimeException("Exception");
        }

        return null;
    }

    // TODO: 기온, 지역, 성별 추천 코디
    public void conditionCody(String temp, String location, String gender) {

    }
}
