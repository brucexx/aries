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
 * ���ÿͻ��� ��
 * 1.ͨ��configCenter�ҵ���Ӧ��conifg���������õ�configServer
 * 2.ֱ������configServer
 * 
 * ͨ��configCenter·�ɵķ�ʽ�ȽϺ�һЩ������Ҫָ�����configServer
 * 
 * @author zhao.xiong
 * @version $Id: ConfigServerRegClient.java, v 0.1 2013-4-19 ����4:45:08 zhao.xiong Exp $
 */
public class ConfigClient {

    /** configCenter ����ֻ�ö����� **/
    private HATcpClient centerClient;

    /** ����ÿ̨��������������÷��������������� **/
    private HATcpClient configClient;

    public void regToConfigServer() {
        configClient = new HATcpClient(new HashSet<InetSocketAddress>(),
            CommonConstants.CONFIG_SERVER_DESC);

        centerClient = new HATcpClient(new HashSet<InetSocketAddress>(),
            CommonConstants.CENTER_SERVER_DESC);
    }

}
