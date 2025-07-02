package com.globalholidaymini.querydsl;

import static com.globalholidaymini.entity.QHoliday.holiday;

import com.globalholidaymini.dto.FindAllHolidayCustomDto;
import com.globalholidaymini.dto.GetHolidaysRequestDto;
import com.globalholidaymini.entity.Holiday;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class HolidayQueryDsl {

    private final JPAQueryFactory jpaQueryFactory;

    public Page<FindAllHolidayCustomDto> findAllHolidayCustom(GetHolidaysRequestDto requestDto) {
        Pageable pageable = requestDto.toPageable(requestDto.getSort());

        List<FindAllHolidayCustomDto> content = jpaQueryFactory.select(
                Projections.fields(FindAllHolidayCustomDto.class,
                    holiday.id,
                    holiday.date,
                    holiday.countryCode,
                    holiday.years,
                    holiday.localName,
                    holiday.name,
                    holiday.fixed,
                    holiday.global,
                    holiday.launchYear
                ))
            .from(holiday)
            .where(
                goeStartDay(requestDto.getStartDay()),
                loeEndDay(requestDto.getEndDay()),
                eqCountryCode(requestDto.getCountryCode()),
                eqYears(requestDto.getYears())
            )
            .orderBy(getOrderSpecifierHoliday(pageable.getSort()).toArray(OrderSpecifier[]::new))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        JPAQuery<Long> countQuery = jpaQueryFactory.select(holiday.count())
            .from(holiday)
            .where(
                goeStartDay(requestDto.getStartDay()),
                loeEndDay(requestDto.getEndDay()),
                eqCountryCode(requestDto.getCountryCode()),
                eqYears(requestDto.getYears())
            );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanExpression goeStartDay(LocalDate startDay) {
        return startDay != null ? holiday.date.goe(startDay) : null;
    }

    private BooleanExpression loeEndDay(LocalDate endDay) {
        return endDay != null ? holiday.date.loe(endDay) : null;
    }

    private BooleanExpression eqCountryCode(String countryCode) {
        return StringUtils.isNotBlank(countryCode) ? holiday.countryCode.eq(countryCode.trim())
            : null;
    }

    private BooleanExpression eqYears(Integer years) {
        return years != null ? holiday.years.eq(years) : null;
    }

    private List<OrderSpecifier> getOrderSpecifierHoliday(Sort sort) {

        List<OrderSpecifier> orders = new ArrayList<>();

        sort.forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;

            Path<Object> fieldPath = Expressions.path(Holiday.class, holiday, order.getProperty());

            orders.add(new OrderSpecifier(direction, fieldPath));
        });

        orders.add(new OrderSpecifier(Order.DESC, holiday.id));

        return orders;
    }
}
