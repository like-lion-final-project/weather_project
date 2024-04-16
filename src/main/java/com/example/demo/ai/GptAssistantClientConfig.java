package com.example.demo.ai;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class GptAssistantClientConfig {
    /**
     * OpenAI assistant api 헤더에 추가할 베타 표시.
     */
    public static final String OPEN_AI_BETA = "OpenAI-Beta";

    /**
     * OpenAI assistant api 버전.
     */
    public static final String ASSISTANTS_V1 = "assistants=v1";
    private static final String BASE_URL = "https://api.openai.com";


    @Value("${open-ai.api-key}")
    private String OPEN_AI_API_KEY = "";

    @Bean
    public RestClient GptAssistantClient() {
        System.out.println(OPEN_AI_API_KEY + " :api 키");
        return RestClient.builder()
                .baseUrl(BASE_URL)
                .defaultHeader( OPEN_AI_BETA,ASSISTANTS_V1)
                .defaultHeader( "Authorization","Bearer " + OPEN_AI_API_KEY)
                .build();
    }
}
