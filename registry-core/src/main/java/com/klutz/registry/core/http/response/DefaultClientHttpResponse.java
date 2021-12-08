package com.klutz.registry.core.http.response;

import com.klutz.registry.core.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.utils.HttpClientUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * created on 2021/12/8
 * @author klutz
 */
public class DefaultClientHttpResponse implements HttpClientResponse {
    
    private HttpResponse response;
    
    private Header responseHeader;
    
    public DefaultClientHttpResponse(HttpResponse response) {
        this.response = response;
    }
    
    @Override
    public int getStatusCode() {
        return this.response.getStatusLine().getStatusCode();
    }

    @Override
    public Header getHeader() {
        if (this.responseHeader == null) {
            this.responseHeader = new Header();
            org.apache.http.Header[] allHeaders = response.getAllHeaders();
            for (org.apache.http.Header header : allHeaders) {
                this.responseHeader.addParam(header.getName(), header.getValue());
            }
        }
        return this.responseHeader;
    }
    
    @Override
    public InputStream getBody() throws IOException {
        return response.getEntity().getContent();
    }
    
    @Override
    public void close() {
        try {
            if (this.response != null) {
                HttpClientUtils.closeQuietly(response);
            }
        } catch (Exception ex) {
            // ignore
        }
    }
}
