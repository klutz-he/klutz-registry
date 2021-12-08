package com.klutz.registry.core.http;

/**
 * created on 2021/12/8
 * @author klutz
 */
public class HttpRestResult<T>{

    private Header header;

    private int statusCode;

    private T data;

    private String message;

    public HttpRestResult(Header header, int statusCode, T data, String message) {
        this.header = header;
        this.statusCode = statusCode;
        this.data = data;
        this.message = message;
    }

    public Header getHeader() {
        return header;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public boolean isOk() {
        return statusCode == 200;
    }
}
