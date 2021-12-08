package com.klutz.registry.core.http;

import com.fasterxml.jackson.databind.JavaType;
import com.klutz.registry.core.http.handler.BeanResponseHandler;
import com.klutz.registry.core.http.handler.ResponseHandler;
import com.klutz.registry.core.http.handler.StringResponseHandler;
import com.klutz.registry.core.http.handler.VoidResponseHandler;
import com.klutz.registry.core.http.request.HttpClientRequest;
import com.klutz.registry.core.http.response.HttpClientResponse;
import com.klutz.registry.core.utils.HttpUtils;
import com.klutz.registry.core.utils.JacksonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.net.URI;

/**
 * created on 2021/12/8
 *
 * @author klutz
 */
public class KlutzRestTemplate {

    private final HttpClientRequest httpClientRequest;

    private final Logger logger = LoggerFactory.getLogger(KlutzRestTemplate.class);

    private final StringResponseHandler stringResponseHandler = new StringResponseHandler();

    private final VoidResponseHandler voidResponseHandler = new VoidResponseHandler();

    private final BeanResponseHandler<?> beanResponseHandler = new BeanResponseHandler<>();

    public KlutzRestTemplate(HttpClientRequest httpClientRequest) {
        this.httpClientRequest = httpClientRequest;
    }

    public <T> HttpRestResult<T> exchange(String url, String method, Header header, Query query, Object body, Type type) throws Exception {
        return execute(url, method, new RequestHttpEntity(header, query, body), type);
    }

    public <T> HttpRestResult<T> get(String url, Header header, Query query, Type type) throws Exception {
        return execute(url, "GET", new RequestHttpEntity(header, query, null), type);
    }

    public <T> HttpRestResult<T> post(String url, Header header, Object body, Type type) throws Exception {
        return execute(url, "POST", new RequestHttpEntity(header, null, body), type);
    }

    public <T> HttpRestResult<T> put(String url, Header header, Object body, Type type) throws Exception {
        return execute(url, "PUT", new RequestHttpEntity(header, null, body), type);
    }

    public <T> HttpRestResult<T> delete(String url, Header header, Query query, Type type) throws Exception {
        return execute(url, "DELETE", new RequestHttpEntity(header, query, null), type);
    }

    @SuppressWarnings("unchecked")
    private <T> HttpRestResult<T> execute(String url, String httpMethod, RequestHttpEntity requestEntity,
                                          Type responseType) throws Exception {
        URI uri = HttpUtils.buildUri(url, requestEntity.getQuery());
        if (logger.isDebugEnabled()) {
            logger.debug("HTTP method: {}, url: {}, body: {}", httpMethod, uri, requestEntity.getBody());
        }

        ResponseHandler<T> responseHandler = selectResponseHandler(responseType);
        HttpClientResponse response = null;
        try {
            response = httpClientRequest.execute(uri, httpMethod, requestEntity);
            return responseHandler.handle(response);
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

    private ResponseHandler selectResponseHandler(Type responseType) {
        ResponseHandler responseHandler = null;
        if (responseType == null) {
            responseHandler = voidResponseHandler;
        } else {
            JavaType javaType = JacksonUtils.constructJavaType(responseType);
            String name = javaType.getRawClass().getName();
            if (name.equals("java.lang.String")) {
                responseHandler = stringResponseHandler;
            } else if (name.equals("java.lang.Void")) {

                responseHandler = voidResponseHandler;
            } else {
                responseHandler = beanResponseHandler;
            }
        }
        responseHandler.setResponseType(responseType);
        return responseHandler;
    }

}
