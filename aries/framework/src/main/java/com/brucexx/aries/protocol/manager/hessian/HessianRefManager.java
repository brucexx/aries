package com.brucexx.aries.protocol.manager.hessian;

import org.springframework.stereotype.Component;

import com.brucexx.aries.protocol.adaptor.SOAInfoAdaptor;
import com.brucexx.aries.protocol.manager.AbstractReferenceManager;

@Component("hessianRefManager")
public class HessianRefManager extends AbstractReferenceManager {

    /**
     * 
     * @see com.brucexx.aries.protocol.manager.AbstractReferenceManager#setProcessor(com.brucexx.aries.protocol.adaptor.SOAInfoAdaptor)
     */
    @Override
    protected void setProcessor(SOAInfoAdaptor pb) {
        pb.setProtocolProcessor("");
    }

}
