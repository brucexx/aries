/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.brucexx.aries.protocol.manager;

import com.brucexx.aries.protocol.adaptor.SOAInfoAdaptor;
import com.brucexx.aries.tag.model.ServiceModel;

/**
 * aries服务管理
 * @author zhao.xiong
 * @version $Id: AriesService.java, v 0.1 2012-8-10 下午03:46:57 zhao.xiong Exp $
 */
public interface AriesServiceManager {

    /**
     * service模型，注册后会自动启动
     */
    SOAInfoAdaptor registerService(ServiceModel model);

    /**
     * 获取service模型
     * 
     * @param model
     * @return
     */
    SOAInfoAdaptor getCreator(String serviceId);
}
