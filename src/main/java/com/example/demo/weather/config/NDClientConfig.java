package com.example.demo.weather.config;

import com.example.demo.weather.service.NDApiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class NDClientConfig {
    private final String ND_CLIENT_ID = "X-Naver-Client-Id";
    private final String ND_CLIENT_SECRET = "X-Naver-Client-Secret";

    @Value("${naver.developers.client-id}")
    private String ndClientId;
    @Value("${naver.developers.client-secret}")
    private String ndClientSecret;

    @Bean
    public RestClient ndClient() {
        return RestClient.builder()
                .baseUrl("https://openapi.naver.com")
                .defaultHeader(ND_CLIENT_ID, ndClientId)
                .defaultHeader(ND_CLIENT_SECRET, ndClientSecret)
                .build();
    }

    @Bean
    public NDApiService NewsSearchService() {
        return HttpServiceProxyFactory.builderFor(RestClientAdapter.create(ndClient()))
                .build()
                .createClient(NDApiService.class);
    }
}
