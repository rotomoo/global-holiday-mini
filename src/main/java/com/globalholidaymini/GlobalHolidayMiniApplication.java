package com.globalholidaymini;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GlobalHolidayMiniApplication {

    public static void main(String[] args) {
        SpringApplication.run(GlobalHolidayMiniApplication.class, args);
    }

}
