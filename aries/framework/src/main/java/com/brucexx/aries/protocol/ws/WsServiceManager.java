/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.brucexx.aries.protocol.ws;

import java.util.HashSet;
import java.util.Set;

import javax.xml.ws.Endpoint;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import com.brucexx.aries.exception.AriesException;
import com.brucexx.aries.protocol.AriesServiceManager;
import com.brucexx.aries.tag.ServiceModel;
import com.brucexx.aries.tag.TagModelCache;

/**
 * 
 * @author zhao.xiong
 * @version $Id: WsService.java, v 0.1 2012-8-10 下午03:53:54 zhao.xiong Exp $
 */
public final class WsServiceManager implements AriesServiceManager, ApplicationListener,
                                   BeanFactoryAware {

    private BeanFactory beanFactory;

    /**key为service 的 id **/
    private Set<String> set = new HashSet<String>();

    @Override
    public void registerService(ServiceModel model) {
        logger.info("注册webservice服务==>" + model);
        set.add(model.getId());
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ContextRefreshedEvent) {
            //容器加载完毕再 启动服务
            try {
                logger.info("系统开始加载webservice服务bean");
                try {
                    startUp();
                } catch (Exception e) {
                    logger.error("ws启动出错", e);
                }
            } finally {
                logger.info("系统加载webservice服务bean完成");
            }
        }
    }

    @Override
    public void startUp() throws Exception {
        for (String serviceId : set) {
            //加载服务
            ServiceModel serviceModel = TagModelCache.getService(serviceId);
            try {

                if (serviceModel != null) {
                    String refBeanId = serviceModel.getRefBean();
                    String _interface = serviceModel.get_interface();
                    if (StringUtils.isEmpty(refBeanId)) {
                        throw new AriesException("refBeanId:" + refBeanId + "不能为空");

                    }
                    if (StringUtils.isEmpty(_interface)) {
                        throw new AriesException("serviceInterface:" + _interface + "不能为空");
                    }
                    if (beanFactory.getBean(refBeanId) == null) {
                        throw new AriesException("refBean:" + refBeanId + "不能为空");
                    }

                    Endpoint.publish("http://localhost:8000/" + _interface,
                        beanFactory.getBean(refBeanId));

                    logger.info(serviceModel + "webservice启动成功！");
                    //TODO 向configService注册节点信息

                } else {
                    logger.warn("serviceId:" + serviceId + "没有注册对应组件");
                }
            } catch (Exception e) {
                //加载服务失败
                logger.error(serviceModel + "启动失败", e);
            }
        }
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

}
