/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.brucexx.aries.context;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;

import com.brucexx.aries.server.ConfigServerEnum;
import com.brucexx.aries.util.MsgUtil;

/**
 * configServer/configCenter ��������
 * @author zhao.xiong
 * @version $Id: ConfigContext.java, v 0.1 2013-4-18 ����5:14:00 zhao.xiong Exp $
 */
public class ConfigContext {

    private static Logger                        logger = Logger.getLogger("aries-config");

    private static Map<ConfigServerEnum, String> map    = new HashMap<ConfigServerEnum, String>();

    /**
     * ��������
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
                ConfigServerEnum confEnum = ConfigServerEnum.getEnumByCode(key);
                if (confEnum == null) {
                    logger.warn("�޷��ҵ�" + key + "��Ӧ����ö��");
                } else {
                    map.put(confEnum, val);
                }
            }
            logger.info("���ص����óɹ�:" + MsgUtil.LINE_END + map);

        } catch (Exception e) {
            logger.error("����" + filepath + "����ʧ��", e);
        } finally {
            IOUtils.closeQuietly(fis);
        }
    }

    public static String get(ConfigServerEnum confEnum) {
        return map.get(confEnum);
    }

    public static int getCenterHostPort() {
        return NumberUtils.toInt(map.get(ConfigServerEnum.DEFAULT_CENTER_PORT), 13147);
    }

    public static int getConfigHostPort() {
        return NumberUtils.toInt(map.get(ConfigServerEnum.DEFAULT_CONFIG_PORT), 13148);
    }
}
