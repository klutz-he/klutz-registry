package com.klutz.registry.core.http;

/**
 * created on 2021/12/7
 * @author klutz
 */
public class RequestHttpEntity {

    private final Header header;

    private final Query query;

    private final Object body;

    public RequestHttpEntity(Header header, Query query, Object body) {
        this.header = header;
        this.query = query;
        this.body = body;
    }

    public Header getHeader() {
        return header;
    }

    public Query getQuery() {
        return query;
    }

    public Object getBody() {
        return body;
    }
}
