/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.brucexx.aries.protocol;


/**
 * configServer�ڵ�ע�ᵽconfigServerManager��ͨ�ű���
 * @author zhao.xiong
 * @version $Id: NodeRegPackage.java, v 0.1 2013-4-11 ����5:02:18 zhao.xiong Exp $
 */
public class NodeRegPackage implements MsgPackage {

    /**  */
    private static final long serialVersionUID = -4809688750946703938L;

    /** ���÷�����Ψһ��ʶ��MAC��ַ   **/
    private String            configServiceId;

    /** uuid+10λ���**/
    private String            msgId;

    /** �Ƿ�ɹ� **/
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
