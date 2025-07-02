package com.globalholidaymini.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;

import com.globalholidaymini.dto.CreateRecentFiveYearsHolidaysResponseDto;
import com.globalholidaymini.dto.DeleteHolidayResponseDto;
import com.globalholidaymini.dto.RefreshHolidayResponseDto;
import com.globalholidaymini.dto.feign.CountryResponseDto;
import com.globalholidaymini.dto.feign.HolidayApiResponseDto;
import com.globalholidaymini.repository.HolidayRepository;
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

    @Mock
    private HolidayRepository holidayRepository;

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

    @Test
    @DisplayName("success case : 공휴일 정상 재동기화")
    void 공휴일_정상_재동기화() {
        // arrange
        String countryCode = "KR";
        Integer years = 2025;

        HolidayApiResponseDto dto = new HolidayApiResponseDto(
            "2025-10-03", "개천절", "National Foundation Day", "KR", true, true, 1909
        );

        Mockito.when(holidayWebClientService.getHolidays(years, countryCode))
            .thenReturn(Mono.just(List.of(dto)));

        Mockito.when(holidayUpsertService.upsertAll(any())).thenReturn(1);

        // act
        RefreshHolidayResponseDto result = holidayService.refreshHoliday(countryCode, years);

        // assert
        Assertions.assertThat(result.getTotalCnt()).isEqualTo(1);
    }

    @Test
    @DisplayName("success case : 공휴일 정상 삭제")
    void 공휴일_정상_삭제() {
        // arrange
        String countryCode = "KR";
        Integer years = 2025;

        Mockito.when(holidayRepository.deleteByCountryCodeAndYears(countryCode, years))
            .thenReturn(5);

        // act
        DeleteHolidayResponseDto result = holidayService.deleteHoliday(countryCode, years);

        // assert
        Assertions.assertThat(result.getDeleteCnt()).isEqualTo(5);
    }

    @Test
    @DisplayName("success case : 배치 자동화 정상 동작")
    void 배치_자동화_정상_동작() {
        // arrange
        CountryResponseDto countryResponseDto = new CountryResponseDto("KR", "대한민국");
        List<CountryResponseDto> countries = List.of(countryResponseDto);

        Mockito.when(holidayWebClientService.getCountries())
            .thenReturn(Mono.just(countries));

        HolidayApiResponseDto holidayApiResponseDto1 = new HolidayApiResponseDto("2024-12-31",
            "New Year Eve", "New Year Eve", "KR", true, true, 1909);
        HolidayApiResponseDto holidayApiResponseDto2 = new HolidayApiResponseDto("2025-01-01",
            "New Year", "New Year", "KR", true, true, 1909);

        Mockito.when(holidayWebClientService.getHolidays(anyInt(), anyString()))
            .thenReturn(Mono.just(List.of(holidayApiResponseDto1, holidayApiResponseDto2)));

        Mockito.when(holidayUpsertService.upsertAll(any()))
            .thenReturn(2);

        // act
        holidayService.syncCurrentAndPreviousYear();

        // assert
        Mockito.verify(holidayWebClientService, Mockito.times(1)).getCountries();
        Mockito.verify(holidayWebClientService, Mockito.times(2))
            .getHolidays(anyInt(), anyString());
        Mockito.verify(holidayUpsertService, Mockito.times(1)).upsertAll(any());
    }
}