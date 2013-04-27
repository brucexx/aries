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
 * @version $Id: AbstractReferenceModel.java, v 0.1 2013-4-25 ����5:00:38 zhao.xiong Exp $
 */
public class AbstractReferenceManager implements AriesReferenceManager {

    protected Logger                               logger = Logger.getLogger("aries-core");

    /**��������ģ��,ע���ref bean���ݶ������� keyΪbean id**/
    protected HashMap<String, SOAInfoAdaptor> refMap = new HashMap<String, SOAInfoAdaptor>();

    /**
     * serviceģ��
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
     * ��ȡ������creator
     * @param referenceId
     * @return
     */
    public SOAInfoAdaptor getCreator(String referenceId) {
        return refMap.get(referenceId);
    }

}
