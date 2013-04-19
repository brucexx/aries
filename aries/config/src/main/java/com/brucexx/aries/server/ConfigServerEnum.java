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

    /**注册中心地址池 ,ip以及分号分隔,ip1:port;ip2:port**/
    CENTER_HOST_POOL("CENTER_HOST_POOL", "注册中心地址池 ,ip以及分号分隔"),

    /**注册中心本地监听端口 **/
    DEFAULT_CENTER_PORT("CENTER_LISTEN_PORT", "注册中心本地监听端口"),

    /**配置服务器监听端口 **/
    DEFAULT_CONFIG_PORT("CONFIG_LISTEN_PORT", "配置服务器的本地监听端口"),

    /**应用log级别 **/
    APP_LOGGING_LEVEL("APP_LOGGING_LEVEL", "应用log级别"),

    /**数据源host地址  **/
    DS_URL("DS_URL", "数据源url地址"),

    /**数据源用户名 **/
    DS_USER("DS_USER", "数据源用户名"),

    /**数据源密码 **/
    DS_PWD("DS_PWD", "数据源密码"),

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
