package com.globalholidaymini.common;

import lombok.Getter;

@Getter
public class BaseResponseData<T> {

    private int code;
    private String msg;
    private T data;
    private boolean success;

    public BaseResponseData(int code, String msg, T data, boolean success) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.success = success;
    }

    public static <T> BaseResponseData<T> success(String msg, T data) {
        return new BaseResponseData<T>(0, msg, data, true);
    }
}
