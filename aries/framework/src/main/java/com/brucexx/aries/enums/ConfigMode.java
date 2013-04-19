/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.brucexx.aries.enums;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * @author zhao.xiong
 * @version $Id: ConfigMode.java, v 0.1 2013-4-25 下午1:36:08 zhao.xiong Exp $
 */
public enum ConfigMode {

    CENTER,

    DIRECT;

    /**
     * 根据枚举值获取枚举
     * 
     * @param code
     * @return
     */
    public static ConfigMode getEnumByCode(String code) {
        for (ConfigMode configMode : values()) {
            if (StringUtils.equals(configMode.name(), code)) {
                return configMode;
            }
        }
        return DIRECT;
    }

}
