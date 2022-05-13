package com.tian.blog;

public enum StatusCode {

    SUCCESS(200, "请求成功"),
    FAIL(400, "请求失败"),
    NoAuthorization(403, "认证失败");

    private final Integer code;
    private final String message;

    StatusCode(Integer code, String message){
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
