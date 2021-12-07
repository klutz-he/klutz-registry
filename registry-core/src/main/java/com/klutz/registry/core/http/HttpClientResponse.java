package com.klutz.registry.core.http;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

/**
 * created on 2021/12/7
 * @author klutz
 */
public interface HttpClientResponse extends Closeable {

    Header getHeader();

    InputStream getBody() throws IOException;

    Integer getStatusCode();

    @Override
    void close();

}
