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
@Schema(name = "공휴일 삭제 응답")
public class DeleteHolidayResponseDto {

    @Schema(name = "deleteCnt", title = "삭제된 데이터 수")
    private int deleteCnt;
}