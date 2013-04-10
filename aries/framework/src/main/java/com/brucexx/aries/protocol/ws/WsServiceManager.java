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
 * @version $Id: WsService.java, v 0.1 2012-8-10 ����03:53:54 zhao.xiong Exp $
 */
public final class WsServiceManager implements AriesServiceManager, ApplicationListener,
                                   BeanFactoryAware {

    private BeanFactory beanFactory;

    /**keyΪservice �� id **/
    private Set<String> set = new HashSet<String>();

    @Override
    public void registerService(ServiceModel model) {
        logger.info("ע��webservice����==>" + model);
        set.add(model.getId());
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ContextRefreshedEvent) {
            //������������� ��������
            try {
                logger.info("ϵͳ��ʼ����webservice����bean");
                try {
                    startUp();
                } catch (Exception e) {
                    logger.error("ws��������", e);
                }
            } finally {
                logger.info("ϵͳ����webservice����bean���");
            }
        }
    }

    @Override
    public void startUp() throws Exception {
        for (String serviceId : set) {
            //���ط���
            ServiceModel serviceModel = TagModelCache.getService(serviceId);
            try {

                if (serviceModel != null) {
                    String refBeanId = serviceModel.getRefBean();
                    String _interface = serviceModel.get_interface();
                    if (StringUtils.isEmpty(refBeanId)) {
                        throw new AriesException("refBeanId:" + refBeanId + "����Ϊ��");

                    }
                    if (StringUtils.isEmpty(_interface)) {
                        throw new AriesException("serviceInterface:" + _interface + "����Ϊ��");
                    }
                    if (beanFactory.getBean(refBeanId) == null) {
                        throw new AriesException("refBean:" + refBeanId + "����Ϊ��");
                    }

                    Endpoint.publish("http://localhost:8000/" + _interface,
                        beanFactory.getBean(refBeanId));

                    logger.info(serviceModel + "webservice�����ɹ���");
                    //TODO ��configServiceע��ڵ���Ϣ

                } else {
                    logger.warn("serviceId:" + serviceId + "û��ע���Ӧ���");
                }
            } catch (Exception e) {
                //���ط���ʧ��
                logger.error(serviceModel + "����ʧ��", e);
            }
        }
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

}
