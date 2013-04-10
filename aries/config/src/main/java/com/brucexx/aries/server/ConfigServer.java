/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.brucexx.aries.server;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import com.brucexx.aries.data.ConfigDataRepository;
import com.brucexx.aries.data.ServiceInfo;
import com.brucexx.aries.protocol.HeartBeatPackage;
import com.brucexx.aries.protocol.SendPackage;

/**
 * 配置中心server，用于给客户端返回注册信息
 * @author zhao.xiong
 * @version $Id: ConfigServer.java, v 0.1 2013-4-9 下午4:09:53 zhao.xiong Exp $
 */
@Component("configServer")
public class ConfigServer implements InitializingBean {

    private static Logger            logger       = Logger.getLogger("aries-config");

    private static final int         SESSION_PORT = 13148;

    /**配置中心数据 **/
    private ConfigDataRepository     configDataRepository;

    private static NioSocketAcceptor acceptor;

    public void startConfigServer() {
        try {
            logger.info("开始启动配置中心服务器[" + SESSION_PORT + "]!");
            acceptor = new NioSocketAcceptor();

            SocketSessionConfig config = acceptor.getSessionConfig();
            config.setReuseAddress(true);

            ProtocolCodecFilter filter = new ProtocolCodecFilter(
                new ObjectSerializationCodecFactory());
            acceptor.getFilterChain().addLast("codec", filter);
            acceptor.setHandler(new IoHandlerAdapter() {
                public void sessionCreated(final IoSession session) {
                    session.getConfig().setReadBufferSize(2048);
                    logger.info(session + "连接上服务器!");
                }

                public void sessionClosed(IoSession session) throws Exception {
                    logger.info("session" + session + "关闭");
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

            acceptor.bind(new InetSocketAddress(SESSION_PORT));
            logger.info("配置中心服务器启动" + SESSION_PORT + "成功!");
        } catch (IOException e) {
            logger.error("绑定" + SESSION_PORT + "异常，无法启动", e);
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
        //如果是service的话注册资源，否则返回已注册并且是存活状态的service列表
        if (sendPackage.isService()) {
            //注册service资源
            ServiceInfo serviceInfo = new ServiceInfo();
            serviceInfo.setGroupId(sendPackage.getGroupId());
            serviceInfo.setHostIp(session.getRemoteAddress().toString());
            serviceInfo.setProtocol(sendPackage.getProtocol());
            serviceInfo.setResourceId(sendPackage.getResourceId());

            configDataRepository.regService(serviceInfo);

        } else {
            //ref的话获取列表
            session.write(configDataRepository.getServiceNodeList(sendPackage.getGroupId()));
        }
    }

    /** 
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        this.startConfigServer();
    }

    public void setConfigDataRepository(ConfigDataRepository configDataRepository) {
        this.configDataRepository = configDataRepository;
    }

}
