package com.globalholidaymini.service;

import com.globalholidaymini.dto.feign.HolidayApiResponseDto;
import com.globalholidaymini.repository.jdbc.HolidayJdbcRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HolidayUpsertService {

    private final HolidayJdbcRepository holidayJdbcRepository;

    @Transactional
    public int upsertAll(List<HolidayApiResponseDto> holidays) {
        return holidayJdbcRepository.upsertAll(holidays);
    }
}
