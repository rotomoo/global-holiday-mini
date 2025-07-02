package com.globalholidaymini.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "최근 5년 공휴일 데이터 적재 응답")
public class CreateRecentFiveYearsHolidaysResponseDto {

    @Schema(name = "totalCnt", title = "총 데이터 적재 갯수")
    private int totalCnt;
}
