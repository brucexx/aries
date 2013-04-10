package com.brucexx.aries.protocol;

import org.apache.log4j.Logger;

import com.brucexx.aries.tag.ReferenceModel;

public interface AriesReferenceManager {

    Logger logger = Logger.getLogger("aries-core");

    /**
     * serviceģ��
     */
    void registerReference(ReferenceModel model);

}
