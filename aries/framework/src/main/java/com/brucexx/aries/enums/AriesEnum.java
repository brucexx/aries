/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.brucexx.aries.enums;

import org.apache.commons.lang.StringUtils;

/**
 * aries框架枚举
 * @author zhao.xiong
 * @version $Id: ConfigServerEnum.java, v 0.1 2013-4-11 下午4:15:16 zhao.xiong Exp $
 */
public enum AriesEnum {

    /**注册中心地址池 ,ip以及分号分隔,ip1:port;ip2:port**/
    CENTER_HOST_POOL("CENTER_HOST_POOL", "注册中心地址池 ,ip以及分号分隔"),

    /**配置服务器地址池 ,ip以及分号分隔,ip1:port;ip2:port**/
    CONFIG_HOST_POOL("CONFIG_HOST_POOL", "*配置服务器地址池 ,ip以及分号分隔"),

    /**注册中心监听端口 **/
    DEFAULT_CENTER_PORT("CENTER_LISTEN_PORT", "注册中心本地监听端口"),

    /**配置服务器监听端口 **/
    DEFAULT_CONFIG_PORT("CONFIG_LISTEN_PORT", "配置服务器的本地监听端口"),

    /**应用log级别 **/
    APP_LOGGING_LEVEL("APP_LOGGING_LEVEL", "应用log级别"),

    /**配置服务器模式 **/
    CONFIG_MODE("CONFIG_MODE", "配置服务器模式，有二种,CENTER或DIRECT两种模式");

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
     * 根据枚举值获取枚举
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
