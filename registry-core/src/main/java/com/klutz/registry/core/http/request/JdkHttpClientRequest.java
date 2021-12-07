package com.klutz.registry.core.http.request;

import com.klutz.registry.core.http.Header;
import com.klutz.registry.core.http.HttpClientResponse;
import com.klutz.registry.core.http.RequestHttpEntity;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;

/**
 * created on 2021/12/7
 * @author klutz
 */
public class JdkHttpClientRequest implements HttpClientRequest {

    @Override
    public HttpClientResponse execute(URI uri, String httpMethod, RequestHttpEntity requestHttpEntity) throws Exception {

        Header header = requestHttpEntity.getHeader();

        HttpURLConnection conn = (HttpURLConnection) uri.toURL().openConnection();

        return null;
    }

    @Override
    public void close() throws IOException {

    }
}
