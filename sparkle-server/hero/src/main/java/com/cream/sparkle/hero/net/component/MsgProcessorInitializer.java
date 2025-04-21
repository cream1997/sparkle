package com.cream.sparkle.hero.net.component;

import com.cream.sparkle.hero.processor.base.MsgProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MsgProcessorInitializer implements SmartInitializingSingleton {

    private final MsgDispatcher msgDispatcher;
    private final ApplicationContext applicationContext;


    @Autowired
    public MsgProcessorInitializer(MsgDispatcher msgDispatcher, ApplicationContext applicationContext) {
        this.msgDispatcher = msgDispatcher;
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterSingletonsInstantiated() {
        applicationContext.getBeansOfType(MsgProcessor.class)
                .values().forEach(msgDispatcher::registryReqMsgProcessor);
    }
}
