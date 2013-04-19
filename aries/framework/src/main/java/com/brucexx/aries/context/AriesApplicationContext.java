/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.brucexx.aries.context;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import com.brucexx.aries.protocol.manager.AriesReferenceManager;
import com.brucexx.aries.protocol.manager.AriesServiceManager;

/**
 * aries应用上下文
 * @author zhao.xiong
 * @version $Id: AriesContext.java, v 0.1 2013-4-25 下午4:40:35 zhao.xiong Exp $
 */
public class AriesApplicationContext implements ApplicationContextAware, ApplicationListener {

    private ApplicationContext applicationContext;

    /** 
     * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /** 
     * @see org.springframework.context.ApplicationListener#onApplicationEvent(org.springframework.context.ApplicationEvent)
     */
    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ContextRefreshedEvent) {
            //刷新事件后，开始收集对应soa 组件
             
            
          
            

        }
    }

}
