package com.brucexx.aries.protocol.manager.jvm;

import com.brucexx.aries.protocol.adaptor.SOAInfoAdaptor;
import com.brucexx.aries.protocol.manager.AbstractReferenceManager;

public class JVMRefManager extends AbstractReferenceManager {

    /**
     * 
     * @see com.brucexx.aries.protocol.manager.AbstractReferenceManager#setProcessor(com.brucexx.aries.protocol.adaptor.SOAInfoAdaptor)
     */
    @Override
    protected void setProcessor(SOAInfoAdaptor pb) {
        pb.setProtocolProcessor("");
    }

}
