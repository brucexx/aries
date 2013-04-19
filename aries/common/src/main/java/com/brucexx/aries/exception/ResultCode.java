/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.brucexx.aries.exception;

import org.apache.commons.lang.StringUtils;

/**
 * aries���������  AR0010001   AR0xx0000   AR+3λ�Զ�������+4λseq
 * 
 * @author zhao.xiong
 * @version $Id: ResultCode.java, v 0.1 2013-4-10 ����4:10:27 zhao.xiong Exp $
 */
public enum ResultCode {

    /** ---------------------- ������:0���� ---------------------- */

    /** �ɹ� */
    SUCCESS("AR0010000", "�ɹ�"),

    /**������Ϊ�� **/
    NOT_EMPTY("AR0010001", "������Ϊ��"),

    ILLEGAL_PROTOCOL("AR0010002", "��֧�ֵ�soaЭ������"),

    NO_HOST_ADDR("AR0010003", "û��������ַ"),

    ;

    /** ö��ֵ  */
    private String code;
    /** ö������ */
    private String message;

    /**
     * ���췽��
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
     * ����ö��ֵ��ȡö��
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
     * ��ȡ��־��Ϣ
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
