package com.globalholidaymini.repository.jdbc;

import com.globalholidaymini.dto.feign.HolidayApiResponseDto;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class HolidayJdbcRepository {

    private final JdbcTemplate jdbcTemplate;
    private final int batchSize = 1000;

    public int upsertAll(List<HolidayApiResponseDto> items) {
        int totalCnt = 0;
        List<HolidayApiResponseDto> subItems = new ArrayList<>();

        for (int i = 0; i < items.size(); i++) {
            subItems.add(items.get(i));

            if ((i + 1) % batchSize == 0) {
                totalCnt += batchUpsert(subItems);
                subItems.clear();
            }
        }

        if (!subItems.isEmpty()) {
            totalCnt += batchUpsert(subItems);
        }

        return totalCnt;
    }

    private int batchUpsert(List<HolidayApiResponseDto> holidays) {
        String sql = """
            INSERT INTO holiday 
                (date, country_code, years, local_name, name, fixed, global, launch_year)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            ON DUPLICATE KEY UPDATE 
                name = VALUES(name),
                fixed = VALUES(fixed),
                global = VALUES(global),
                launch_year = VALUES(launch_year)
            """;

        jdbcTemplate.batchUpdate(sql,

            new BatchPreparedStatementSetter() {

                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    HolidayApiResponseDto holiday = holidays.get(i);
                    LocalDate parsedDate = LocalDate.parse(holiday.getDate(),
                        DateTimeFormatter.ISO_DATE);

                    ps.setObject(1, parsedDate);
                    ps.setString(2, holiday.getCountryCode());
                    ps.setInt(3, parsedDate.getYear());
                    ps.setString(4, holiday.getLocalName());
                    ps.setString(5, holiday.getName());
                    ps.setBoolean(6, Boolean.TRUE.equals(holiday.getFixed()));
                    ps.setBoolean(7, Boolean.TRUE.equals(holiday.getGlobal()));
                    if (holiday.getLaunchYear() != null) {
                        ps.setInt(8, holiday.getLaunchYear());
                    } else {
                        ps.setNull(8, Types.INTEGER);
                    }
                }

                @Override
                public int getBatchSize() {
                    return holidays.size();
                }
            }
        );

        return holidays.size();
    }
}