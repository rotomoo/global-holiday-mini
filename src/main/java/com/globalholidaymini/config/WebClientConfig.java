package com.globalholidaymini.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient() {
        Jackson2JsonDecoder customDecoder = new Jackson2JsonDecoder(
            new ObjectMapper(),
            MediaType.valueOf("text/json;charset=utf-8"),
            MediaType.APPLICATION_JSON
        );

        ExchangeStrategies strategies = ExchangeStrategies.builder()
            .codecs(configurer -> configurer.defaultCodecs().jackson2JsonDecoder(customDecoder))
            .build();

        return WebClient.builder()
            .baseUrl("https://date.nager.at/api/v3")
            .exchangeStrategies(strategies)
            .build();
    }
}