package com.globalholidaymini.repository;

import com.globalholidaymini.entity.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HolidayRepository extends JpaRepository<Holiday, Long> {

    int deleteByCountryCodeAndYears(String countryCode, Integer years);
}
