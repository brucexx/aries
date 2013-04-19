/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.brucexx.aries.communication.pkg.common;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 发送消息包
 * @author zhao.xiong
 * @version $Id: SendPackage.java, v 0.1 2013-4-18 下午4:31:07 zhao.xiong Exp $
 */
public class SendPackage implements MsgPackage {

    /**  */
    private static final long serialVersionUID = 3177740550412121689L;

    /** 用于一次消息的唯一标识 uuid+10位随机数**/
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
