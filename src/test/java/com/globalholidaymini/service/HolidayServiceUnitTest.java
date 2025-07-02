package com.globalholidaymini.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;

import com.globalholidaymini.dto.CreateRecentFiveYearsHolidaysResponseDto;
import com.globalholidaymini.dto.feign.CountryResponseDto;
import com.globalholidaymini.dto.feign.HolidayApiResponseDto;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

@DisplayName("[단위] HolidayService 테스트")
@ExtendWith(MockitoExtension.class)
class HolidayServiceUnitTest {

    @Mock
    private HolidayWebClientService holidayWebClientService;

    @Mock
    private HolidayUpsertService holidayUpsertService;

    @InjectMocks
    private HolidayService holidayService;

    @Test
    @DisplayName("success case : 공휴일 데이터 정상 적재")
    void 공휴일_데이터_정상_적재() {
        // arrange
        CountryResponseDto countryResponseDto = new CountryResponseDto("KR", "대한민국");
        List<CountryResponseDto> countries = List.of(countryResponseDto);

        Mockito.when(holidayWebClientService.getCountries())
            .thenReturn(Mono.just(countries));

        HolidayApiResponseDto holidayApiResponseDto = new HolidayApiResponseDto("2025-01-01",
            "New Year", "New Year", "KR", true, true, 1909);

        Mockito.when(holidayWebClientService.getHolidays(anyInt(), anyString()))
            .thenReturn(Mono.just(List.of(holidayApiResponseDto)));

        Mockito.when(holidayUpsertService.upsertAll(any()))
            .thenReturn(1);

        // act
        CreateRecentFiveYearsHolidaysResponseDto result = holidayService.createRecentFiveYearsHolidays();

        // assert
        Assertions.assertThat(result.getTotalCnt()).isEqualTo(1);
    }
}