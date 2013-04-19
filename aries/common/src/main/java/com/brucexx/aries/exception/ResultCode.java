/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.brucexx.aries.exception;

import org.apache.commons.lang.StringUtils;

/**
 * aries错误码规则  AR0010001   AR0xx0000   AR+3位自定义类型+4位seq
 * 
 * @author zhao.xiong
 * @version $Id: ResultCode.java, v 0.1 2013-4-10 下午4:10:27 zhao.xiong Exp $
 */
public enum ResultCode {

    /** ---------------------- 公用类:0×× ---------------------- */

    /** 成功 */
    SUCCESS("AR0010000", "成功"),

    /**不允许为空 **/
    NOT_EMPTY("AR0010001", "不允许为空"),

    ILLEGAL_PROTOCOL("AR0010002", "不支持的soa协议类型"),

    NO_HOST_ADDR("AR0010003", "没有主机地址"),

    ;

    /** 枚举值  */
    private String code;
    /** 枚举描述 */
    private String message;

    /**
     * 构造方法
     * 
     * @param code
     * @param message
     */
    private ResultCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    /** 
     * @see com.alipay.finsupport.component.common.lang.model.enums.BaseEnum#getCode()
     */
    public String getCode() {
        return this.code;
    }

    /** 
     * @see com.alipay.finsupport.component.common.lang.model.enums.BaseEnum#getMessage()
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * 根据枚举值获取枚举
     * 
     * @param code
     * @return
     */
    public static ResultCode getEnumByCode(String code) {
        for (ResultCode resultCode : values()) {
            if (StringUtils.equals(resultCode.getCode(), code)) {
                return resultCode;
            }
        }
        return null;
    }

    /**
     * 获取日志信息
     * 
     * @return
     */
    public String getLogMessage() {
        StringBuffer sb = new StringBuffer();
        sb.append(toString());
        sb.append("[");
        sb.append(code);
        sb.append(":");
        sb.append(message);
        sb.append("]");
        return sb.toString();
    }

}
