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
 * @version $Id: AriesService.java, v 0.1 2012-8-10 ����03:46:57 zhao.xiong Exp $
 */
public interface AriesServiceManager {

    Logger logger = Logger.getLogger("aries-core");

    /**
     * ��������
     * 
     * @throws Exception
     */
    void startUp() throws Exception;

    /**
     * serviceģ��
     */
    void registerService(ServiceModel model);

}
