/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.brucexx.aries.enums;

import org.apache.commons.lang.StringUtils;

/**
 * aries���ö��
 * @author zhao.xiong
 * @version $Id: ConfigServerEnum.java, v 0.1 2013-4-11 ����4:15:16 zhao.xiong Exp $
 */
public enum AriesEnum {

    /**ע�����ĵ�ַ�� ,ip�Լ��ֺŷָ�,ip1:port;ip2:port**/
    CENTER_HOST_POOL("CENTER_HOST_POOL", "ע�����ĵ�ַ�� ,ip�Լ��ֺŷָ�"),

    /**���÷�������ַ�� ,ip�Լ��ֺŷָ�,ip1:port;ip2:port**/
    CONFIG_HOST_POOL("CONFIG_HOST_POOL", "*���÷�������ַ�� ,ip�Լ��ֺŷָ�"),

    /**ע�����ļ����˿� **/
    DEFAULT_CENTER_PORT("CENTER_LISTEN_PORT", "ע�����ı��ؼ����˿�"),

    /**���÷����������˿� **/
    DEFAULT_CONFIG_PORT("CONFIG_LISTEN_PORT", "���÷������ı��ؼ����˿�"),

    /**Ӧ��log���� **/
    APP_LOGGING_LEVEL("APP_LOGGING_LEVEL", "Ӧ��log����"),

    /**���÷�����ģʽ **/
    CONFIG_MODE("CONFIG_MODE", "���÷�����ģʽ���ж���,CENTER��DIRECT����ģʽ");

    ;

    private String code;

    private String desc;

    private AriesEnum(String code, String desc) {
        this.desc = desc;
        this.code = code;
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
    public String getDesc() {
        return this.desc;
    }

    /**
     * ����ö��ֵ��ȡö��
     * 
     * @param code
     * @return
     */
    public static AriesEnum getEnumByCode(String code) {
        for (AriesEnum resultCode : values()) {
            if (StringUtils.equalsIgnoreCase(resultCode.getCode(), code)) {
                return resultCode;
            }
        }
        return null;
    }
}
