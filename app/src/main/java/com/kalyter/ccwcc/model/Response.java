/**
 * Created by Kalyter on 12/24/2016.
 */
package com.kalyter.ccwcc.model;

import java.util.HashMap;
import java.util.Map;

public class Response<T> {
    private int code;
    private String message;
    private T data;

    public Response() {
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
