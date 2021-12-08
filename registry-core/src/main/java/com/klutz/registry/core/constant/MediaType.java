package com.klutz.registry.core.constant;

import org.apache.commons.lang3.StringUtils;

/**
 * created on 2021/12/8
 * @author klutz
 */
public final class MediaType {

    public static final String APPLICATION_FORM_URLENCODED = "application/x-www-form-urlencoded;charset=UTF-8";
    public static final String APPLICATION_JSON = "application/json;charset=UTF-8";

    private MediaType(String type, String charset) {
        this.type = type;
        this.charset = charset;
    }

    /**
     * content type.
     */
    private final String type;

    /**
     * content type charset.
     */
    private final String charset;

    public static MediaType valueOf(String contentType) {
        if (StringUtils.isEmpty(contentType)) {
            throw new IllegalArgumentException("MediaType must not be empty");
        }
        String[] values = contentType.split(";");
        String charset = "UTF-8";
        for (String value : values) {
            if (value.startsWith("charset=")) {
                charset = value.substring("charset=".length());
            }
        }
        return new MediaType(values[0], charset);
    }

    public String getType() {
        return type;
    }

    public String getCharset() {
        return charset;
    }

    @Override
    public String toString() {
        return type + ";charset=" + charset;
    }

}
