/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.brucexx.aries.communication.pkg.common;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 响应接收包
 * @author zhao.xiong
 * @version $Id: RecvPackage.java, v 0.1 2013-4-18 上午11:51:36 zhao.xiong Exp $
 */
public class RecvPackage implements MsgPackage {

    /**  */
    private static final long serialVersionUID = -8152347497084536726L;

    /** 用于一次消息的唯一标识 uuid+10位随机数**/
    private String            msgId;

    /**是否注册成功/获取信息成功 **/
    private boolean           isSuccess;

    /** 错误信息**/
    private String            errorMsg;

    /**异常时返回堆栈信息 **/
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
