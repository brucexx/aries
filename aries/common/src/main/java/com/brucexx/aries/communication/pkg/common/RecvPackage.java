/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.brucexx.aries.communication.pkg.common;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * ��Ӧ���հ�
 * @author zhao.xiong
 * @version $Id: RecvPackage.java, v 0.1 2013-4-18 ����11:51:36 zhao.xiong Exp $
 */
public class RecvPackage implements MsgPackage {

    /**  */
    private static final long serialVersionUID = -8152347497084536726L;

    /** ����һ����Ϣ��Ψһ��ʶ uuid+10λ�����**/
    private String            msgId;

    /**�Ƿ�ע��ɹ�/��ȡ��Ϣ�ɹ� **/
    private boolean           isSuccess;

    /** ������Ϣ**/
    private String            errorMsg;

    /**�쳣ʱ���ض�ջ��Ϣ **/
    private Exception         exception;

    public RecvPackage() {

    }

    public RecvPackage(String msgId) {
        this.msgId = msgId;
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

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }
    
    @Override
    public String toString(){
        return ToStringBuilder.reflectionToString(this);
    }
    

}
