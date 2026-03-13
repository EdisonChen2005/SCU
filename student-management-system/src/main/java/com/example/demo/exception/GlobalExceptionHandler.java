package com.example.demo.exception; // 包名必须和你创建的一致

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

// 核心注解：全局捕获Controller层的异常，且返回JSON格式（不是页面）
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. 捕获参数校验异常（比如姓名为空、年龄负数）
    @ExceptionHandler(BindException.class) // 指定捕获BindException类型的异常
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 返回400状态码（请求参数错误）
    @ResponseBody // 强制返回JSON，不走页面跳转
    public Map<String, Object> handleBindException(BindException e) {
        // 封装返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("code", 400); // 自定义错误码
        result.put("message", "参数校验失败"); // 错误提示

        // 收集每个字段的具体错误信息（比如name:姓名不能为空）
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : e.getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        result.put("errors", errors);

        return result; // 返回JSON格式的错误信息
    }

    // 2. 捕获业务异常（比如修改不存在的学生）
    @ExceptionHandler(RuntimeException.class) // 捕获运行时异常
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // 返回500状态码（服务器内部错误）
    @ResponseBody
    public Map<String, Object> handleRuntimeException(RuntimeException e) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 500);
        result.put("message", e.getMessage()); // 业务异常的具体提示
        result.put("errors", null);
        return result;
    }
}