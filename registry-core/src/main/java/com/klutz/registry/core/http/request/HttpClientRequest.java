package com.klutz.registry.core.http.request;

import com.klutz.registry.core.http.response.HttpClientResponse;
import com.klutz.registry.core.http.RequestHttpEntity;

import java.io.Closeable;
import java.net.URI;

/**
 * created on 2021/12/7
 * @author klutz
 */
public interface HttpClientRequest extends Closeable {

    HttpClientResponse execute(URI uri, String httpMethod, RequestHttpEntity requestHttpEntity) throws Exception ;

}
