package com.globalholidaymini.service;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@DisplayName("[통합] HolidayService 테스트")
@SpringBootTest
@AutoConfigureMockMvc
class HolidayServiceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("success case : 공휴일 조회 정상 호출")
    void 공휴일_조회_정상_호출() throws Exception {
        // given
        String countryCode = "KR";
        String years = "2025";
        String startDay = "2025-01-01";
        String endDay = "2025-12-31";
        String pageNumber = "1";
        String pageSize = "10";
        String sort = "ID_DESC";

        // when & then
        mockMvc.perform(get("/api/v1/holidays")
                .param("countryCode", countryCode)
                .param("years", years)
                .param("startDay", startDay)
                .param("endDay", endDay)
                .param("pageNumber", pageNumber)
                .param("pageSize", pageSize)
                .param("sort", sort))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(0))
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.paging.pageNumber").value(Integer.parseInt(pageNumber)))
            .andExpect(jsonPath("$.data.list").isArray());
    }
}
