/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.brucexx.aries.protocol;


/**
 * configServer节点注册到configServerManager的通信报文
 * @author zhao.xiong
 * @version $Id: NodeRegPackage.java, v 0.1 2013-4-11 下午5:02:18 zhao.xiong Exp $
 */
public class NodeRegPackage implements MsgPackage {

    /**  */
    private static final long serialVersionUID = -4809688750946703938L;

    /** 配置服务器唯一标识，MAC地址   **/
    private String            configServiceId;

    /** uuid+10位随机**/
    private String            msgId;

    /** 是否成功 **/
    private boolean           isSuccess        = false;

    public String getConfigServiceId() {
        return configServiceId;
    }

    public void setConfigServiceId(String configServiceId) {
        this.configServiceId = configServiceId;
    }

    public String getMsgId() {

        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

}
