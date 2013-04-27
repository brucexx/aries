/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.brucexx.aries.server.center;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.springframework.stereotype.Component;

import com.brucexx.aries.communication.pkg.common.HeartBeatPackage;
import com.brucexx.aries.communication.pkg.conf.RegRespPackage;
import com.brucexx.aries.communication.pkg.conf.RegSendPackage;
import com.brucexx.aries.context.ConfigContext;

/**
 * configServer���ģ���Ҫ����cofigServer��ˮƽ��չ���� configServerManagerע��ڵ���Ϣ��ͨ��
 * @author zhao.xiong
 * @version $Id: ConfigServerManager.java, v 0.1 2013-4-11 ����11:21:58 zhao.xiong Exp $
 */
@Component("configCenterServer")
public class ConfigCenterServer {
    private static Logger logger          = Logger.getLogger("config-center");

    /**ע�������configServer�Ļ���ip�б�  **/
    private Set<String>   configServerSet = new HashSet<String>();

    public void addOrUpdateConfigServer(String configServerIp) {
        configServerSet.add(configServerIp);
    }

    public void removeConfigServer(String configServerIp) {
        configServerSet.remove(configServerIp);
    }

    /** ���ڷ���ҵ��ϵͳ**/
    private static NioSocketAcceptor acceptor;

    /**
     * 
     */
    public void startCenterServer() {
        try {
            logger.info("��ʼ������������[" + ConfigContext.getCenterHostPort() + "]!");
            acceptor = new NioSocketAcceptor();

            SocketSessionConfig config = acceptor.getSessionConfig();
            config.setReuseAddress(true);

            ProtocolCodecFilter filter = new ProtocolCodecFilter(
                new ObjectSerializationCodecFactory());
            acceptor.getFilterChain().addLast("codec", filter);
            acceptor.setHandler(new IoHandlerAdapter() {
                public void sessionCreated(final IoSession session) {
                    logger.info(session + "��������������!");
                }

                public void sessionClosed(IoSession session) throws Exception {
                    logger.info("session" + session + "�Ͽ�");
                }

                public void messageSent(IoSession session, Object msg) throws Exception {
                    logger.info("session:" + session + "��Ӧ��Ϣ:" + msg);
                }

                public void messageReceived(IoSession session, Object arg1) throws Exception {
                    logger.info("���յ�ָ����Ϣ[" + arg1 + "]");
                    if (arg1 instanceof RegSendPackage) {
                        commandProcess(session, (RegSendPackage) arg1);
                    } else if (arg1 instanceof HeartBeatPackage) {
                        heartBeatResponse(session);
                    }
                }

                public void exceptionCaught(IoSession session, Throwable arg1) throws Exception {
                    logger.error("session:" + session + "�����쳣", arg1);
                    session.close(true);
                }
            });

            acceptor.bind(new InetSocketAddress(ConfigContext.getCenterHostPort()));
            logger.info("������������" + ConfigContext.getCenterHostPort() + "�ɹ�!");

        } catch (IOException e) {
            logger.error("��" + ConfigContext.getCenterHostPort() + "�쳣���޷�����", e);
        }
    }

    /**
     * 
     * @param session
     * @param arg1
     */
    protected void commandProcess(IoSession session, RegSendPackage regPackage) {
        RegRespPackage rpp = new RegRespPackage();
        try {
            //1.check msg
            if (StringUtils.isEmpty(regPackage.getConfigServiceId())) {
                logger.warn(session + "��config MACΪ��");
                rpp.setErrorMsg("config MACΪ��");
                return;
            }
            rpp.setConfigServiceId(regPackage.getConfigServiceId());
            rpp.setMsgId(regPackage.getMsgId());
            rpp.setSuccess(true);

        } catch (Exception e) {
            logger.error("ע���쳣", e);
            rpp.setException(e);
        } finally {
            session.write(rpp);
        }

    }

    /**
     * 
     * @param session
     */
    protected void heartBeatResponse(IoSession session) {
        session.write(new HeartBeatPackage());
    }

}
