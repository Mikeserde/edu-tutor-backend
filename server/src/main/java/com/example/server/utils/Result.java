package com.example.server.utils;

import lombok.Data;

@Data
public class Result<T> {
    private int code;
    private String message;
    private T data;

    // 成功（带数据）
    public static <T> Result<T> success(T data) {
        return success("操作成功", data);
    }

    // 成功（带消息和数据）
    public static <T> Result<T> success(String message, T data) {
        Result<T> result = new Result<>();
        result.code = 200;
        result.message = message;
        result.data = data;
        return result;
    }

    // 成功（无数据）
    public static Result<Void> success() {
        return success(null);
    }

    // 失败（带消息）
    public static <T> Result<T> fail(String message) {
        Result<T> result = new Result<>();
        result.code = 400;
        result.message = message;
        result.data = null;
        return result;
    }
}
