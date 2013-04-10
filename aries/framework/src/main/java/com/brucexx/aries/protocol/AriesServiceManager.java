/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.brucexx.aries.protocol;

import org.apache.log4j.Logger;

import com.brucexx.aries.tag.ServiceModel;

/**
 * 
 * @author zhao.xiong
 * @version $Id: AriesService.java, v 0.1 2012-8-10 下午03:46:57 zhao.xiong Exp $
 */
public interface AriesServiceManager {

    Logger logger = Logger.getLogger("aries-core");

    /**
     * 启动服务
     * 
     * @throws Exception
     */
    void startUp() throws Exception;

    /**
     * service模型
     */
    void registerService(ServiceModel model);

}
