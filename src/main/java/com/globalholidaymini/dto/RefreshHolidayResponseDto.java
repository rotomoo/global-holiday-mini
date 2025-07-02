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
@Schema(name = "공휴일 재동기화 응답")
public class RefreshHolidayResponseDto {

    @Schema(name = "totalCnt", title = "동기화된 데이터 갯수")
    private int totalCnt;
}