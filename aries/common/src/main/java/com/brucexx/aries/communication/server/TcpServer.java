/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.brucexx.aries.communication.server;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import com.brucexx.aries.communication.pkg.common.HeartBeatPackage;
import com.brucexx.aries.communication.pkg.common.SendPackage;

/**
 * 
 * @author zhao.xiong
 * @version $Id: TcpServer.java, v 0.1 2013-4-19 ����9:55:03 zhao.xiong Exp $
 */
public class TcpServer {

    private static Logger            logger = Logger.getLogger("common-transfer");

    /** ���ڷ���ҵ��ϵͳ**/
    private static NioSocketAcceptor acceptor;

    /**
     * 
     */
    public void startserver(int port) {
        try {
            logger.info("��ʼ����������[" + port + "]!");
            acceptor = new NioSocketAcceptor();

            SocketSessionConfig config = acceptor.getSessionConfig();
            config.setReuseAddress(true);

            ProtocolCodecFilter filter = new ProtocolCodecFilter(
                new ObjectSerializationCodecFactory());
            acceptor.getFilterChain().addLast("codec", filter);
            acceptor.setHandler(new IoHandlerAdapter() {
                public void sessionCreated(final IoSession session) {
                    logger.info(session + "�����Ϸ�����!");
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

            acceptor.bind(new InetSocketAddress(port));
            logger.info("����������" + port + "�ɹ�!");
        } catch (IOException e) {
            logger.error("��" + port + "�쳣���޷�����", e);
        }

    }

    /**
     * 
     * @param session
     * @param arg1
     */
    protected void commandProcess(IoSession session, SendPackage arg1) {
        //for subclass do something
    }

    /**
     * 
     * @param session
     */
    protected void heartBeatResponse(IoSession session) {

        session.write(new HeartBeatPackage());
    }

}
