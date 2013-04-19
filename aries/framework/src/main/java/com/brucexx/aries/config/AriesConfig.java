/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.brucexx.aries.config;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.brucexx.aries.enums.AriesEnum;
import com.brucexx.aries.util.MsgUtil;

/**
 * aries框架上下文件，可从这里获取对应的配置枚举
 * @author zhao.xiong
 * @version $Id: AriesContext.java, v 0.1 2013-4-19 下午5:16:08 zhao.xiong Exp $
 */
public class AriesConfig {

    private static Logger                 logger = Logger.getLogger("aries-core");

    private static Map<AriesEnum, String> map    = new HashMap<AriesEnum, String>();

    /**
     * 加载配置
     * 
     * @param filepath
     */
    public static void loadProperties(String filepath) {
        FileInputStream fis = null;
        Properties prop = new Properties();
        try {
            fis = new FileInputStream(filepath);
            prop.load(fis);
            for (Map.Entry<Object, Object> entry : prop.entrySet()) {
                String key = entry.getKey().toString();
                String val = entry.getValue().toString();
                AriesEnum confEnum = AriesEnum.getEnumByCode(key);
                if (confEnum == null) {
                    logger.warn("无法找到" + key + "对应配置枚举");
                } else {
                    map.put(confEnum, val);
                }
            }
            logger.info("加载的配置成功:" + MsgUtil.LINE_END + map);

        } catch (Exception e) {
            logger.error("加载" + filepath + "配置失败", e);
        } finally {
            IOUtils.closeQuietly(fis);
        }
    }

    public static String get(AriesEnum arieEnum) {
        return map.get(arieEnum);
    }

}
