package com.globalholidaymini.service;

import com.globalholidaymini.dto.feign.CountryResponseDto;
import com.globalholidaymini.dto.feign.HolidayApiResponseDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class HolidayWebClientService {

    private final WebClient webClient;

    public Mono<List<HolidayApiResponseDto>> getHolidays(int year, String countryCode) {
        return webClient.get()
            .uri("/PublicHolidays/{year}/{countryCode}", year, countryCode)
            .retrieve()
            .bodyToFlux(HolidayApiResponseDto.class)
            .collectList();
    }

    public Mono<List<CountryResponseDto>> getCountries() {
        return webClient.get()
            .uri("/AvailableCountries")
            .retrieve()
            .bodyToFlux(CountryResponseDto.class)
            .collectList();
    }
}
