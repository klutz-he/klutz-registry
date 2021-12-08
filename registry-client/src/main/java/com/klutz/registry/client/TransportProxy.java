package com.klutz.registry.client;

import com.klutz.registry.client.beat.BeatInfo;
import com.klutz.registry.core.constant.HttpHeaderConstants;
import com.klutz.registry.core.constant.MediaType;
import com.klutz.registry.core.entity.InstanceInfo;
import com.klutz.registry.core.entity.PeerNode;
import com.klutz.registry.core.exception.KlutzException;
import com.klutz.registry.core.http.*;
import com.klutz.registry.core.http.request.JdkHttpClientRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * created on 2021/12/8
 * @author klutz
 */
public class TransportProxy {

    private final Logger logger = LoggerFactory.getLogger(TransportProxy.class);

    private List<String> serverList;

    private KlutzRestTemplate klutzRestTemplate;

    public TransportProxy( String serverList) {
        if (StringUtils.isNotEmpty(serverList)) {
            this.serverList = Arrays.asList(serverList.split(","));
            for( int i = 0 ; i < this.serverList.size();i++){
                String item = this.serverList.get(i);
                if( item.endsWith("/")){
                    this.serverList.set(i,item.substring(0,item.length()));
                }
            }
        }
        klutzRestTemplate = new KlutzRestTemplate(new JdkHttpClientRequest());
    }


    public void registerService(InstanceInfo instance) {
        Header header = new Header();
        header.addParam(PeerNode.REPLICATION_HEADER,"false");
        header.addParam(HttpHeaderConstants.CONTENT_TYPE, MediaType.APPLICATION_JSON);
        reqApi("/instance", "POST", header, Query.EMPTY, instance, Void.class);
    }

    /**
     * 发送心跳
     * @param beatInfo
     */
    public boolean sendBeat(BeatInfo beatInfo) throws KlutzException {
        Map<String,String> params = new HashMap<>();
        params.put("instanceId",beatInfo.getInstanceId());
        params.put("serviceName",beatInfo.getServiceName());

        //设置为表单
        Header header = new Header();
        header.addParam(PeerNode.REPLICATION_HEADER,"false");
        header.addParam(HttpHeaderConstants.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED);

        HttpRestResult<Void> restResult = reqApi("/instance/renew", "PUT", header, Query.EMPTY, params, Void.class);
        int statusCode = restResult.getStatusCode();
        if( statusCode == 200){
            return true;
        }else if( statusCode == 404){
            return false;
        }else {
            throw new KlutzException("error renew http code "+statusCode);
        }
    }

    public <T> HttpRestResult<T> reqApi( String api,String method, Header header, Query query,
                                        Object body, Class<T> responseType) throws KlutzException{
        Random random = new Random(System.currentTimeMillis());
        int index = random.nextInt(serverList.size());
        KlutzException exception = null;
        for( int i = 0 ; i < serverList.size() ;i++){
            String server = serverList.get(index);
            try {
                return exchange(server,api,method,header,query,body,responseType);
            } catch (KlutzException e) {
                exception = e;
            }
            index = (index + 1) % serverList.size();
        }
        logger.error("req api:{} method:{} ",api,method,exception);
        throw exception;
    }

    public <T> HttpRestResult<T> exchange(String server,String api,String method, Header header, Query query,
                                          Object body, Class<T> responseType) throws KlutzException {

        try {
            String url = server;
            if( !api.startsWith("/")){
                url += "/";
            }
            url += api;
            return klutzRestTemplate.exchange(url,method,header,query,body,responseType);
        } catch (Exception exception) {
            throw new KlutzException(exception);
        }
    }


}
