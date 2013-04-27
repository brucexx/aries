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
 * aries��������ļ����ɴ������ȡ��Ӧ������ö��
 * @author zhao.xiong
 * @version $Id: AriesContext.java, v 0.1 2013-4-19 ����5:16:08 zhao.xiong Exp $
 */
public class AriesConfig {

    private static Logger                 logger = Logger.getLogger("aries-core");

    private static Map<AriesEnum, String> map    = new HashMap<AriesEnum, String>();

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
                AriesEnum confEnum = AriesEnum.getEnumByCode(key);
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

    public static String get(AriesEnum arieEnum) {
        return map.get(arieEnum);
    }

}
