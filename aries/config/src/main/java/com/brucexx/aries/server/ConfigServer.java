/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.brucexx.aries.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.springframework.stereotype.Component;

import com.brucexx.aries.data.ConfigDataRepository;
import com.brucexx.aries.data.ServiceInfo;
import com.brucexx.aries.protocol.HeartBeatPackage;
import com.brucexx.aries.protocol.RecvPackage;
import com.brucexx.aries.protocol.SendPackage;

/**
 * ��������server�����ڸ��ͻ��˷���ע����Ϣ�Լ�����service�Ļ�����Ϣ��
 * һ����
 * @author zhao.xiong
 * @version $Id: ConfigServer.java, v 0.1 2013-4-9 ����4:09:53 zhao.xiong Exp $
 */
@Component("configServer")
public class ConfigServer {

    private static Logger            logger            = Logger.getLogger("aries-config");

    /** ��������˿ڣ���configServer���� **/
    private static final int         WORK_SESSION_PORT = 13148;

    /**������������ **/
    private ConfigDataRepository     configDataRepository;

    /** ���ڷ���ҵ��ϵͳ**/
    private static NioSocketAcceptor acceptor;

    /** **/
    private static ManagerClient      serverClient;

    /**
     * 
     */
    public void startConfigServer() {
        try {
            logger.info("��ʼ�����������ķ�����[" + WORK_SESSION_PORT + "]!");
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
                    if (arg1 instanceof SendPackage) {
                        commandProcess(session, (SendPackage) arg1);
                    } else if (arg1 instanceof HeartBeatPackage) {
                        heartBeatResponse(session);
                    }
                }

                public void exceptionCaught(IoSession session, Throwable arg1) throws Exception {
                    logger.error("session:" + session + "�����쳣", arg1);
                    session.close(true);
                }
            });

            acceptor.bind(new InetSocketAddress(WORK_SESSION_PORT));
            logger.info("�������ķ���������" + WORK_SESSION_PORT + "�ɹ�!");
        } catch (IOException e) {
            logger.error("��" + WORK_SESSION_PORT + "�쳣���޷�����", e);
        }

    }

    /**
     * 
     * @param session
     */
    protected void heartBeatResponse(IoSession session) {

        session.write(new HeartBeatPackage());
    }

    /**
     * 
     * @param session
     * @param arg1
     */
    protected void commandProcess(IoSession session, SendPackage sendPackage) {
        //�����service�Ļ�ע����Դ�����򷵻���ע�Ტ���Ǵ��״̬��service�б�
        RecvPackage rp = new RecvPackage();
        rp.setResourceId(sendPackage.getResourceId());
        rp.setGroupId(sendPackage.getGroupId());
        rp.setProtocol(sendPackage.getProtocol());
        rp.setMsgId(sendPackage.getMsgId());
        rp.setService(sendPackage.isService());

        if (sendPackage.isService()) {
            try {
                logger.info(session + "ע��service["
                            + ToStringBuilder.reflectionToString(sendPackage) + "]");
                //ע��service��Դ
                ServiceInfo serviceInfo = new ServiceInfo();
                serviceInfo.setGroupId(sendPackage.getGroupId());
                serviceInfo.setHostIp(session.getRemoteAddress().toString());
                serviceInfo.setProtocol(sendPackage.getProtocol());
                serviceInfo.setResourceId(sendPackage.getResourceId());
                configDataRepository.regService(serviceInfo);
                logger.info(session + "[" + ToStringBuilder.reflectionToString(sendPackage)
                            + "]ע��service�ɹ���");
                //����ע��ɹ�����Ϣ
                rp.setSuccess(true);
            } catch (Exception e) {
                logger.error(session + "[" + ToStringBuilder.reflectionToString(sendPackage)
                             + "]ע��service�쳣��", e);
                rp.setServerException(e);
            }
            session.write(rp);

        } else {
            logger.info(session + "��ѯref[" + ToStringBuilder.reflectionToString(sendPackage) + "]");
            //ref�Ļ���ȡ�б�
            List<String> nodeInfoList = new ArrayList<String>();
            try {
                configDataRepository.getServiceNodeList(sendPackage.getGroupId());

                List<ServiceInfo> hostList = configDataRepository.getServiceNodeList(sendPackage
                    .getGroupId());
                for (ServiceInfo serviceInfo : hostList) {
                    nodeInfoList.add(serviceInfo.getHostIp());
                }
                rp.setSuccess(true);
                rp.setNodeInfoList(nodeInfoList);
            } catch (Exception e) {
                logger.error(session + "[" + ToStringBuilder.reflectionToString(sendPackage)
                             + "]��ȡserviceList�쳣��", e);
                rp.setServerException(e);
            } finally {
                session.write(nodeInfoList);
                logger.info(session + "��Ӧservice�б�:" + nodeInfoList);
            }

        }
    }

    public void setConfigDataRepository(ConfigDataRepository configDataRepository) {
        this.configDataRepository = configDataRepository;
    }

}
