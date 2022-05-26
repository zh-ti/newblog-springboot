package com.tian.blog.controller;

import com.tian.blog.common.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionController {

    @ExceptionHandler(Exception.class)
    public Result<String> exceptionHandler() {
        return Result.fail("服务器异常，请稍后再试吧");
    }
}
