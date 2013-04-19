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
 * @version $Id: TcpServer.java, v 0.1 2013-4-19 上午9:55:03 zhao.xiong Exp $
 */
public class TcpServer {

    private static Logger            logger = Logger.getLogger("common-transfer");

    /** 用于服务业务系统**/
    private static NioSocketAcceptor acceptor;

    /**
     * 
     */
    public void startserver(int port) {
        try {
            logger.info("开始启动服务器[" + port + "]!");
            acceptor = new NioSocketAcceptor();

            SocketSessionConfig config = acceptor.getSessionConfig();
            config.setReuseAddress(true);

            ProtocolCodecFilter filter = new ProtocolCodecFilter(
                new ObjectSerializationCodecFactory());
            acceptor.getFilterChain().addLast("codec", filter);
            acceptor.setHandler(new IoHandlerAdapter() {
                public void sessionCreated(final IoSession session) {
                    logger.info(session + "连接上服务器!");
                }

                public void sessionClosed(IoSession session) throws Exception {
                    logger.info("session" + session + "断开");
                }

                public void messageSent(IoSession session, Object msg) throws Exception {
                    logger.info("session:" + session + "响应消息:" + msg);
                }

                public void messageReceived(IoSession session, Object arg1) throws Exception {
                    logger.info("接收到指令消息[" + arg1 + "]");
                    if (arg1 instanceof SendPackage) {
                        commandProcess(session, (SendPackage) arg1);
                    } else if (arg1 instanceof HeartBeatPackage) {
                        heartBeatResponse(session);
                    }
                }

                public void exceptionCaught(IoSession session, Throwable arg1) throws Exception {
                    logger.error("session:" + session + "出现异常", arg1);
                    session.close(true);
                }
            });

            acceptor.bind(new InetSocketAddress(port));
            logger.info("服务器启动" + port + "成功!");
        } catch (IOException e) {
            logger.error("绑定" + port + "异常，无法启动", e);
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
