package com.tian.blog.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {
    private Boolean success;
    private Integer code;
    private String message;
    private T data;

    public static <T> Result<T> success(T data) {
        Result<T> result = Result.success(StatusCode.SUCCESS, data);
        result.success = true;
        result.data = data;
        return result;
    }

    public static <T> Result<T> success(StatusCode statusCode, T data) {
        Result<T> result = new Result<>();
        result.success = true;
        result.code = statusCode.getCode();
        result.message = statusCode.getMessage();
        result.data = data;
        return result;
    }

    public static <T> Result<T> fail(String message) {
        Result<T> result = Result.fail(StatusCode.FAIL);
        result.success = false;
        result.message = message;
        return result;
    }

    public static <T> Result<T> fail(StatusCode statusCode) {
        Result<T> result = new Result<>();
        result.success = false;
        result.code = statusCode.getCode();
        result.message = statusCode.getMessage();
        return result;
    }
}
