/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.brucexx.aries.protocol;

/**
 * 配置服务器的信息状态
 * @author zhao.xiong
 * @version $Id: NodeInfoPackage.java, v 0.1 2013-4-15 下午5:07:18 zhao.xiong Exp $
 */
public class NodeInfoPackage implements MsgPackage {

    /**  */
    private static final long serialVersionUID = 4769955086861388056L;

    /** uuid+10位随机**/
    private String            msgId;

    /** 配置服务器的信息**/
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
