package com.globalholidaymini.scheduler;

import com.globalholidaymini.service.HolidayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class HolidayBatchScheduler {

    private final HolidayService holidayService;

    /**
     * 매년 1월 2일 01:00 KST 전년도, 금년도 공휴일 자동 동기화
     */
    @Scheduled(cron = "0 0 1 2 1 *", zone = "Asia/Seoul")
    public void syncOnlyCurrentAndPreviousYear() {
        log.info("[Scheduler] 전체 국가 공휴일 자동 동기화 시작");
        holidayService.syncCurrentAndPreviousYear();
        log.info("[Scheduler] 전체 국가 공휴일 자동 동기화 완료");
    }
}
