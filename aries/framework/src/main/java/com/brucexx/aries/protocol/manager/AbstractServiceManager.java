/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.brucexx.aries.protocol.manager;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;

import com.brucexx.aries.exception.AriesException;
import com.brucexx.aries.protocol.adaptor.SOAInfoAdaptor;
import com.brucexx.aries.tag.model.ServiceModel;

/**
 * 服务管理器抽象
 * @author zhao.xiong
 * @version $Id: AbstractServiceModel.java, v 0.1 2013-4-25 下午5:14:53 zhao.xiong Exp $
 */
public class AbstractServiceManager implements AriesServiceManager {

    protected Logger                           logger     = Logger.getLogger("aries-core");

    /**服务注册模型  key为service的**/
    protected Map<String, SOAInfoAdaptor> serviceMap = new HashMap<String, SOAInfoAdaptor>();

    protected BeanFactory                      beanFactory;

    /**
     * service模型
     */
    public SOAInfoAdaptor registerService(ServiceModel model) {
        SOAInfoAdaptor pb = new SOAInfoAdaptor();
        pb.setTagModel(model);
        serviceMap.put(model.getId(), pb);
        setProcessor(pb);
        return pb;
    }

    /**
     * 获取创建的creator
     * @see com.brucexx.aries.protocol.manager.AriesServiceManager#getCreator(java.lang.String)
     */
    @Override
    public SOAInfoAdaptor getCreator(String serviceId) {
        return serviceMap.get(serviceId);
    }

    /**
     * 
     * @param pb
     */
    protected void setProcessor(SOAInfoAdaptor pb) {
    }

    /**
     * 启动
     * 
     * @param serviceModel
     */
    protected void startUp(ServiceModel serviceModel) {
        try {
            if (serviceModel != null) {
                String refBeanId = serviceModel.getRefBean();
                String _interface = serviceModel.getInterface();
                if (StringUtils.isEmpty(refBeanId)) {
                    throw new AriesException("refBeanId:" + refBeanId + "不能为空");

                }
                if (StringUtils.isEmpty(_interface)) {
                    throw new AriesException("serviceInterface:" + _interface + "不能为空");
                }
                if (beanFactory.getBean(refBeanId) == null) {
                    throw new AriesException("refBean:" + refBeanId + "不能为空");
                }

            } else {
                logger.warn("serviceId:" + serviceModel + "没有注册对应组件");
            }
        } catch (Exception e) {
            //加载服务失败
            logger.error(serviceModel + "启动失败", e);
        }
    }

    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

}
