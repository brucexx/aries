package com.brucexx.aries.protocol.manager;

import com.brucexx.aries.protocol.adaptor.SOAInfoAdaptor;
import com.brucexx.aries.tag.model.ReferenceModel;

public interface AriesReferenceManager {

    /**
     * serviceģ�ͣ�ע��󷵻ض�Ӧ�Ĵ���
     */
    SOAInfoAdaptor registerReference(ReferenceModel model);

    /**
     * ��ȡ������creator
     * @param referenceId
     * @return
     */
    SOAInfoAdaptor getCreator(String referenceId);

}
