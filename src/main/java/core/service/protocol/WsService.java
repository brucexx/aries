/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package core.service.protocol;

import com.brucexx.core.common.tagbean.ServiceModel;

import core.common.enums.AriesProtocol;
import core.service.AriesService;

/**
 * 
 * @author zhao.xiong
 * @version $Id: WsService.java, v 0.1 2012-8-10 ÏÂÎç03:53:54 zhao.xiong Exp $
 */
public class WsService implements AriesService {

    private ServiceModel serviceModel;

    /** 
     * @see core.service.AriesService#startUp()
     */
    @Override
    public void startUp() throws Exception {
    }

    /** 
     * @see core.service.AriesService#getProtocol()
     */
    @Override
    public AriesProtocol getProtocol() {
        return AriesProtocol.getEnumByCode(serviceModel.getProtocol().toUpperCase());
    }

    /** 
     * @see core.service.AriesService#setServiceModel(com.brucexx.core.common.tagbean.ServiceModel)
     */
    @Override
    public void setServiceModel(ServiceModel model) {
        this.serviceModel = model;
    }

}
