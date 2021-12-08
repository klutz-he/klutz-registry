package com.klutz.registry.core.http.handler;

import com.klutz.registry.core.http.Header;
import com.klutz.registry.core.http.HttpRestResult;
import com.klutz.registry.core.http.response.HttpClientResponse;
import org.apache.commons.io.IOUtils;

import java.lang.reflect.Type;

/**
 * created on 2021/12/8
 * @author klutz
 */
public class VoidResponseHandler extends ResponseHandler<Void>{

    @Override
    public HttpRestResult<Void> convertResult(HttpClientResponse response, Type responseType) throws Exception {
        final Header headers = response.getHeader();
        return new HttpRestResult<Void>(headers, response.getStatusCode(),null , null);

    }
}
