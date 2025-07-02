package com.globalholidaymini.decode;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.globalholidaymini.dto.feign.CountryResponseDto;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@DisplayName("[단위] 디코드 시뮬레이션 테스트")
public class DecodeSimulationTest {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String BASE_URL = "https://date.nager.at/api/v3";

    @Test
    @DisplayName("success case : 공휴일을_비동기로_조회한다")
    void 공휴일을_비동기로_조회한다() throws Exception {
        // arrange
        String countryUrl = BASE_URL + "/AvailableCountries";
        ResponseEntity<String> countryResponse = restTemplate.getForEntity(countryUrl,
            String.class);
        List<CountryResponseDto> countries = objectMapper.readValue(
            countryResponse.getBody(), new TypeReference<>() {
            }
        );
        System.out.println("전체 국가 수: " + countries.size());

        // act
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        int year = 2025;

        for (CountryResponseDto country : countries) {
            String code = country.getCountryCode();
            String url = BASE_URL + "/PublicHolidays/" + year + "/" + code;

            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                try {
                    ResponseEntity<String> res = restTemplate.getForEntity(url, String.class);
                    String contentType = res.getHeaders().getContentType().toString();
                    String body = res.getBody();

                    List<JsonNode> holidays = objectMapper.readValue(body, new TypeReference<>() {
                    });
                    System.out.println(
                        "성공 " + code + " - " + holidays.size() + "건 / Content-Type: "
                            + contentType);
                    System.out.println("body = " + body);
                } catch (Exception e) {
                    // 실패 시 응답 로그 출력
                    System.err.println("실패 " + code + " 디코딩 실패: " + e.getMessage());
                    try {
                        String raw = restTemplate.getForObject(url, String.class);
                        System.err.println("응답 내용 (" + code + "):\n" + raw);
                    } catch (Exception innerEx) {
                        System.err.println("재요청 실패 (" + code + "): " + innerEx.getMessage());
                    }
                }
            });

            futures.add(future);
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        // assert
        Assertions.assertThat(countries.size()).isGreaterThan(10);
        Assertions.assertThat(futures.size()).isGreaterThan(10);
    }
}
