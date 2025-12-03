package com.eatnote.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private boolean success;

    /**
     * Response status code
     */
    private String code;

    /**
     * Response message
     */
    private String message;

    /**
     * Response data
     */
    private T data;

//    /**
//     * Response timestamp (ISO 8601 format)
//     */
//    private String timestamp;
//
//    /**
//     * Request ID for tracing
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