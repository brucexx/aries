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
 * @version $Id: AriesService.java, v 0.1 2012-8-10 ����03:46:57 zhao.xiong Exp $
 */
public interface AriesService {

    /**
     * ��������
     * 
     * @throws Exception
     */
    void startUp() throws Exception;

    /**
     * ��ȡ����Э��
     * 
     * @return
     */
    AriesProtocol getProtocol();

    /**
     * serviceģ��
     */
    void setServiceModel(ServiceModel model);

}
