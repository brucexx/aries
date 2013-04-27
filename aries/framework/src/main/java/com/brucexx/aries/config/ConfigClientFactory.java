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
 * ���ÿͻ��� ��
 * 1.ͨ��configCenter�ҵ���Ӧ��conifg���������õ�configServer
 * 2.ֱ������configServer
 * 
 * ͨ��configCenter·�ɵķ�ʽ�ȽϺ�һЩ������Ҫָ�����configServer
 * 
 * @author zhao.xiong
 * @version $Id: ConfigServerRegClient.java, v 0.1 2013-4-19 ����4:45:08 zhao.xiong Exp $
 */
public class ConfigClientFactory {

    private static Logger      logger = Logger.getLogger("aries-config");

    /** configCenter ����ֻ�ö����� ,ͨ����ע�����Ļ�ȡ���÷������ĵ�ַ��**/
    private static HATcpClient centerClient;

    /** ����ÿ̨��������������÷��������������� **/
    private static HATcpClient configClient;

    /**
     * ͨ��ע������centerServer��Ѱ�ҵ�ֱʵ��ConfigServer,ÿ̨ҵ�������configServer����һ������
     */
    private static void connServerByCenter() {

        centerClient = new HATcpClient(AriesConfigUtil.getCenterAddrPool(),
            CommonConstants.CENTER_SERVER_DESC);

        //ϵͳ�ڼ���ʱ��ע�����Ļ�ȡ���÷������ĵ�ַ
        SyncPackageResult result = centerClient.retryOnceSend(3, new NodeInfoSendPackage(),
            CommonConstants.TIME_OUT);
        if (result != null) {
            NodeInfoRespPackage resp = (NodeInfoRespPackage) result.getRecvPackage();
            if (resp.isSuccess()) {
                StringBuilder sb = new StringBuilder();
                sb.append("��center������" + centerClient.getCurAddr() + "��ȡconfigServer�б�>>>"
                          + MsgUtil.LINE_END);
                Set<String> set = resp.getConfServerSet();
                Set<InetSocketAddress> addrSet = new HashSet<InetSocketAddress>();
                for (String ip : set) {
                    addrSet.add(new InetSocketAddress(ip, AriesConfigUtil.getConfigHostPort()));
                    sb.append(ip).append(MsgUtil.LINE_END);
                }
                logger.info(sb.toString());

                //��center��ȡ��configServerSet��ע��
                directConnectServer(addrSet);

            } else {
                logger.error("center�������쳣" + resp.getErrorMsg(), resp.getException());
            }

        } else {
            logger.info("��centerע������3��ʧ�ܣ�����ֱ��ʹ��DIRECTģʽ����configServer");
            directConnectServer(AriesConfigUtil.getConfigAddrPool());
        }
    }

    /**
     * ֱ�����ӵ����÷���������ȺСʱ���������ַ�ʽ
     */
    private static void directConnectServer(Set<InetSocketAddress> configServerSet) {
        logger.info("�����÷������б�[" + configServerSet + "]ע��");
        configClient = new HATcpClient(configServerSet, CommonConstants.CONFIG_SERVER_DESC);
        configClient.heartBeat();
        if (configClient.hasAddrSurvive()) {
            logger.info("��config" + configClient.getCurAddr() + "���ӳɹ��� ");
        }

        Timer timer = new Timer(true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //�˴�
                configClient.heartBeat();
            }
        }, 0, 30000);

    }

    /**
     * ��ȡ���÷�����configClient
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
     * ע��regService��Ϣ
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
     * ע��reference Info��Ϣ
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
