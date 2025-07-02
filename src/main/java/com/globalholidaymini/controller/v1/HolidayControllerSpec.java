package com.globalholidaymini.controller.v1;

import com.globalholidaymini.common.BaseResponseData;
import com.globalholidaymini.controller.v1.spec.HolidayControllerSwaggerSpec;
import com.globalholidaymini.dto.CreateRecentFiveYearsHolidaysResponseDto;
import com.globalholidaymini.dto.DeleteHolidayResponseDto;
import com.globalholidaymini.dto.GetHolidaysRequestDto;
import com.globalholidaymini.dto.GetHolidaysResponseDto;
import com.globalholidaymini.dto.RefreshHolidayResponseDto;
import com.globalholidaymini.service.HolidayService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class HolidayControllerSpec implements HolidayControllerSwaggerSpec {

    private final HolidayService holidayService;

    @Override
    @PostMapping("/recent-five-years/holidays")
    public BaseResponseData createRecentFiveYearsHolidays() {
        CreateRecentFiveYearsHolidaysResponseDto responseDto = holidayService.createRecentFiveYearsHolidays();

        return BaseResponseData.success("최근 5년 공휴일 데이터 적재", responseDto);
    }

    @Override
    @GetMapping("/holidays")
    public BaseResponseData getHolidays(@ModelAttribute GetHolidaysRequestDto requestDto) {
        GetHolidaysResponseDto responseDto = holidayService.getHolidays(requestDto);

        return BaseResponseData.success("공휴일 조회", responseDto);
    }

    @Override
    @PatchMapping("/refresh/holidays/{countryCode}/{years}")
    public BaseResponseData refreshHolidays(@PathVariable("countryCode") String countryCode,
        @PathVariable("years") Integer years) {
        RefreshHolidayResponseDto responseDto = holidayService.refreshHoliday(countryCode, years);
        return BaseResponseData.success("공휴일 재동기화", responseDto);
    }

    @Override
    @DeleteMapping("/holidays/{countryCode}/{years}")
    public BaseResponseData deleteHolidays(@PathVariable("countryCode") String countryCode,
        @PathVariable("years") Integer years) {
        DeleteHolidayResponseDto responseDto = holidayService.deleteHoliday(countryCode, years);
        return BaseResponseData.success("공휴일 삭제", responseDto);
    }
}
