package com.eatnote.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private boolean success;

    /**
     * 响应状态码
     */
    private String code;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

//    /**
//     * 响应时间戳 (ISO 8601格式)
//     */
//    private String timestamp;
//
//    /**
//     * 请求ID，用于链路追踪
//     */
//    private String requestId;

    public static <T> Result<T> success(T data){
        Result<T> result=new Result<>();
        result.success=true;
        result.code="200";
        result.message="success";
        result.data=data;
        return result;
    }

    public static <T> Result<T> success() {
        return success(null);
    }


    public static <T> Result<T> success(String message, T data) {
        Result<T> result = new Result<>();
        result.success = true;
        result.code = "200";
        result.message = message;
        result.data = data;
        return result;
    }

    public static <T> Result<T> error(String message) {
        Result<T> result = new Result<>();
        result.success = false;
        result.code = "500";
        result.message = message;
        result.data = null;
        return result;
    }


}
