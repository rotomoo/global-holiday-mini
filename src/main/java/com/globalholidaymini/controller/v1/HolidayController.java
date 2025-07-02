package com.globalholidaymini.controller.v1;

import com.globalholidaymini.common.BaseResponseData;
import com.globalholidaymini.controller.v1.swagger.HolidayControllerSwagger;
import com.globalholidaymini.dto.CreateRecentFiveYearsHolidaysResponseDto;
import com.globalholidaymini.dto.GetHolidaysRequestDto;
import com.globalholidaymini.dto.GetHolidaysResponseDto;
import com.globalholidaymini.service.HolidayService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class HolidayController implements HolidayControllerSwagger {

    private final HolidayService holidayService;

    @PostMapping("/recent-five-years/holidays")
    public BaseResponseData createRecentFiveYearsHolidays() {
        CreateRecentFiveYearsHolidaysResponseDto responseDto = holidayService.createRecentFiveYearsHolidays();

        return BaseResponseData.success("최근 5년 공휴일 데이터 적재", responseDto);
    }

    @GetMapping("/holidays")
    public BaseResponseData getHolidays(@ModelAttribute GetHolidaysRequestDto requestDto) {
        GetHolidaysResponseDto responseDto = holidayService.getHolidays(requestDto);

        return BaseResponseData.success("공휴일 조회", responseDto);
    }
}
