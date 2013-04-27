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
 * configServer中心，主要用于cofigServer的水平扩展，向 configServerManager注册节点信息后，通过
 * @author zhao.xiong
 * @version $Id: ConfigServerManager.java, v 0.1 2013-4-11 上午11:21:58 zhao.xiong Exp $
 */
@Component("configCenterServer")
public class ConfigCenterServer {
    private static Logger logger          = Logger.getLogger("config-center");

    /**注册进来的configServer的机器ip列表  **/
    private Set<String>   configServerSet = new HashSet<String>();

    public void addOrUpdateConfigServer(String configServerIp) {
        configServerSet.add(configServerIp);
    }

    public void removeConfigServer(String configServerIp) {
        configServerSet.remove(configServerIp);
    }

    /** 用于服务业务系统**/
    private static NioSocketAcceptor acceptor;

    /**
     * 
     */
    public void startCenterServer() {
        try {
            logger.info("开始启动配置中心[" + ConfigContext.getCenterHostPort() + "]!");
            acceptor = new NioSocketAcceptor();

            SocketSessionConfig config = acceptor.getSessionConfig();
            config.setReuseAddress(true);

            ProtocolCodecFilter filter = new ProtocolCodecFilter(
                new ObjectSerializationCodecFactory());
            acceptor.getFilterChain().addLast("codec", filter);
            acceptor.setHandler(new IoHandlerAdapter() {
                public void sessionCreated(final IoSession session) {
                    logger.info(session + "连接上配置中心!");
                }

                public void sessionClosed(IoSession session) throws Exception {
                    logger.info("session" + session + "断开");
                }

                public void messageSent(IoSession session, Object msg) throws Exception {
                    logger.info("session:" + session + "响应消息:" + msg);
                }

                public void messageReceived(IoSession session, Object arg1) throws Exception {
                    logger.info("接收到指令消息[" + arg1 + "]");
                    if (arg1 instanceof RegSendPackage) {
                        commandProcess(session, (RegSendPackage) arg1);
                    } else if (arg1 instanceof HeartBeatPackage) {
                        heartBeatResponse(session);
                    }
                }

                public void exceptionCaught(IoSession session, Throwable arg1) throws Exception {
                    logger.error("session:" + session + "出现异常", arg1);
                    session.close(true);
                }
            });

            acceptor.bind(new InetSocketAddress(ConfigContext.getCenterHostPort()));
            logger.info("配置中心启动" + ConfigContext.getCenterHostPort() + "成功!");

        } catch (IOException e) {
            logger.error("绑定" + ConfigContext.getCenterHostPort() + "异常，无法启动", e);
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
                logger.warn(session + "的config MAC为空");
                rpp.setErrorMsg("config MAC为空");
                return;
            }
            rpp.setConfigServiceId(regPackage.getConfigServiceId());
            rpp.setMsgId(regPackage.getMsgId());
            rpp.setSuccess(true);

        } catch (Exception e) {
            logger.error("注册异常", e);
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
