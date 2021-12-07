package com.klutz.registry.server.controller;

import com.klutz.registry.core.entity.ServiceInfo;
import com.klutz.registry.server.service.PeerAwareInstanceRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * created on 2021/12/7
 * @author registry
 */
@Controller
@RequestMapping(value = "/service")
public class ServiceController {

    @Autowired
    private PeerAwareInstanceRegistry peerAwareInstanceRegistry;

    @GetMapping(value = "/{serviceId}")
    public ResponseEntity<ServiceInfo> getServiceInfo(  @PathVariable String serviceId ){

        ServiceInfo serviceInfo = peerAwareInstanceRegistry.getServiceInfo( serviceId);
        if( serviceInfo == null ){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }else {
            return ResponseEntity.ok(serviceInfo);
        }
    }

}
