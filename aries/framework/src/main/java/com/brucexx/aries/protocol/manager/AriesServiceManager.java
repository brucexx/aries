/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.brucexx.aries.protocol.manager;

import com.brucexx.aries.protocol.adaptor.SOAInfoAdaptor;
import com.brucexx.aries.tag.model.ServiceModel;

/**
 * aries�������
 * @author zhao.xiong
 * @version $Id: AriesService.java, v 0.1 2012-8-10 ����03:46:57 zhao.xiong Exp $
 */
public interface AriesServiceManager {

    /**
     * serviceģ�ͣ�ע�����Զ�����
     */
    SOAInfoAdaptor registerService(ServiceModel model);

    /**
     * ��ȡserviceģ��
     * 
     * @param model
     * @return
     */
    SOAInfoAdaptor getCreator(String serviceId);
}
