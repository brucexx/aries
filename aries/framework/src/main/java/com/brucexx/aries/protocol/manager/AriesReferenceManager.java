package com.brucexx.aries.protocol.manager;

import com.brucexx.aries.protocol.adaptor.SOAInfoAdaptor;
import com.brucexx.aries.tag.model.ReferenceModel;

public interface AriesReferenceManager {

    /**
     * service模型，注册后返回对应的代理
     */
    SOAInfoAdaptor registerReference(ReferenceModel model);

    /**
     * 获取创建的creator
     * @param referenceId
     * @return
     */
    SOAInfoAdaptor getCreator(String referenceId);

}
