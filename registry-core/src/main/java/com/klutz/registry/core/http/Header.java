package com.klutz.registry.core.http;

import com.klutz.registry.core.constant.HttpHeaderConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * created on 2021/12/7
 * @author klutz
 */
public class Header {

    private Map<String, String> header;

    public static final Header EMPTY = new Header();

    public Header(){
        this.header = new HashMap<>();
    }

    public Map<String, String> getHeader() {
        return header;
    }

    public String getValue(String key) {
        return header.get(key);
    }

    public String getCharset() {
        String acceptCharset = getValue(HttpHeaders.ACCEPT_CHARSET);
        if(StringUtils.isBlank(acceptCharset)){
            acceptCharset = "UTF-8";
        }
        return acceptCharset;
    }

    public Header addParam(String key, String value) {
        if (StringUtils.isNotEmpty(key)) {
            header.put(key, value);
        }
        return this;
    }

    public void setOriginalResponseHeader(Map<String, List<String>> headerFields) {
        if( headerFields == null ){
            return;
        }
        for (Map.Entry<String, List<String>> entry : headerFields.entrySet()) {
            addParam(entry.getKey(), entry.getValue().get(0));
        }
    }
}
