package com.globalholidaymini.service;


import com.globalholidaymini.common.PagingInfo;
import com.globalholidaymini.dto.CreateRecentFiveYearsHolidaysResponseDto;
import com.globalholidaymini.dto.FindAllHolidayCustomDto;
import com.globalholidaymini.dto.GetHolidaysRequestDto;
import com.globalholidaymini.dto.GetHolidaysResponseDto;
import com.globalholidaymini.dto.feign.CountryResponseDto;
import com.globalholidaymini.dto.feign.HolidayApiResponseDto;
import com.globalholidaymini.querydsl.HolidayQueryDsl;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class HolidayService {

    private final HolidayWebClientService holidayWebClientService;
    private final HolidayUpsertService holidayUpsertService;
    private final HolidayQueryDsl holidayQueryDsl;

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
            .collect(Collectors.toSet()) // Set 중복 제거
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
}