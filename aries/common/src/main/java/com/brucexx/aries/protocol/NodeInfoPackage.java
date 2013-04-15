/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.brucexx.aries.protocol;

/**
 * ���÷���������Ϣ״̬
 * @author zhao.xiong
 * @version $Id: NodeInfoPackage.java, v 0.1 2013-4-15 ����5:07:18 zhao.xiong Exp $
 */
public class NodeInfoPackage implements MsgPackage {

    /**  */
    private static final long serialVersionUID = 4769955086861388056L;

    /** uuid+10λ���**/
    private String            msgId;

    /** ���÷���������Ϣ**/
    private ConfigNodeInfo    configNodeInfo;

    public String getMsgId() {
        return msgId;
    }

    public ConfigNodeInfo getConfigNodeInfo() {
        return configNodeInfo;
    }

    public void setConfigNodeInfo(ConfigNodeInfo configNodeInfo) {
        this.configNodeInfo = configNodeInfo;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

}
