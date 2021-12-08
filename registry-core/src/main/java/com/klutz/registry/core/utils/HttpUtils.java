package com.klutz.registry.core.utils;

import com.klutz.registry.core.constant.MediaType;
import com.klutz.registry.core.http.Header;
import com.klutz.registry.core.http.Query;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * created on 2021/12/8
 * @author klutz
 */
public final class HttpUtils {

    public static String encodingParams(Map<String,Object> params, String encoding ) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        if (null == params || params.isEmpty()) {
            return null;
        }
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String valueString = ( entry.getValue() == null ? null : entry.getValue().toString());

            if ( StringUtils.isEmpty(valueString)) {
                continue;
            }

            sb.append(entry.getKey()).append("=");
            sb.append(URLEncoder.encode(valueString, encoding));
            sb.append("&");
        }

        return sb.toString();
    }

    public static URI buildUri(String url, Query query) throws URISyntaxException {
        if (query != null && !query.isEmpty()) {
            url = url + "?" + query.toQueryUrl();
        }
        return new URI(url);
    }

    public static void initRequestFromEntity(HttpRequestBase requestBase, Map<String, String> body, String charset)
            throws Exception {
        if (body == null || body.isEmpty()) {
            return;
        }
        List<NameValuePair> params = new ArrayList<NameValuePair>(body.size());
        for (Map.Entry<String, String> entry : body.entrySet()) {
            params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        if (requestBase instanceof HttpEntityEnclosingRequest) {
            HttpEntityEnclosingRequest request = (HttpEntityEnclosingRequest) requestBase;
            HttpEntity entity = new UrlEncodedFormEntity(params, charset);
            request.setEntity(entity);
        }
    }

    public static void initRequestEntity(HttpRequestBase requestBase, Object body, Header header) throws Exception {
        if (body == null) {
            return;
        }
        if (requestBase instanceof HttpEntityEnclosingRequest) {
            HttpEntityEnclosingRequest request = (HttpEntityEnclosingRequest) requestBase;
            MediaType mediaType = MediaType.valueOf(header.getValue(HttpHeaders.CONTENT_TYPE));
            ContentType contentType = ContentType.create(mediaType.getType(), mediaType.getCharset());
            HttpEntity entity;
            if (body instanceof byte[]) {
                entity = new ByteArrayEntity((byte[]) body, contentType);
            } else {
                entity = new StringEntity(body instanceof String ? (String) body : JacksonUtils.toJson(body),
                        contentType);
            }
            request.setEntity(entity);
        }
    }
}
