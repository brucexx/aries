/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.brucexx.aries.server.center;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.brucexx.aries.exception.Assert;
import com.brucexx.aries.exception.ResultCode;
import com.brucexx.aries.protocol.HeartBeatPackage;
import com.brucexx.aries.protocol.MsgPackage;
import com.brucexx.aries.protocol.NodeRegPackage;
import com.brucexx.aries.protocol.RecvPackage;
import com.brucexx.aries.server.ConfigServerEnum;
import com.brucexx.aries.util.MsgUtil;
import com.brucexx.aries.util.SystemUtil;

/**
 * 这里保持一个连接就好了
 * @author zhao.xiong
 * @version $Id: ConfigServerManagerClient.java, v 0.1 2013-4-12 下午1:24:41 zhao.xiong Exp $
 */
public class ManagerClient {

    /**configServer管理器端口， 由configServerManager监听,由configServer启动时向configServerManager注册信息 **/
    private static final int              CONFIG_MANAGER_PORT = 13147;

    private static Logger                 logger              = Logger.getLogger("aries-config");

    /** configserver配置属性**/
    private Map<ConfigServerEnum, String> map                 = new HashMap<ConfigServerEnum, String>();

    /** 消息发送缓存map，用于异步转同步**/
    private final Map<String, MsgPackage> msgSendMap          = new HashMap<String, MsgPackage>();

    /** 消息接受缓存map，用于异步转同步**/
    private final Map<String, MsgPackage> msgRecvMap          = new ConcurrentHashMap<String, MsgPackage>();

    /** 用于连接configManager进行配置 **/
    private NioSocketConnector            connector;

    /** 只保持一个连接 **/
    private IoSession                     curSession;

    /** 心跳任务初始化**/
    private boolean                       init                = false;

    /**管理器中心ip **/
    private String                        managerIp;

    public void startHeartBeat() {
        if (!init) {
            Timer timer = new Timer(true);
            timer.schedule(new TimerTask() {

                @Override
                public void run() {
                    //此处
                    IoSession session;
                    try {
                        session = getCurSession();
                        session.write(new HeartBeatPackage());
                        logger.info("心跳维持发送--->");
                    } catch (InterruptedException e) {
                        logger.error("获取当前session超时", e);
                    }

                }
            }, 5000, 30000);
            init = true;
        }
    }

    public void regConfigCenter() {
        RecvPackage recv = sendAndRecv();
        if (!recv.isSuccess()) {
            logger.error("注册失败！");
        }
    }

    /**
     * 向配置中心注册配置服务器
     */
    private RecvPackage sendAndRecv() {
        NodeRegPackage regPackage = new NodeRegPackage();
        regPackage.setMsgId(MsgUtil.genMsgId());
        regPackage.setConfigServiceId(SystemUtil.getMAC());
        int i = 0;
        boolean isWriteOK = false;
        while (++i < 5) {
            try {
                IoSession session = getCurSession();
                WriteFuture wf = session.write(regPackage);
                if ((isWriteOK = (wf.isDone() && wf.isWritten()))) {
                    msgSendMap.put(regPackage.getMsgId(), regPackage);
                    break;
                }
            } catch (Exception e) {
                logger.error("注册配置服务中心写入异常", e);
            }
        }
        if (i == 5 && !isWriteOK) {
            logger.info(regPackage.getMsgId() + "注册失败！");
            return new RecvPackage();
        }

        try {
            i = 0;
            while (++i < 50) {
                Thread.sleep(200);
                MsgPackage recvPackage = null;
                if ((recvPackage = msgRecvMap.get(regPackage.getMsgId())) != null) {
                    RecvPackage rp = (RecvPackage) recvPackage;
                    if (rp.isSuccess()) {
                        msgSendMap.remove(regPackage.getMsgId());
                        logger.info("向配置服务器管理器[" + managerIp + "]服务注册配置节点成功!");
                        return rp;
                    }
                }
            }
            logger.warn("向配置服务器管理器[" + managerIp + "]服务注册配置节点失败!");

        } catch (Exception e) {
            logger.error("注册配置服务中心异常", e);
        } finally {
            msgSendMap.remove(regPackage.getMsgId());
        }
        return new RecvPackage();
    }

    /**
     * 
     * 
     * @param msgRecv
     */
    private void recvMsg(MsgPackage msgRecv) {
        try {
            if (msgSendMap.get(msgRecv.getMsgId()) != null) {
                msgRecvMap.put(msgRecv.getMsgId(), msgRecv);
            } else {
                logger.warn(msgRecv + "无法找到之前的发送包,不加入接收包缓存!");
            }

        } catch (Exception e) {
            logger.error("接收配置服务中心消息异常", e);
        }
    }

    /**
     * 连接管理器中心
     */
    public void connectManager() {
        try {

            if (StringUtils.isNotEmpty(map.get(ConfigServerEnum.MANAGER_HOST))) {
                managerIp = map.get(ConfigServerEnum.MANAGER_HOST);
            }
            Assert.notEmpty(managerIp, ResultCode.NOT_EMPTY);

            ProtocolCodecFilter filter = new ProtocolCodecFilter(
                new ObjectSerializationCodecFactory());
            connector = new NioSocketConnector();
            connector.getFilterChain().addLast("codec", filter);
            connector.setConnectTimeoutMillis(5000);
            connector.getSessionConfig().setWriteTimeout(5000);

            logger.info("客户端开始连接服务器!" + managerIp);

            connector.setHandler(new IoHandlerAdapter() {

                public void sessionCreated(IoSession session) throws Exception {
                    super.sessionCreated(session);
                }

                public void sessionClosed(IoSession session) throws Exception {
                    logger.info(session + "关闭" + MsgUtil.LINE_END);
                }

                public void messageSent(IoSession session, Object arg1) throws Exception {

                    logger.info("向" + session + "发送消息:" + arg1);
                }

                public void messageReceived(IoSession session, Object arg1) throws Exception {
                    logger.info("接收到" + session + "消息:" + arg1);
                    if (arg1 instanceof MsgPackage) {
                        recvMsg((MsgPackage) arg1);
                    } else {
                        //do nothing..心跳
                        logger.info(session + "心跳返回:" + arg1);
                    }

                }

                public void exceptionCaught(IoSession session, Throwable arg1) throws Exception {
                    logger.error(session + "客户端异常", arg1);
                    session.close(true);
                }
            });
            ConnectFuture future = connector.connect(new InetSocketAddress(managerIp,
                CONFIG_MANAGER_PORT));
            try {
                future.await();
                if (future.isConnected()) {
                    curSession = future.getSession();
                    logger.info(curSession + "连接上配置服务器管理中心!");
                    startHeartBeat();
                } else {
                    logger.error("无法连接上配置服务器管理中心!");
                }
            } catch (InterruptedException ex) {
                logger.error("连接配置服务器管理中心异常", ex);
            }
        } catch (Exception e) {
            logger.error("连接配置服务器管理中心异常", e);
        }
    }

    /**
     * 
     * 
     * @return
     * @throws InterruptedException
     */
    private synchronized IoSession getCurSession() throws InterruptedException {
        int i = 0;
        while (++i < 20 && (curSession == null || !curSession.isConnected())) {
            logger.info("正在重试连接第" + i + "次");
            connectManager();
            logger.info("重试连接第" + i + "次成功!");
        }
        if (i == 20) {
            logger.info("超过重试次数20次，10分钟后重新连接");
            Thread.sleep(600000);
            getCurSession();
        }
        return curSession;
    }
}
