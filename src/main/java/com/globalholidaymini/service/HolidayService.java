package com.globalholidaymini.service;


import com.globalholidaymini.common.PagingInfo;
import com.globalholidaymini.dto.CreateRecentFiveYearsHolidaysResponseDto;
import com.globalholidaymini.dto.DeleteHolidayResponseDto;
import com.globalholidaymini.dto.GetHolidaysRequestDto;
import com.globalholidaymini.dto.GetHolidaysResponseDto;
import com.globalholidaymini.dto.RefreshHolidayResponseDto;
import com.globalholidaymini.dto.feign.CountryResponseDto;
import com.globalholidaymini.dto.feign.HolidayApiResponseDto;
import com.globalholidaymini.dto.querydsl.FindAllHolidayCustomDto;
import com.globalholidaymini.querydsl.HolidayQueryDsl;
import com.globalholidaymini.repository.HolidayRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class HolidayService {

    private final HolidayWebClientService holidayWebClientService;
    private final HolidayUpsertService holidayUpsertService;
    private final HolidayQueryDsl holidayQueryDsl;
    private final HolidayRepository holidayRepository;

    /**
     * 최근 5년 공휴일 데이터 적재
     *
     * @return
     */
    public CreateRecentFiveYearsHolidaysResponseDto createRecentFiveYearsHolidays() {
        int currentYear = LocalDate.now().getYear();
        List<CountryResponseDto> countries = holidayWebClientService.getCountries().block();

        List<Mono<List<HolidayApiResponseDto>>> requests = new ArrayList<>();
        for (CountryResponseDto country : countries) {
            for (int year = currentYear - 4; year <= currentYear; year++) {
                requests.add(holidayWebClientService.getHolidays(year, country.getCountryCode()));
            }
        }

        List<HolidayApiResponseDto> result = Flux.fromIterable(requests)
            .flatMap(mono -> mono)
            .flatMap(Flux::fromIterable)
            .collect(Collectors.toSet()) // 중복 제거
            .blockOptional()
            .map(ArrayList::new)
            .orElseGet(ArrayList::new);

        int totalCnt = holidayUpsertService.upsertAll(result);

        return new CreateRecentFiveYearsHolidaysResponseDto(totalCnt);
    }

    /**
     * 공휴일 조회
     *
     * @param requestDto
     * @return
     */
    public GetHolidaysResponseDto getHolidays(GetHolidaysRequestDto requestDto) {
        Page<FindAllHolidayCustomDto> historiesPage = holidayQueryDsl.findAllHolidayCustom(
            requestDto);

        return new GetHolidaysResponseDto(PagingInfo.createPagingInfo(historiesPage),
            historiesPage.getContent());
    }

    /**
     * 공휴일 재동기화
     *
     * @param countryCode
     * @param years
     * @return
     */
    public RefreshHolidayResponseDto refreshHoliday(String countryCode, Integer years) {
        List<HolidayApiResponseDto> holidayApiResponseDtos = holidayWebClientService.getHolidays(
            years, countryCode).block();

        int totalCnt = holidayUpsertService.upsertAll(holidayApiResponseDtos);

        return new RefreshHolidayResponseDto(totalCnt);
    }

    /**
     * 공휴일 삭제
     *
     * @param countryCode
     * @param years
     * @return
     */
    @Transactional
    public DeleteHolidayResponseDto deleteHoliday(String countryCode, Integer years) {
        int deleteCnt = holidayRepository.deleteByCountryCodeAndYears(countryCode, years);

        return new DeleteHolidayResponseDto(deleteCnt);
    }

    /**
     * 매년 1월 2일 01:00 KST 전년도, 금년도 공휴일 자동 동기화
     */
    public void syncCurrentAndPreviousYear() {
        int currentYear = LocalDate.now().getYear();
        int previousYear = currentYear - 1;
        List<CountryResponseDto> countries = holidayWebClientService.getCountries().block();

        List<Mono<List<HolidayApiResponseDto>>> requests = new ArrayList<>();
        for (CountryResponseDto country : countries) {
            requests.add(
                holidayWebClientService.getHolidays(previousYear, country.getCountryCode()));
            requests.add(
                holidayWebClientService.getHolidays(currentYear, country.getCountryCode()));
        }

        List<HolidayApiResponseDto> result = Flux.fromIterable(requests)
            .flatMap(mono -> mono)
            .flatMap(Flux::fromIterable)
            .collect(Collectors.toSet()) // 중복 제거
            .blockOptional()
            .map(ArrayList::new)
            .orElseGet(ArrayList::new);

        holidayUpsertService.upsertAll(result);
    }
}
