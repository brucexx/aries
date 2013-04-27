/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.brucexx.aries.protocol.manager.ws;

import javax.xml.ws.Endpoint;

import com.brucexx.aries.config.ConfigClientFactory;
import com.brucexx.aries.protocol.adaptor.SOAInfoAdaptor;
import com.brucexx.aries.protocol.manager.AbstractServiceManager;
import com.brucexx.aries.tag.model.ServiceModel;

/**
 * 
 * @author zhao.xiong
 * @version $Id: WsService.java, v 0.1 2012-8-10 ����03:53:54 zhao.xiong Exp $
 */
public final class WsServiceManager extends AbstractServiceManager {

    /**
     * 
     * @param pb
     */
    protected void setProcessor(SOAInfoAdaptor pbc) {
        ServiceModel model = (ServiceModel) pbc.getTagModel();
        logger.info("ע��webservice����==>" + model);
        //����
        Endpoint.publish("http://localhost:8000/" + model.getInterface(),
            beanFactory.getBean(model.getRefBean()));

        logger.info(model + "webservice�����ɹ���");
        //����������ע��
        ConfigClientFactory.regServiceInfo(model);
    }

}
