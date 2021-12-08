package com.klutz.registry.server.processor;

import com.klutz.registry.server.protocol.ProtocolType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created on 2021/12/8
 * @author klutz
 */
@Component
public class Dispatcher {

    private final Logger logger = LoggerFactory.getLogger(Dispatcher.class);

    @Autowired
    private List<Processor> processors;

    private Map<ProtocolType,Processor> processorMap = new HashMap<>();

    @PostConstruct
    public void init(){
        for( Processor processor : processors ){
            processorMap.put(processor.protocolType(),processor);
        }
    }

    public void process(ProtocolType protocolType, String body) {

        Processor processor = processorMap.get(protocolType);
        if( processor == null){
            logger.error("not find protocol processor {}",protocolType.getType());
            return;
        }
        processor.processor(body);
    }
}
