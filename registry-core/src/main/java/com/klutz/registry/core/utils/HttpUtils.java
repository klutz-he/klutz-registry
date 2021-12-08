package com.klutz.registry.core.utils;

import com.klutz.registry.core.http.Query;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
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
}
