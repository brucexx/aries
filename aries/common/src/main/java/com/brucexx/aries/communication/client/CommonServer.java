/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.brucexx.aries.communication.client;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.log4j.Logger;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import com.brucexx.aries.communication.pkg.common.HeartBeatPackage;
import com.brucexx.aries.communication.pkg.common.RecvPackage;
import com.brucexx.aries.communication.pkg.common.SendPackage;
import com.brucexx.aries.communication.pkg.soa.ProtocolSendPackage;

/**
 * 服务器
 * @author zhao.xiong
 * @version $Id: CommonServer.java, v 0.1 2013-4-24 下午2:12:31 zhao.xiong Exp $
 */
public class CommonServer {

    protected Logger                 logger = Logger.getLogger("aries-common");

    /** 用于服务业务系统**/
    private static NioSocketAcceptor acceptor;

    public void startServer(int port, final String serverDesc) {

        try {
            logger.info("开始启动" + serverDesc + "[" + port + "]!");
            acceptor = new NioSocketAcceptor();

            SocketSessionConfig config = acceptor.getSessionConfig();
            config.setReuseAddress(true);

            ProtocolCodecFilter filter = new ProtocolCodecFilter(
                new ObjectSerializationCodecFactory());
            acceptor.getFilterChain().addLast("codec", filter);
            acceptor.setHandler(new IoHandlerAdapter() {
                public void sessionCreated(final IoSession session) {
                    logger.info(serverDesc + ":" + session + "连接上" + serverDesc + "!");
                }

                public void sessionClosed(IoSession session) throws Exception {
                    logger.info(serverDesc + "：session" + session + "断开");
                }

                public void messageSent(IoSession session, Object msg) throws Exception {
                    logger.info(serverDesc + "：session:" + session + "响应消息:" + msg);
                }

                public void messageReceived(IoSession session, Object arg1) throws Exception {
                    logger.info(serverDesc + "接收到指令消息[" + arg1 + "]");
                    if (arg1 instanceof ProtocolSendPackage) {
                        processRecv(session, (ProtocolSendPackage) arg1);
                    } else if (arg1 instanceof HeartBeatPackage) {
                        heartBeatResponse(session);
                    }
                }

                public void exceptionCaught(IoSession session, Throwable arg1) throws Exception {
                    logger.error(serverDesc + ":session" + session + "出现异常", arg1);
                    session.close(true);
                }
            });

            acceptor.bind(new InetSocketAddress(port));
            logger.info(serverDesc + "启动" + port + "成功!");

        } catch (IOException e) {
            logger.error(serverDesc + "绑定" + port + "异常，无法启动", e);
        }
    }

    public int getConnectedCount() {
        return acceptor.getManagedSessionCount();
    }

    /**
     * 写入
     * 
     * @param session
     * @param recvPackage
     */
    public void writeResp(IoSession session, RecvPackage recvPackage) {
        WriteFuture wf = session.write(recvPackage);
        if (wf.isWritten()) {
            recvPackage.setSuccess(true);
        } else {
            recvPackage.setException(wf.getException());
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
     * 处理接收信息
     * 
     * @param session
     * @param sendPackage
     */
    protected void processRecv(IoSession session, SendPackage sendPackage) {
        //subclass go
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

}
