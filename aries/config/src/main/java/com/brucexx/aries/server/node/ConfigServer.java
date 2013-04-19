/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.brucexx.aries.server.node;

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

import com.brucexx.aries.communication.pkg.common.HeartBeatPackage;
import com.brucexx.aries.communication.pkg.soa.ProtocolSendPackage;
import com.brucexx.aries.context.ConfigContext;
import com.brucexx.aries.data.ConfigDataRepository;
import com.brucexx.aries.data.ServiceInfo;
import com.brucexx.aries.protocol.RecvConfigPackage;

/**
 * 配置中心server，用于给客户端返回注册信息以及保存service的机器信息，
 * 一旦和
 * @author zhao.xiong
 * @version $Id: ConfigServer.java, v 0.1 2013-4-9 下午4:09:53 zhao.xiong Exp $
 */
@Component("configServer")
public class ConfigServer {

    private static Logger            logger = Logger.getLogger("config-server");

    /**配置中心数据 **/
    private ConfigDataRepository     configDataRepository;

    /** 用于服务业务系统**/
    private static NioSocketAcceptor acceptor;

    /**配置中心注册客户端 **/
    private static CenterRegClient   centerRegClient;

    /**
     * 
     */
    public void startConfigServer() {
        try {
            logger.info("开始启动配置服务器[" + ConfigContext.getConfigHostPort() + "]!");
            acceptor = new NioSocketAcceptor();

            SocketSessionConfig config = acceptor.getSessionConfig();
            config.setReuseAddress(true);

            ProtocolCodecFilter filter = new ProtocolCodecFilter(
                new ObjectSerializationCodecFactory());
            acceptor.getFilterChain().addLast("codec", filter);
            acceptor.setHandler(new IoHandlerAdapter() {
                public void sessionCreated(final IoSession session) {
                    logger.info(session + "连接上配置服务嘎啦!");
                }

                public void sessionClosed(IoSession session) throws Exception {
                    logger.info("session" + session + "断开");
                }

                public void messageSent(IoSession session, Object msg) throws Exception {
                    logger.info("session:" + session + "响应消息:" + msg);
                }

                public void messageReceived(IoSession session, Object arg1) throws Exception {
                    logger.info("接收到指令消息[" + arg1 + "]");
                    if (arg1 instanceof ProtocolSendPackage) {
                        commandProcess(session, (ProtocolSendPackage) arg1);
                    } else if (arg1 instanceof HeartBeatPackage) {
                        heartBeatResponse(session);
                    }
                }

                public void exceptionCaught(IoSession session, Throwable arg1) throws Exception {
                    logger.error("session:" + session + "出现异常", arg1);
                    session.close(true);
                }
            });

            acceptor.bind(new InetSocketAddress(ConfigContext.getConfigHostPort()));
            logger.info("配置服务器启动" + ConfigContext.getConfigHostPort() + "成功!");
            //向配置中心注册
            centerRegClient = new CenterRegClient();
            centerRegClient.startHeartBeat();
            centerRegClient.regConfigCenter();

        } catch (IOException e) {
            logger.error("绑定" + ConfigContext.getConfigHostPort() + "异常，无法启动", e);
        }
    }

    /**
     * 
     * @param session
     * @param arg1
     */
    protected void commandProcess(IoSession session, ProtocolSendPackage sendPackage) {
        //如果是service的话注册资源，否则返回已注册并且是存活状态的service列表
        RecvConfigPackage rp = new RecvConfigPackage();
        rp.setResourceId(sendPackage.getResourceId());
        rp.setGroupId(sendPackage.getGroupId());
        rp.setProtocol(sendPackage.getProtocol());
        rp.setMsgId(sendPackage.getMsgId());
        rp.setService(sendPackage.isService());

        if (sendPackage.isService()) {
            try {
                logger.info(session + "注册service["
                            + ToStringBuilder.reflectionToString(sendPackage) + "]");
                //注册service资源
                ServiceInfo serviceInfo = new ServiceInfo();
                serviceInfo.setGroupId(sendPackage.getGroupId());
                serviceInfo.setHostIp(session.getRemoteAddress().toString());
                serviceInfo.setProtocol(sendPackage.getProtocol());
                serviceInfo.setResourceId(sendPackage.getResourceId());

                configDataRepository.regService(serviceInfo);
                logger.info(session + "[" + sendPackage + "]注册service成功！");
                //返回注册成功的消息
                rp.setSuccess(true);
            } catch (Exception e) {
                logger.error(session + "[" + sendPackage + "]注册service异常！", e);
                rp.setServerException(e);
            } finally {
                session.write(rp);
            }

        } else {
            logger.info(session + "查询ref[" + sendPackage + "]");
            //ref的话获取列表
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
                             + "]获取serviceList异常！", e);
                rp.setServerException(e);
            } finally {
                session.write(rp);
                logger.info(session + "响应service列表:" + nodeInfoList);
            }
        }
    }

    /**
     * 
     * @param session
     */
    protected void heartBeatResponse(IoSession session) {
        session.write(new HeartBeatPackage());
    }

    public void setConfigDataRepository(ConfigDataRepository configDataRepository) {
        this.configDataRepository = configDataRepository;
    }

}
