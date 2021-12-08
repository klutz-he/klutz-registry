package com.klutz.registry.server.controller;

import com.klutz.registry.core.entity.InstanceInfo;
import com.klutz.registry.core.entity.PeerNode;
import com.klutz.registry.server.exception.ParamCheckException;
import com.klutz.registry.server.service.PeerAwareInstanceRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * created on 2021/12/7
 * @author klutz
 */
@Controller
@RequestMapping(value = "/instance")
public class InstanceController {

    @Autowired
    private PeerAwareInstanceRegistry peerAwareInstanceRegistry;

    private boolean isBlank(String str) {
        return str == null || str.isEmpty();
    }

    @PostMapping
    public ResponseEntity<?> registry(@RequestBody InstanceInfo instance,
                                      @RequestHeader(PeerNode.REPLICATION_HEADER) String isReplication){
        if( isBlank(instance.getAppName())){
            throw new ParamCheckException("missing appName");
        }
        if( isBlank(instance.getIp())){
            throw new ParamCheckException("missing ip");
        }
        if( isBlank(instance.getPort())){
            throw new ParamCheckException("missing port");
        }
        if( isBlank(instance.getInstanceId())){
            throw new ParamCheckException("missing instanceId");
        }

        peerAwareInstanceRegistry.register(instance, "true".equals(isReplication));

        return ResponseEntity.ok(null);
    }

    @DeleteMapping
    public ResponseEntity<?> cancel(@RequestParam(value = "instanceId") String instanceId,
                                    @RequestParam(value = "appName") String appName,
                                    @RequestHeader(PeerNode.REPLICATION_HEADER) String isReplication){

        boolean success = peerAwareInstanceRegistry.cancel(appName,instanceId,"true".equals(isReplication));
        if( success){
            return ResponseEntity.ok(null);
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping(path = "/renew")
    public ResponseEntity<?> renew(@RequestParam(value = "instanceId") String instanceId,
                                    @RequestParam(value = "serviceName") String serviceName,
                                    @RequestHeader(PeerNode.REPLICATION_HEADER) String isReplication){

        boolean success = peerAwareInstanceRegistry.renew(serviceName,instanceId,"true".equals(isReplication));
        if( success){
            return ResponseEntity.ok(null);
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

}
