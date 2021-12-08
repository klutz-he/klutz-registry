package com.klutz.registry.core.http.response;

import com.klutz.registry.core.http.Header;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

/**
 * created on 2021/12/7
 * @author klutz
 */
public class JdkHttpClientResponse implements HttpClientResponse {

    private final HttpURLConnection conn;

    private InputStream responseStream;

    private Header responseHeader;

    public JdkHttpClientResponse(HttpURLConnection conn) {
        this.conn = conn;
    }

    @Override
    public Header getHeader() {
        if (this.responseHeader == null) {
            this.responseHeader = new Header();
        }
        this.responseHeader.setOriginalResponseHeader(conn.getHeaderFields());
        return this.responseHeader;
    }

    @Override
    public InputStream getBody() throws IOException {
        InputStream errorStream = this.conn.getErrorStream();
        this.responseStream = (errorStream != null ? errorStream : this.conn.getInputStream());
        return this.responseStream;
    }

    @Override
    public Integer getStatusCode() throws IOException{
        return conn.getResponseCode();
    }

    @Override
    public void close() {
        IOUtils.closeQuietly(responseStream);
    }
}
