package com.example.demo.weather.config;

import com.example.demo.weather.service.FcstHttpInterface;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class FcstClientConfig {

    @Bean
    public RestClient FsctRestClient() {
        return RestClient.builder()
                .baseUrl("http://apis.data.go.kr")
                .build();
    }

    @Bean
    public FcstHttpInterface fcstHttpInterface() {
        return HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(FsctRestClient()))
                .build()
                .createClient(FcstHttpInterface.class);
    }

}
