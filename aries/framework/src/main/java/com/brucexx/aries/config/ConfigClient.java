/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.brucexx.aries.config;

import java.net.InetSocketAddress;
import java.util.HashSet;

import com.brucexx.aries.communication.client.HATcpClient;
import com.brucexx.aries.protocol.CommonConstants;

/**
 * 配置客户端 ，
 * 1.通过configCenter找到对应的conifg来给出可用的configServer
 * 2.直接配置configServer
 * 
 * 通过configCenter路由的方式比较好一些，不需要指定多个configServer
 * 
 * @author zhao.xiong
 * @version $Id: ConfigServerRegClient.java, v 0.1 2013-4-19 下午4:45:08 zhao.xiong Exp $
 */
public class ConfigClient {

    /** configCenter 这里只用短连接 **/
    private HATcpClient centerClient;

    /** 这里每台机器启动会和配置服务器建立长连接 **/
    private HATcpClient configClient;

    public void regToConfigServer() {
        configClient = new HATcpClient(new HashSet<InetSocketAddress>(),
            CommonConstants.CONFIG_SERVER_DESC);

        centerClient = new HATcpClient(new HashSet<InetSocketAddress>(),
            CommonConstants.CENTER_SERVER_DESC);
    }

}
