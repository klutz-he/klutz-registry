package com.klutz.registry.core.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.klutz.registry.core.exception.KlutzException;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;

/**
 * created on 2021/12/8
 * @author klutz
 */
public final class JacksonUtils {

    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public static String toJson( Object object ){
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new KlutzException(e);
        }
    }

    public static <T> T readValue( String json,Class<T> clz ){
        try {
            return mapper.readValue(json,clz);
        } catch (Throwable e) {
            throw new KlutzException(e);
        }
    }

    public static <T> T toObj(InputStream inputStream, Type responseType) {
        try {
            return mapper.readValue(inputStream, mapper.constructType(responseType));
        } catch (IOException e) {
            throw new KlutzException(e);
        }
    }

    public static JavaType constructJavaType(Type type) {
        return mapper.constructType(type);
    }
}
