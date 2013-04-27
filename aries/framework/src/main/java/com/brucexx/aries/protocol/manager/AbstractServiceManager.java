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
 * �������������
 * @author zhao.xiong
 * @version $Id: AbstractServiceModel.java, v 0.1 2013-4-25 ����5:14:53 zhao.xiong Exp $
 */
public class AbstractServiceManager implements AriesServiceManager {

    protected Logger                           logger     = Logger.getLogger("aries-core");

    /**����ע��ģ��  keyΪservice��**/
    protected Map<String, SOAInfoAdaptor> serviceMap = new HashMap<String, SOAInfoAdaptor>();

    protected BeanFactory                      beanFactory;

    /**
     * serviceģ��
     */
    public SOAInfoAdaptor registerService(ServiceModel model) {
        SOAInfoAdaptor pb = new SOAInfoAdaptor();
        pb.setTagModel(model);
        serviceMap.put(model.getId(), pb);
        setProcessor(pb);
        return pb;
    }

    /**
     * ��ȡ������creator
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
     * ����
     * 
     * @param serviceModel
     */
    protected void startUp(ServiceModel serviceModel) {
        try {
            if (serviceModel != null) {
                String refBeanId = serviceModel.getRefBean();
                String _interface = serviceModel.getInterface();
                if (StringUtils.isEmpty(refBeanId)) {
                    throw new AriesException("refBeanId:" + refBeanId + "����Ϊ��");

                }
                if (StringUtils.isEmpty(_interface)) {
                    throw new AriesException("serviceInterface:" + _interface + "����Ϊ��");
                }
                if (beanFactory.getBean(refBeanId) == null) {
                    throw new AriesException("refBean:" + refBeanId + "����Ϊ��");
                }

            } else {
                logger.warn("serviceId:" + serviceModel + "û��ע���Ӧ���");
            }
        } catch (Exception e) {
            //���ط���ʧ��
            logger.error(serviceModel + "����ʧ��", e);
        }
    }

    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

}
