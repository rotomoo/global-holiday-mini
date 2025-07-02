package com.globalholidaymini.service;

import com.globalholidaymini.dto.feign.CountryResponseDto;
import com.globalholidaymini.dto.feign.HolidayApiResponseDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class HolidayWebClientService {

    private final WebClient webClient;

    @CircuitBreaker(name = "holidayApi", fallbackMethod = "getHolidaysFallback")
    @Retry(name = "holidayApi")
    public Mono<List<HolidayApiResponseDto>> getHolidays(int year, String countryCode) {
        return webClient.get()
            .uri("/PublicHolidays/{year}/{countryCode}", year, countryCode)
            .retrieve()
            .bodyToFlux(HolidayApiResponseDto.class)
            .collectList();
    }

    @CircuitBreaker(name = "holidayApi", fallbackMethod = "getCountriesFallback")
    @Retry(name = "holidayApi")
    public Mono<List<CountryResponseDto>> getCountries() {
        return webClient.get()
            .uri("/AvailableCountries")
            .retrieve()
            .bodyToFlux(CountryResponseDto.class)
            .collectList();
    }

    private Mono<List<HolidayApiResponseDto>> getHolidaysFallback(int year, String countryCode,
        Throwable t) {
        return Mono.error(new RuntimeException("공휴일 API 호출 실패", t));
    }

    private Mono<List<CountryResponseDto>> getCountriesFallback(Throwable t) {
        return Mono.error(new RuntimeException("국가 목록 API 호출 실패", t));
    }
}
