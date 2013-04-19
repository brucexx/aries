/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.brucexx.aries.protocol.manager;

import java.util.HashMap;

import org.apache.log4j.Logger;

import com.brucexx.aries.protocol.adaptor.SOAInfoAdaptor;
import com.brucexx.aries.tag.model.ReferenceModel;

/**
 * 
 * @author zhao.xiong
 * @version $Id: AbstractReferenceModel.java, v 0.1 2013-4-25 下午5:00:38 zhao.xiong Exp $
 */
public class AbstractReferenceManager implements AriesReferenceManager {

    protected Logger                               logger = Logger.getLogger("aries-core");

    /**服务引用模型,注册的ref bean数据都在这里 key为bean id**/
    protected HashMap<String, SOAInfoAdaptor> refMap = new HashMap<String, SOAInfoAdaptor>();

    /**
     * service模型
     */
    public SOAInfoAdaptor registerReference(ReferenceModel model) {
        SOAInfoAdaptor pb = new SOAInfoAdaptor();
        pb.setTagModel(model);
        refMap.put(model.getId(), pb);
        setProcessor(pb);
        return pb;
    }

    /**
     * 
     */
    protected void setProcessor(SOAInfoAdaptor pb) {
    }

    /**
     * 获取创建的creator
     * @param referenceId
     * @return
     */
    public SOAInfoAdaptor getCreator(String referenceId) {
        return refMap.get(referenceId);
    }

}
