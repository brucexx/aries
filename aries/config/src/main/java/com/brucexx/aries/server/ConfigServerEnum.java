/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.brucexx.aries.server;

import org.apache.commons.lang.StringUtils;

/**
 * �������ķ�������ö��
 * @author zhao.xiong
 * @version $Id: ConfigServerEnum.java, v 0.1 2013-4-11 ����4:15:16 zhao.xiong Exp $
 */
public enum ConfigServerEnum {

    /**��������ַ **/
    MANAGER_HOST("MANAGER_HOST", "configserver manager address"),

    /**ע���������˽��֤��,���û�У� ������ע��ڵ�ļ��� **/
    MANAGER_PRIVATE_KEY("MANAGER_PRIVATE_KEY", "configserver manager˽��֤��"),

    /** ע�����������֤��**/
    MANAGER_PUBLIC_KEY("MANAGER_PUBLIC_KEY", "configserver manager����֤��"),

    ;

    private String code;

    private String desc;

    private ConfigServerEnum(String code, String desc) {
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
    public static ConfigServerEnum getEnumByCode(String code) {
        for (ConfigServerEnum resultCode : values()) {
            if (StringUtils.equals(resultCode.getCode(), code)) {
                return resultCode;
            }
        }
        return null;
    }
}
