/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.brucexx.aries.config;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import com.brucexx.aries.communication.enums.AriesProtocol;
import com.brucexx.aries.enums.AriesEnum;
import com.brucexx.aries.enums.ConfigMode;
import com.brucexx.aries.tag.model.TagModel;

/**
 * 
 * @author zhao.xiong
 * @version $Id: AriesContextUtil.java, v 0.1 2013-4-24 ����11:38:42 zhao.xiong Exp $
 */
public class AriesConfigUtil {

    /**
     * ��ȡע�����ĵ�ַ��
     * 
     * @return
     */

    public static Set<InetSocketAddress> getCenterAddrPool() {
        Set<InetSocketAddress> set = new HashSet<InetSocketAddress>();
        String[] managerIpList = StringUtils
            .split(AriesConfig.get(AriesEnum.CENTER_HOST_POOL), ';');
        for (String addr : managerIpList) {
            String[] addrInfo = StringUtils.split(addr, ":");
            InetSocketAddress ipAddr = new InetSocketAddress(addrInfo[0], NumberUtils.toInt(
                addrInfo[1], getCenterHostPort()));
            set.add(ipAddr);
        }
        return set;
    }

    /**
     * ��ȡ���÷�������ַ��
     * 
     * @return
     */
    public static Set<InetSocketAddress> getConfigAddrPool() {
        Set<InetSocketAddress> set = new HashSet<InetSocketAddress>();
        String[] configIpList = StringUtils.split(AriesConfig.get(AriesEnum.CONFIG_HOST_POOL), ';');
        for (String addr : configIpList) {
            String[] addrInfo = StringUtils.split(addr, ":");
            InetSocketAddress ipAddr = new InetSocketAddress(addrInfo[0], NumberUtils.toInt(
                addrInfo[1], getCenterHostPort()));
            set.add(ipAddr);
        }
        return set;
    }

    /**
     * ��ȡע�����Ķ˿�
     * 
     * @return
     */
    public static int getCenterHostPort() {
        return NumberUtils.toInt(AriesConfig.get(AriesEnum.DEFAULT_CENTER_PORT), 13147);
    }

    /**
     * ��ȡ���÷������˿�
     * 
     * @return
     */
    public static int getConfigHostPort() {
        return NumberUtils.toInt(AriesConfig.get(AriesEnum.DEFAULT_CONFIG_PORT), 13148);
    }

    /**
     * ��ȡ�������ĵ�ģʽ
     * 
     * @return
     */
    public static ConfigMode getConfigMode() {
        return ConfigMode.getEnumByCode(AriesConfig.get(AriesEnum.CONFIG_MODE));
    }

    /**
     * 
     * @param tm
     * @return
     */
    public static String getResourceId(TagModel tm) {
        AriesProtocol protocol = AriesProtocol.getEnumByCode(tm.getProtocol());
        switch (protocol) {
            case MQ:
                return tm.getMqInfo().getTopic();
            case JVM:
            case HESSIAN:
            case WS:
            default:
                return tm.getInterface();

        }
    }

}
