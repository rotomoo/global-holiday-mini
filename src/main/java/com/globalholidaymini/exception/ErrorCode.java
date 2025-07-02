package com.globalholidaymini.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /* 광고 등록 */
    ALREADY_EXISTS_ADVERTISING_NAME(1001, BAD_REQUEST, "이미 해당 광고명이 존재합니다."),

    /* 광고 조회 */
    ADVERTISING_NOT_EXIST(1010, BAD_REQUEST, "해당 광고가 존재하지 않습니다."),
    ADVERTISING_NO_AVAILABLE_COUNT(1020, BAD_REQUEST, "해당 광고는 이미 참여 가능 횟수가 모두 소진되었습니다."),

    /* 광고 참여 중복 */
    ALREADY_PARTICIPATION_ADVERTISING(1050, BAD_REQUEST, "이미 해당 광고에 참여하였습니다."),

    /* 광고 참여 락 예외 */
    ADVERTISING_LOCK_TIMEOUT(1060, CONFLICT, "광고 참여 처리 중 락 획득에 실패했습니다."),
    ADVERTISING_LOCK_INTERRUPTED(1061, INTERNAL_SERVER_ERROR, "광고 참여 처리 중 인터럽트가 발생했습니다."),

    /* 유저 조회 */
    USER_NOT_EXIST(2010, BAD_REQUEST, "해당 유저가 존재하지 않습니다.");

    private final int customCode;
    private final HttpStatus httpStatus;
    private final String msg;
}
