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
 * 配置中心server，用于给客户端返回注册信息以及保存service的机器信息，
 * 一旦和
 * @author zhao.xiong
 * @version $Id: ConfigServer.java, v 0.1 2013-4-9 下午4:09:53 zhao.xiong Exp $
 */
@Component("configServer")
public class ConfigServer {

    private static Logger            logger            = Logger.getLogger("aries-config");

    /** 工作服务端口，由configServer监听 **/
    private static final int         WORK_SESSION_PORT = 13148;

    /**配置中心数据 **/
    private ConfigDataRepository     configDataRepository;

    /** 用于服务业务系统**/
    private static NioSocketAcceptor acceptor;

    /** **/
    private static ManagerClient      serverClient;

    /**
     * 
     */
    public void startConfigServer() {
        try {
            logger.info("开始启动配置中心服务器[" + WORK_SESSION_PORT + "]!");
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

            acceptor.bind(new InetSocketAddress(WORK_SESSION_PORT));
            logger.info("配置中心服务器启动" + WORK_SESSION_PORT + "成功!");
        } catch (IOException e) {
            logger.error("绑定" + WORK_SESSION_PORT + "异常，无法启动", e);
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
        RecvPackage rp = new RecvPackage();
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
                logger.info(session + "[" + ToStringBuilder.reflectionToString(sendPackage)
                            + "]注册service成功！");
                //返回注册成功的消息
                rp.setSuccess(true);
            } catch (Exception e) {
                logger.error(session + "[" + ToStringBuilder.reflectionToString(sendPackage)
                             + "]注册service异常！", e);
                rp.setServerException(e);
            }
            session.write(rp);

        } else {
            logger.info(session + "查询ref[" + ToStringBuilder.reflectionToString(sendPackage) + "]");
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
                session.write(nodeInfoList);
                logger.info(session + "响应service列表:" + nodeInfoList);
            }

        }
    }

    public void setConfigDataRepository(ConfigDataRepository configDataRepository) {
        this.configDataRepository = configDataRepository;
    }

}
