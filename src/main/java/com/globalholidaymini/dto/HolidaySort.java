package com.globalholidaymini.dto;

import com.globalholidaymini.common.Sortable;
import lombok.Getter;
import org.springframework.data.domain.Sort.Direction;

@Getter
public enum HolidaySort implements Sortable {
    ID_ASC("id", Direction.ASC),
    ID_DESC("id", Direction.DESC),

    DATE_ASC("date", Direction.ASC),
    DATE_DESC("date", Direction.DESC),

    COUNTRY_CODE_ASC("countryCode", Direction.ASC),
    COUNTRY_CODE_DESC("countryCode", Direction.DESC),

    YEARS_ASC("years", Direction.ASC),
    YEARS_DESC("years", Direction.DESC);

    private String columnName;
    private Direction direction;

    HolidaySort(String columnName, Direction direction) {
        this.columnName = columnName;
        this.direction = direction;
    }
}
