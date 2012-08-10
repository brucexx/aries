/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package core.service;

import com.brucexx.core.common.tagbean.ServiceModel;

import core.common.enums.AriesProtocol;

/**
 * 
 * @author zhao.xiong
 * @version $Id: AriesService.java, v 0.1 2012-8-10 下午03:46:57 zhao.xiong Exp $
 */
public interface AriesService {

    /**
     * 启动服务
     * 
     * @throws Exception
     */
    void startUp() throws Exception;

    /**
     * 获取服务协议
     * 
     * @return
     */
    AriesProtocol getProtocol();

    /**
     * service模型
     */
    void setServiceModel(ServiceModel model);

}
