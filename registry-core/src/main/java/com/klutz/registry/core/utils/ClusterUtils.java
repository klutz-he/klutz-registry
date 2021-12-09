package com.klutz.registry.core.utils;

import com.klutz.registry.core.entity.ServerInfo;
import org.apache.commons.lang3.StringUtils;

/**
 * created on 2021/12/7
 * @author klutz
 */
public class ClusterUtils {

    public static ServerInfo resolve( String ipAndPort){
        if(StringUtils.isEmpty(ipAndPort)){
            return null;
        }
        String[] split = ipAndPort.split(":");
        if( split.length != 2){
            throw new IllegalArgumentException("error ipAndPort "+ipAndPort);
        }
        return new ServerInfo(split[0],Integer.valueOf(split[1]));
    }

}
