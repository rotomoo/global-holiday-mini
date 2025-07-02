package com.globalholidaymini.dto;

import com.globalholidaymini.common.Sortable;
import com.globalholidaymini.dto.sort.HolidaySort;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "공휴일 조회 요청")
public class GetHolidaysRequestDto {

    /* 요청 필터 */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(name = "startDay", description = "시작 기간", example = "2025-06-01")
    private LocalDate startDay;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(name = "endDay", description = "종료 기간", example = "2025-07-01")
    private LocalDate endDay;

    @Schema(name = "countryCode", description = "국가 코드", example = "KR")
    private String countryCode;

    @Schema(name = "years", description = "연도", example = "2025")
    private Integer years;

    /* 페이징 필터 */
    @Schema(name = "pageNumber", description = "페이지 번호. 기본값 1.", example = "1")
    private Integer pageNumber = 1;

    @Schema(name = "pageSize", description = "페이지 당 항목 수. 기본값 10.", example = "10")
    private Integer pageSize = 10;

    @Schema(
        name = "sort",
        description = "정렬 기준. 기본값 ID_DESC<br>ID_ASC, ID_DESC<br>DATE_ASC, DATE_DESC<br>COUNTRY_CODE_ASC, COUNTRY_CODE_DESC<br>YEARS_ASC, YEARS_DESC",
        example = "ID_DESC"
    )
    private HolidaySort sort = HolidaySort.ID_DESC;

    public Pageable toPageable() {
        return PageRequest.of(
            pageNumber - 1,
            pageSize);
    }

    public Pageable toPageable(Sortable sort) {
        return PageRequest.of(
            pageNumber - 1,
            pageSize,
            Sort.by(sort.getDirection(), sort.getColumnName()));
    }
}
