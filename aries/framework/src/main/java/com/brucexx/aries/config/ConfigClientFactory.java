/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.brucexx.aries.config;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.brucexx.aries.communication.client.HATcpClient;
import com.brucexx.aries.communication.pkg.common.SyncPackageResult;
import com.brucexx.aries.communication.pkg.conf.NodeInfoRespPackage;
import com.brucexx.aries.communication.pkg.conf.NodeInfoSendPackage;
import com.brucexx.aries.communication.pkg.soa.ProtocolSendPackage;
import com.brucexx.aries.enums.ConfigMode;
import com.brucexx.aries.exception.AriesException;
import com.brucexx.aries.exception.ResultCode;
import com.brucexx.aries.protocol.CommonConstants;
import com.brucexx.aries.tag.model.ReferenceModel;
import com.brucexx.aries.tag.model.ServiceModel;
import com.brucexx.aries.util.MsgUtil;

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
public class ConfigClientFactory {

    private static Logger      logger = Logger.getLogger("aries-config");

    /** configCenter 这里只用短连接 ,通过向注册中心获取配置服务器的地址后**/
    private static HATcpClient centerClient;

    /** 这里每台机器启动会和配置服务器建立长连接 **/
    private static HATcpClient configClient;

    /**
     * 通过注册中心centerServer来寻找到直实的ConfigServer,每台业务机器和configServer保持一个连接
     */
    private static void connServerByCenter() {

        centerClient = new HATcpClient(AriesConfigUtil.getCenterAddrPool(),
            CommonConstants.CENTER_SERVER_DESC);

        //系统在加载时向注册中心获取配置服务器的地址
        SyncPackageResult result = centerClient.retryOnceSend(3, new NodeInfoSendPackage(),
            CommonConstants.TIME_OUT);
        if (result != null) {
            NodeInfoRespPackage resp = (NodeInfoRespPackage) result.getRecvPackage();
            if (resp.isSuccess()) {
                StringBuilder sb = new StringBuilder();
                sb.append("向center服务器" + centerClient.getCurAddr() + "获取configServer列表>>>"
                          + MsgUtil.LINE_END);
                Set<String> set = resp.getConfServerSet();
                Set<InetSocketAddress> addrSet = new HashSet<InetSocketAddress>();
                for (String ip : set) {
                    addrSet.add(new InetSocketAddress(ip, AriesConfigUtil.getConfigHostPort()));
                    sb.append(ip).append(MsgUtil.LINE_END);
                }
                logger.info(sb.toString());

                //从center获取的configServerSet来注册
                directConnectServer(addrSet);

            } else {
                logger.error("center服务器异常" + resp.getErrorMsg(), resp.getException());
            }

        } else {
            logger.info("向center注册请求3次失败！尝试直接使用DIRECT模式连接configServer");
            directConnectServer(AriesConfigUtil.getConfigAddrPool());
        }
    }

    /**
     * 直接连接到配置服务器，集群小时可用于这种方式
     */
    private static void directConnectServer(Set<InetSocketAddress> configServerSet) {
        logger.info("向配置服务器列表[" + configServerSet + "]注册");
        configClient = new HATcpClient(configServerSet, CommonConstants.CONFIG_SERVER_DESC);
        configClient.heartBeat();
        if (configClient.hasAddrSurvive()) {
            logger.info("向config" + configClient.getCurAddr() + "连接成功！ ");
        }

        Timer timer = new Timer(true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //此处
                configClient.heartBeat();
            }
        }, 0, 30000);

    }

    /**
     * 获取配置服务器configClient
     * 
     * @return
     */

    public static HATcpClient getConfigClient() {
        if (configClient == null) {
            if (AriesConfigUtil.getConfigMode() == ConfigMode.DIRECT) {
                directConnectServer(AriesConfigUtil.getConfigAddrPool());
            } else {
                connServerByCenter();
            }
        }
        return configClient;
    }

    /**
     * 注册regService信息
     * 
     * @param sm
     */
    public static void regServiceInfo(ServiceModel sm) {
        if (sm != null) {
            ProtocolSendPackage psp = new ProtocolSendPackage();
            psp.setGroupId(sm.getGroupId());
            psp.setMsgId(MsgUtil.genMsgId());
            psp.setProtocol(sm.getProtocol());
            psp.setResourceId(AriesConfigUtil.getResourceId(sm));
            psp.setService(true);
            getConfigClient().retrySendAndRecv(psp);
        }
    }

    /**
     * 注册reference Info信息
     * 
     * @param rm
     */
    public static void regRefInfo(ReferenceModel rm) {
        if (rm != null) {
            ProtocolSendPackage psp = new ProtocolSendPackage();
            psp.setGroupId(rm.getGroupId());
            psp.setMsgId(MsgUtil.genMsgId());
            psp.setProtocol(rm.getProtocol());
            psp.setResourceId(AriesConfigUtil.getResourceId(rm));
            getConfigClient().retrySendAndRecv(psp);
        }
    }

    /**
     * 
     * 
     * @return
     */
    public static HATcpClient getCenterClient() {
        if (centerClient == null) {
            if (AriesConfigUtil.getConfigMode() == ConfigMode.CENTER) {
                connServerByCenter();
            } else {
                throw new AriesException(ResultCode.ILLEGAL_CONFIG_MODE);
            }
        }
        return centerClient;
    }

}
