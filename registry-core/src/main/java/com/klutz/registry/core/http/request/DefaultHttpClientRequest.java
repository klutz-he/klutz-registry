package com.klutz.registry.core.http.request;

import com.klutz.registry.core.constant.MediaType;
import com.klutz.registry.core.http.BaseHttpMethod;
import com.klutz.registry.core.http.Header;
import com.klutz.registry.core.http.RequestHttpEntity;
import com.klutz.registry.core.http.response.DefaultClientHttpResponse;
import com.klutz.registry.core.http.response.HttpClientResponse;
import com.klutz.registry.core.utils.HttpUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;
import java.net.URI;
import java.util.Iterator;
import java.util.Map;

/**
 * created on 2021/12/8
 * @author klutz
 */
public class DefaultHttpClientRequest implements HttpClientRequest{

    private final CloseableHttpClient client;

    public DefaultHttpClientRequest(CloseableHttpClient client) {
        this.client = client;
    }

    static HttpRequestBase build(URI uri, String method, RequestHttpEntity requestHttpEntity) throws Exception {
        final Header header = requestHttpEntity.getHeader();
        final BaseHttpMethod httpMethod = BaseHttpMethod.sourceOf(method);
        final HttpRequestBase httpRequestBase = httpMethod.init(uri.toString());

        Iterator<Map.Entry<String, String>> iterator = header.getHeader().entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            httpRequestBase.setHeader(entry.getKey(), entry.getValue());
        }

        if (MediaType.APPLICATION_FORM_URLENCODED.equals(header.getValue(HttpHeaders.CONTENT_TYPE))
                && requestHttpEntity.getBody() instanceof Map) {
            HttpUtils.initRequestFromEntity(httpRequestBase, (Map<String, String>) requestHttpEntity.getBody(), header.getCharset());
        } else {
            HttpUtils.initRequestEntity(httpRequestBase, requestHttpEntity.getBody(), header);
        }
        return httpRequestBase;
    }

    @Override
    public HttpClientResponse execute(URI uri, String httpMethod, RequestHttpEntity requestHttpEntity) throws Exception {
        HttpRequestBase request = build(uri, httpMethod, requestHttpEntity);
        CloseableHttpResponse response = client.execute(request);
        return new DefaultClientHttpResponse(response);
    }

    @Override
    public void close() throws IOException {
        client.close();
    }
}
