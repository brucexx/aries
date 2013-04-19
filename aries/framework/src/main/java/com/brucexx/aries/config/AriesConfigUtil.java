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
 * @version $Id: AriesContextUtil.java, v 0.1 2013-4-24 上午11:38:42 zhao.xiong Exp $
 */
public class AriesConfigUtil {

    /**
     * 获取注册中心地址池
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
     * 获取配置服务器地址池
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
     * 获取注册中心端口
     * 
     * @return
     */
    public static int getCenterHostPort() {
        return NumberUtils.toInt(AriesConfig.get(AriesEnum.DEFAULT_CENTER_PORT), 13147);
    }

    /**
     * 获取配置服务器端口
     * 
     * @return
     */
    public static int getConfigHostPort() {
        return NumberUtils.toInt(AriesConfig.get(AriesEnum.DEFAULT_CONFIG_PORT), 13148);
    }

    /**
     * 获取配置中心的模式
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
