package com.klutz.registry.core.http.request;

import com.klutz.registry.core.constant.HttpHeaderConstants;
import com.klutz.registry.core.constant.MediaType;
import com.klutz.registry.core.http.Header;
import com.klutz.registry.core.http.response.HttpClientResponse;
import com.klutz.registry.core.http.RequestHttpEntity;
import com.klutz.registry.core.http.response.JdkHttpClientResponse;
import com.klutz.registry.core.utils.HttpUtils;
import com.klutz.registry.core.utils.JacksonUtils;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.Map;
import java.util.Objects;

/**
 * created on 2021/12/7
 * @author klutz
 */
public class JdkHttpClientRequest implements HttpClientRequest {

    @Override
    public HttpClientResponse execute(URI uri, String httpMethod, RequestHttpEntity requestHttpEntity) throws Exception {

        Header header = requestHttpEntity.getHeader();
        Object body = requestHttpEntity.getBody();

        HttpURLConnection conn = (HttpURLConnection) uri.toURL().openConnection();

        conn.setRequestMethod(httpMethod);
        if( header!=null && header.getHeader()!=null){
            for(Map.Entry<String,String> entry : header.getHeader().entrySet()){
                conn.setRequestProperty(entry.getKey(),entry.getValue());
            }
        }

        if( body != null && !"".equals(body)){
            String contentType ="";
            String charset ="";
            if( header !=null ){
                contentType = header.getValue(HttpHeaderConstants.CONTENT_TYPE);
                charset = header.getCharset();
            }

            String bodyStr = JacksonUtils.toJson(body);
            if(Objects.equals(contentType, MediaType.APPLICATION_FORM_URLENCODED)){
                if( body instanceof  Map) {
                    Map<String, Object> map = (Map<String,Object>)body;
                    bodyStr = HttpUtils.encodingParams(map, charset);
                }else {
                    Map<String, Object> map = JacksonUtils.readValue(bodyStr, Map.class);
                    bodyStr = HttpUtils.encodingParams(map, charset);
                }
            }

            if( bodyStr!=null){
                conn.setDoOutput(true);
                byte[] b = bodyStr.getBytes();
                OutputStream outputStream = conn.getOutputStream();
                outputStream.write(b, 0, b.length);
                outputStream.flush();
                IOUtils.closeQuietly(outputStream);
            }
        }

        conn.connect();

        return new JdkHttpClientResponse(conn);
    }

    @Override
    public void close() throws IOException {

    }
}
