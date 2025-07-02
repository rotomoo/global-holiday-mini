package com.globalholidaymini.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FindAllHolidayCustomDto {

    @Schema(name = "id", title = "id")
    private Long id;

    @Schema(name = "date", title = "일자")
    private LocalDate date;

    @Schema(name = "countryCode", title = "국가 코드 (ISO 3166)")
    private String countryCode;

    @Schema(name = "years", title = "연도")
    private Integer years;

    @Schema(name = "localName", title = "지역 이름")
    private String localName;

    @Schema(name = "name", title = "공식 이름")
    private String name;

    @Schema(name = "fixed", title = "고정 여부")
    private Boolean fixed;

    @Schema(name = "global", title = "글로벌 여부")
    private Boolean global;

    @Schema(name = "launchYear", title = "시행 연도")
    private Integer launchYear;
}

