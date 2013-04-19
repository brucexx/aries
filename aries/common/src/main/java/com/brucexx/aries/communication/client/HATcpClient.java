/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.brucexx.aries.communication.client;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.Logger;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.brucexx.aries.communication.pkg.common.HeartBeatPackage;
import com.brucexx.aries.communication.pkg.common.MsgPackage;
import com.brucexx.aries.communication.pkg.common.RecvPackage;
import com.brucexx.aries.communication.pkg.common.SyncPackageResult;
import com.brucexx.aries.protocol.CommonConstants;
import com.brucexx.aries.util.LockUtil;
import com.brucexx.aries.util.MsgUtil;
import com.brucexx.aries.util.TaskUtil;

/**
 * 高可用tcp客户端,多个地址列表，用于切换
 * @author zhao.xiong
 * @version $Id: MinaClient.java, v 0.1 2013-4-18 上午11:07:38 zhao.xiong Exp $
 */
public class HATcpClient {

    private static Logger                   logger      = Logger.getLogger("common-transfer");

    /** 消息发送缓存map，用于异步转同步**/
    private final Map<String, MsgPackage>   msgSendMap  = new HashMap<String, MsgPackage>();

    /** 消息接受缓存map，用于异步转同步**/
    private final Map<String, RecvPackage>  msgRecvMap  = new ConcurrentHashMap<String, RecvPackage>();

    /** 用于连接configManager进行配置 **/
    private NioSocketConnector              connector;
    /** 只保持一个连接 **/
    private IoSession                       curSession;

    /** 可用的地址,从ipSet里迭代选出**/
    private InetSocketAddress               curAvaiableAddr;

    /**服务器信息描述 **/
    private String                          serverDesc  = CommonConstants.CONFIG_SERVER_DESC;

    /**HA中心ip列表 **/
    private Map<InetSocketAddress, Boolean> ipStatusMap = new TreeMap<InetSocketAddress, Boolean>();

    public HATcpClient(Set<InetSocketAddress> ipSet) {
        for (InetSocketAddress ipAddr : ipSet) {
            ipStatusMap.put(ipAddr, Boolean.TRUE);
        }
    }

    /**
     * 
     * @param ipSet
     * @param serverDesc 服务器名称描述
     */
    public HATcpClient(Set<InetSocketAddress> ipSet, String serverDesc) {
        this(ipSet);
        this.serverDesc = serverDesc;

    }

    public void heartBeat() {
        IoSession session = getCurSession();
        session.write(new HeartBeatPackage());
        logger.info("向[" + serverDesc + "]发送心跳--->");
    }

