package com.globalholidaymini.controller.v1.spec;

import com.globalholidaymini.common.BaseResponseData;
import com.globalholidaymini.dto.CreateRecentFiveYearsHolidaysResponseDto;
import com.globalholidaymini.dto.DeleteHolidayResponseDto;
import com.globalholidaymini.dto.GetHolidaysRequestDto;
import com.globalholidaymini.dto.GetHolidaysResponseDto;
import com.globalholidaymini.dto.RefreshHolidayResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;

@Tag(name = "공휴일 API", description = "공휴일 API")
public interface HolidayControllerSwaggerSpec {

    @Operation(
        summary = "최근 5년 공휴일 데이터 적재",
        description = "최근 5년 공휴일 데이터 적재한다.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "성공",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CreateRecentFiveYearsHolidaysResponseDto.class)
                )
            )
        }
    )
    BaseResponseData createRecentFiveYearsHolidays();

    @Operation(
        summary = "공휴일 조회",
        description = "공휴일 페이징 조회",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "성공",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = GetHolidaysResponseDto.class)
                )
            ),
            @ApiResponse(
                responseCode = "500",
                description = "pagetNumber, pageSize 0 확인",
                content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                        value = """
                            {
                              "timestamp": "2025-07-02T04:58:08.810+00:00",
                              "status": 500,
                              "error": "Internal Server Error",
                              "path": "/api/v1/holidays"
                            }
                            """
                    )
                )
            ),
            @ApiResponse(
                responseCode = "400",
                description = "이력 시작, 종료 기간 확인",
                content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                        value = """
                            {
                              "type": "about:blank",
                              "title": "Bad Request",
                              "status": 400,
                              "detail": "Invalid request content.",
                              "instance": "/api/v1/holidays"
                            }
                            """
                    )
                )
            )
        }
    )
    BaseResponseData getHolidays(@ParameterObject GetHolidaysRequestDto requestDto);

    @Operation(
        summary = "공휴일 재동기화",
        description = "특정 연도와 국가의 공휴일 데이터를 재요청 후 덮어쓴다",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "성공",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RefreshHolidayResponseDto.class)
                )
            )
        }
    )
    BaseResponseData refreshHolidays(
        @Parameter(description = "국가 코드", example = "KR") String countryCode,
        @Parameter(description = "연도", example = "2025") Integer years
    );

    @Operation(
        summary = "공휴일 삭제",
        description = "특정 연도와 국가의 공휴일 데이터를 삭제한다",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "성공",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = DeleteHolidayResponseDto.class)
                )
            )
        }
    )
    BaseResponseData deleteHolidays(
        @Parameter(description = "국가 코드", example = "KR") String countryCode,
        @Parameter(description = "연도", example = "2025") Integer years
    );
}
