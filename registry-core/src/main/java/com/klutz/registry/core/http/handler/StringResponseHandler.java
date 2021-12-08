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
public class StringResponseHandler extends ResponseHandler<String>{

    @Override
    public HttpRestResult<String> convertResult(HttpClientResponse response, Type responseType) throws Exception {
        final Header headers = response.getHeader();
        String extractBody = IOUtils.toString(response.getBody(), headers.getCharset());
        return new HttpRestResult<String>(headers, response.getStatusCode(),extractBody , null);

    }
}