    public SyncPackageResult retryOnceSend(final int retryTimes, final MsgPackage sendPackage,
                                           long timeout) {
        int i = 0;
        SyncPackageResult synResult = null;
        while (i++ < retryTimes && (synResult = onceSendAndRecv(sendPackage, timeout)).isTimeout()) {
            logger.info("2S后重试");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                logger.error("", e);
            }
        }
        if (i == retryTimes) {
            logger.warn("重试" + retryTimes + "后失败!");
            return null;
        }
        return synResult;
    }

    /**
     * 重试发送
     * 
     * @param retryTimes
     * @param sendPackage
     * @param timeout
     * @return
     */
    public SyncPackageResult retrySend(final int retryTimes, final MsgPackage sendPackage,
                                       long timeout) {
        int i = 0;
        SyncPackageResult synResult = null;
        while (i++ < retryTimes && (synResult = sendAndRecv(sendPackage, timeout)).isTimeout()) {
            logger.info("2S后重试");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                logger.error("", e);
            }
        }
        if (i == retryTimes) {
            logger.warn("重试" + retryTimes + "后失败!");
            return null;
        }
        return synResult;
    }

    /**
     * 短连接
     * 
     * @param sendPackage
     * @param timeout
     * @return
     */
    public SyncPackageResult onceSendAndRecv(final MsgPackage sendPackage, long timeout) {
        try {
            return this.sendAndRecv(sendPackage, timeout);
        } finally {
            if (curSession != null) {
                this.curSession.close(true);
            }
        }
    }

    /**
     * 
     * 
     * @param sendPackage
     * @return
     */
    public SyncPackageResult sendAndRecv(final MsgPackage sendPackage) {
        return sendAndRecv(sendPackage, CommonConstants.TIME_OUT);
    }

    /**
     * 
     * 
     * @param sendPackage
     * @return
     */
    public SyncPackageResult retrySendAndRecv(final MsgPackage sendPackage) {
        return retrySend(3, sendPackage, CommonConstants.TIME_OUT);
    }

    /**
     * 向配置中心注册配置服务器
     */
    public SyncPackageResult sendAndRecv(final MsgPackage sendPackage, long timeout) {

        FutureTask<RecvPackage> task = new FutureTask<RecvPackage>(new Callable<RecvPackage>() {

            @Override
            public RecvPackage call() throws Exception {

                //同步锁
                LockUtil.syncLock(sendPackage.getMsgId());

                IoSession session = getCurSession();
                WriteFuture wf = session.write(sendPackage);
                if ((wf.isDone() && wf.isWritten())) {
                    logger.info("向[" + serverDesc + "]:" + sendPackage + "写入成功！");
                    msgSendMap.put(sendPackage.getMsgId(), sendPackage);
                } else {
                    //这里可做重试，暂时不做TODO
                    logger.info("向[" + serverDesc + "]:" + sendPackage + "写入失败!");
                }
                //同步读取锁
                LockUtil.readSyncLock(sendPackage.getMsgId());

                RecvPackage rp = (RecvPackage) msgRecvMap.get(sendPackage.getMsgId());
                if (rp != null) {
                    logger.info("从" + serverDesc + "[" + curAvaiableAddr + "]服务获取信息成功!");
                    return rp;
                }
                logger.warn("从" + serverDesc + "[" + curAvaiableAddr + "]服务获取信息失败!");
                return new RecvPackage(sendPackage.getMsgId());
            }
        });

        TaskUtil.runTask(task);
        SyncPackageResult spr = null;
        RecvPackage recvPackge = null;
        try {
            recvPackge = task.get(timeout, TimeUnit.MILLISECONDS);
            spr = new SyncPackageResult(
                recvPackge == null ? new RecvPackage(sendPackage.getMsgId()) : recvPackge);
        } catch (TimeoutException e) {
            LockUtil.releaseReadSyncLock(sendPackage.getMsgId());
            task.cancel(true);
            spr = new SyncPackageResult(
                recvPackge == null ? new RecvPackage(sendPackage.getMsgId()) : recvPackge);
            spr.setTimeout(true);

            logger.error("从[" + serverDesc + "]:" + sendPackage + "读取超时:", e);
        } catch (Exception e) {
            LockUtil.releaseReadSyncLock(sendPackage.getMsgId());
            task.cancel(true);
            spr = new SyncPackageResult(
                recvPackge == null ? new RecvPackage(sendPackage.getMsgId()) : recvPackge);
            logger.error("从[" + serverDesc + "]:" + sendPackage + "同步发收异常:", e);
        } finally {
            msgSendMap.remove(sendPackage.getMsgId());
            msgRecvMap.remove(sendPackage.getMsgId());
            LockUtil.releaseSyncLock(sendPackage.getMsgId());
        }
        return spr;
    }

    /**
     * 连接服务器
     */
    public void connect(InetSocketAddress avaiableAddr) {
        try {
            ProtocolCodecFilter filter = new ProtocolCodecFilter(
                new ObjectSerializationCodecFactory());
            connector = new NioSocketConnector();
            connector.getFilterChain().addLast("codec", filter);
            connector.setConnectTimeoutMillis(3000);
            connector.getSessionConfig().setWriteTimeout(5000);
            logger.info("客户端开始连接[" + serverDesc + "]服务器!" + avaiableAddr);
            connector.setHandler(new IoHandlerAdapter() {

                public void sessionCreated(IoSession session) throws Exception {
                    super.sessionCreated(session);
                }

                public void sessionClosed(IoSession session) throws Exception {
                    logger.info(session + "关闭" + MsgUtil.LINE_END);
                }

                public void messageSent(IoSession session, Object arg1) throws Exception {
                    logger.info("向[" + serverDesc + "]" + session + "发送消息:" + arg1);
                }

                public void messageReceived(IoSession session, Object arg1) throws Exception {
                    logger.info("接收到[" + serverDesc + "]" + session + "消息:" + arg1);
                    if (arg1 instanceof RecvPackage) {
                        recvMsg((RecvPackage) arg1);
                    } else {
                        //do nothing..心跳
                        logger.info(session + "心跳返回:" + arg1);
                    }
                }

                public void exceptionCaught(IoSession session, Throwable arg1) throws Exception {
                    logger.error("[" + serverDesc + "]" + session + "连接异常", arg1);
                    session.close(true);
                }
            });
            ConnectFuture future = connector.connect(avaiableAddr);
            future.await();
            if (future.isConnected()) {
                curSession = future.getSession();
                logger.info(curSession + "连接上" + serverDesc);

            } else {
                logger.error("无法连接上" + serverDesc);
            }

        } catch (Exception e) {
            logger.error("连接" + serverDesc + "异常", e);
        }
    }

    /**
     * 
     * 
     * @param msgRecv
     */
    private void recvMsg(RecvPackage msgRecv) {
        try {
            if (msgSendMap.get(msgRecv.getMsgId()) != null) {
                msgRecvMap.put(msgRecv.getMsgId(), msgRecv);
                //释放同步读取锁
                LockUtil.releaseReadSyncLock(msgRecv.getMsgId());
            } else {
                logger.warn(msgRecv + "无法找到之前的发送包,不加入接收包缓存!");
            }
        } catch (Exception e) {
            logger.error("接收[" + serverDesc + ":" + curAvaiableAddr + "]消息异常", e);
        }
    }

    public boolean hasAddrSurvive() {
        for (Boolean t : ipStatusMap.values()) {
            if (t == Boolean.TRUE) {
                return true;
            }
        }
        return false;
    }

    /**
     * 
     * 
     * @return
     */
    private IoSession getCurSession() {

        if (curSession != null && curSession.isConnected()) {
            return curSession;
        }

        if (curAvaiableAddr == null) {
            curAvaiableAddr = ipStatusMap.keySet().iterator().next();
        } else {
            boolean beFound = false;
            //圈迭代
            for (Iterator<Map.Entry<InetSocketAddress, Boolean>> iter = ipStatusMap.entrySet()
                .iterator(); iter.hasNext();) {
                //首先找到当前的，然后往下找一次
                if (curAvaiableAddr.equals(iter.next().getKey())) {
                    //向下循环
                    if (iter.hasNext()) {
                        beFound = true;
                        curAvaiableAddr = iter.next().getKey();
                        break;
                    } else {
                        //如果是末尾的话也和没找到一样,从头开始,do nothing
                    }
                }
            }
            //没有找到就重新开始
            if (beFound) {
                curAvaiableAddr = ipStatusMap.keySet().iterator().next();
            }
        }

        int i = 0;
        while (++i < 3 && (curSession == null || !curSession.isConnected())) {
            logger.info("正在重试[" + serverDesc + "]连接[" + curAvaiableAddr + "]第" + i + "次");
            connect(curAvaiableAddr);
            logger.info("重试[" + serverDesc + "]连接[" + curAvaiableAddr + "]第" + i + "次成功!");
        }
        if (i == 3) {
            //未连接上的
            if (!ipStatusMap.get(curAvaiableAddr)) {
                ipStatusMap.put(curAvaiableAddr, Boolean.TRUE);
            }
            logger.info("连接[" + serverDesc + curAvaiableAddr + "]超过重试次数3次，切换到下一个地址");
            //向下继续尝试
            getCurSession();

        } else {
            if (!ipStatusMap.get(curAvaiableAddr)) {
                ipStatusMap.put(curAvaiableAddr, Boolean.TRUE);
            }
        }
        return curSession;
    }

    /**
     * 加入地址
     * 
     * @param addr
     */
    public void addAddr(InetSocketAddress addr) {
        ipStatusMap.put(addr, Boolean.TRUE);
    }

    /**
     * 加入地址池
     * 
     * @param addrSet
     */
    public void addAddr(Set<InetSocketAddress> addrSet) {
        for (InetSocketAddress addr : addrSet) {
            addAddr(addr);
        }
    }

    public InetSocketAddress getCurAddr() {
        return curAvaiableAddr;
    }

    public Map<InetSocketAddress, Boolean> getIpStatusMap() {
        return ipStatusMap;
    }

}
