/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.brucexx.aries.server;

import org.apache.commons.lang.StringUtils;

/**
 * 配置中心服务属性枚举
 * @author zhao.xiong
 * @version $Id: ConfigServerEnum.java, v 0.1 2013-4-11 下午4:15:16 zhao.xiong Exp $
 */
public enum ConfigServerEnum {

    /**管理器地址 **/
    MANAGER_HOST("MANAGER_HOST", "configserver manager address"),

    /**注册管理器的私有证书,如果没有， 不进行注册节点的加载 **/
    MANAGER_PRIVATE_KEY("MANAGER_PRIVATE_KEY", "configserver manager私有证书"),

    /** 注册管理器公有证书**/
    MANAGER_PUBLIC_KEY("MANAGER_PUBLIC_KEY", "configserver manager公有证书"),

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
     * 根据枚举值获取枚举
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
