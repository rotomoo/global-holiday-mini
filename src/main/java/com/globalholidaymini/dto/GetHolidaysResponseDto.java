package com.globalholidaymini.dto;

import com.globalholidaymini.common.PagingInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "공휴일 조회 응답")
public class GetHolidaysResponseDto {

    @Schema(name = "paging", title = "페이징 정보")
    private PagingInfo paging;

    @Schema(name = "list", title = "공휴일 목록")
    private List<FindAllHolidayCustomDto> list;
}
