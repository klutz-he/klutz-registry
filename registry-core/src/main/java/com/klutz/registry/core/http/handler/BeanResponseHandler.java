package com.klutz.registry.core.http.handler;

import com.klutz.registry.core.http.Header;
import com.klutz.registry.core.http.HttpRestResult;
import com.klutz.registry.core.http.response.HttpClientResponse;
import com.klutz.registry.core.utils.JacksonUtils;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.lang.reflect.Type;

/**
 * created on 2021/12/8
 * @author klutz
 */
public class BeanResponseHandler<T> extends ResponseHandler<T>{

    @Override
    public HttpRestResult<T> convertResult(HttpClientResponse response, Type responseType) throws Exception {
        final Header headers = response.getHeader();
        InputStream body = response.getBody();
        T extractBody = JacksonUtils.toObj(body, responseType);
        return new HttpRestResult<T>(headers, response.getStatusCode(), extractBody, null);

    }
}
