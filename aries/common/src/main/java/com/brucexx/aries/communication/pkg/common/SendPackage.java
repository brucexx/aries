/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.brucexx.aries.communication.pkg.common;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * ������Ϣ��
 * @author zhao.xiong
 * @version $Id: SendPackage.java, v 0.1 2013-4-18 ����4:31:07 zhao.xiong Exp $
 */
public class SendPackage implements MsgPackage {

    /**  */
    private static final long serialVersionUID = 3177740550412121689L;

    /** ����һ����Ϣ��Ψһ��ʶ uuid+10λ�����**/
    private String            msgId;

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
