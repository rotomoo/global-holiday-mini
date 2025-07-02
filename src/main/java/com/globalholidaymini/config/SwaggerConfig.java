package com.globalholidaymini.config;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        String countryDescription = fetchCountriesFromExternalApi();

        return new OpenAPI()
            .info(new Info()
                .title("OpenAPI definition")
                .version("v0")
                .description("국가 목록 = " + countryDescription));
    }

    private String fetchCountriesFromExternalApi() {
        try {
            String url = "https://date.nager.at/api/v3/AvailableCountries";
            RestTemplate restTemplate = new RestTemplate();
            String json = restTemplate.getForObject(url, String.class);

            ObjectMapper objectMapper = new ObjectMapper();
            List<Map<String, String>> countryList = objectMapper.readValue(json,
                new TypeReference<>() {
                });
            Map<String, String> result = new LinkedHashMap<>();
            for (Map<String, String> country : countryList) {
                result.put(country.get("countryCode"), country.get("name"));
            }

            return objectMapper
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(result);

        } catch (Exception e) {
            return "외부 국가 목록 호출 실패";
        }
    }
}