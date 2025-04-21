package com.cream.sparkle.hero.net.component;

import com.cream.sparkle.hero.processor.base.MsgProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MsgProcessorRegister implements BeanPostProcessor {

    private final MsgDispatcher msgDispatcher;

    @Autowired
    public MsgProcessorRegister(MsgDispatcher msgDispatcher) {
        this.msgDispatcher = msgDispatcher;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof MsgProcessor<?>) {
            this.msgDispatcher.registryReqMsgProcessor((MsgProcessor<?>) bean);
        }
        return bean;
    }
}
