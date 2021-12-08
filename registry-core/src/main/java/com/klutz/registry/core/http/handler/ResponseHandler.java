package com.klutz.registry.core.http.handler;

import com.klutz.registry.core.http.Header;
import com.klutz.registry.core.http.HttpRestResult;
import com.klutz.registry.core.http.response.HttpClientResponse;
import org.apache.commons.io.IOUtils;

import java.lang.reflect.Type;
import java.util.Objects;

/**
 * created on 2021/12/8
 * @author klutz
 */
public abstract class ResponseHandler<T> {

    private Type responseType;

    public void setResponseType(Type responseType) {
        this.responseType = responseType;
    }

    public final HttpRestResult<T> handle(HttpClientResponse response) throws Exception {
        return convertResult(response, this.responseType);
    }

    public abstract HttpRestResult<T> convertResult(HttpClientResponse response, Type responseType) throws Exception;

}
