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
 * �߿���tcp�ͻ���,�����ַ�б������л�
 * @author zhao.xiong
 * @version $Id: MinaClient.java, v 0.1 2013-4-18 ����11:07:38 zhao.xiong Exp $
 */
public class HATcpClient {

    private static Logger                   logger      = Logger.getLogger("common-transfer");

    /** ��Ϣ���ͻ���map�������첽תͬ��**/
    private final Map<String, MsgPackage>   msgSendMap  = new HashMap<String, MsgPackage>();

    /** ��Ϣ���ܻ���map�������첽תͬ��**/
    private final Map<String, RecvPackage>  msgRecvMap  = new ConcurrentHashMap<String, RecvPackage>();

    /** ��������configManager�������� **/
    private NioSocketConnector              connector;
    /** ֻ����һ������ **/
    private IoSession                       curSession;

    /** ���õĵ�ַ,��ipSet�����ѡ��**/
    private InetSocketAddress               curAvaiableAddr;

    /**��������Ϣ���� **/
    private String                          serverDesc  = CommonConstants.CONFIG_SERVER_DESC;

    /**HA����ip�б� **/
    private Map<InetSocketAddress, Boolean> ipStatusMap = new TreeMap<InetSocketAddress, Boolean>();

    public HATcpClient(Set<InetSocketAddress> ipSet) {
        for (InetSocketAddress ipAddr : ipSet) {
            ipStatusMap.put(ipAddr, Boolean.TRUE);
        }
    }

    /**
     * 
     * @param ipSet
     * @param serverDesc ��������������
     */
    public HATcpClient(Set<InetSocketAddress> ipSet, String serverDesc) {
        this(ipSet);
        this.serverDesc = serverDesc;

    }

    public void heartBeat() {
        IoSession session = getCurSession();
        session.write(new HeartBeatPackage());
        logger.info("��[" + serverDesc + "]��������--->");
    }

    public SyncPackageResult retryOnceSend(final int retryTimes, final MsgPackage sendPackage,
                                           long timeout) {
        int i = 0;
        SyncPackageResult synResult = null;
        while (i++ < retryTimes && (synResult = onceSendAndRecv(sendPackage, timeout)).isTimeout()) {
            logger.info("2S������");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                logger.error("", e);
            }
        }
        if (i == retryTimes) {
            logger.warn("����" + retryTimes + "��ʧ��!");
            return null;
        }
        return synResult;
    }

    /**
     * ���Է���
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
            logger.info("2S������");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                logger.error("", e);
            }
        }
        if (i == retryTimes) {
            logger.warn("����" + retryTimes + "��ʧ��!");
            return null;
        }
        return synResult;
    }

    /**
     * ������
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
     * ����������ע�����÷�����
     */
    public SyncPackageResult sendAndRecv(final MsgPackage sendPackage, long timeout) {

        FutureTask<RecvPackage> task = new FutureTask<RecvPackage>(new Callable<RecvPackage>() {

            @Override
            public RecvPackage call() throws Exception {

                //ͬ����
                LockUtil.syncLock(sendPackage.getMsgId());

                IoSession session = getCurSession();
                WriteFuture wf = session.write(sendPackage);
                if ((wf.isDone() && wf.isWritten())) {
                    logger.info("��[" + serverDesc + "]:" + sendPackage + "д��ɹ���");
                    msgSendMap.put(sendPackage.getMsgId(), sendPackage);
                } else {
                    //����������ԣ���ʱ����TODO
                    logger.info("��[" + serverDesc + "]:" + sendPackage + "д��ʧ��!");
                }
                //ͬ����ȡ��
                LockUtil.readSyncLock(sendPackage.getMsgId());

                RecvPackage rp = (RecvPackage) msgRecvMap.get(sendPackage.getMsgId());
                if (rp != null) {
                    logger.info("��" + serverDesc + "[" + curAvaiableAddr + "]�����ȡ��Ϣ�ɹ�!");
                    return rp;
                }
                logger.warn("��" + serverDesc + "[" + curAvaiableAddr + "]�����ȡ��Ϣʧ��!");
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

            logger.error("��[" + serverDesc + "]:" + sendPackage + "��ȡ��ʱ:", e);
        } catch (Exception e) {
            LockUtil.releaseReadSyncLock(sendPackage.getMsgId());
            task.cancel(true);
            spr = new SyncPackageResult(
                recvPackge == null ? new RecvPackage(sendPackage.getMsgId()) : recvPackge);
            logger.error("��[" + serverDesc + "]:" + sendPackage + "ͬ�������쳣:", e);
        } finally {
            msgSendMap.remove(sendPackage.getMsgId());
            msgRecvMap.remove(sendPackage.getMsgId());
            LockUtil.releaseSyncLock(sendPackage.getMsgId());
        }
        return spr;
    }

    /**
     * ���ӷ�����
     */
    public void connect(InetSocketAddress avaiableAddr) {
        try {
            ProtocolCodecFilter filter = new ProtocolCodecFilter(
                new ObjectSerializationCodecFactory());
            connector = new NioSocketConnector();
            connector.getFilterChain().addLast("codec", filter);
            connector.setConnectTimeoutMillis(3000);
            connector.getSessionConfig().setWriteTimeout(5000);
            logger.info("�ͻ��˿�ʼ����[" + serverDesc + "]������!" + avaiableAddr);
            connector.setHandler(new IoHandlerAdapter() {

                public void sessionCreated(IoSession session) throws Exception {
                    super.sessionCreated(session);
                }

                public void sessionClosed(IoSession session) throws Exception {
                    logger.info(session + "�ر�" + MsgUtil.LINE_END);
                }

                public void messageSent(IoSession session, Object arg1) throws Exception {
                    logger.info("��[" + serverDesc + "]" + session + "������Ϣ:" + arg1);
                }

                public void messageReceived(IoSession session, Object arg1) throws Exception {
                    logger.info("���յ�[" + serverDesc + "]" + session + "��Ϣ:" + arg1);
                    if (arg1 instanceof RecvPackage) {
                        recvMsg((RecvPackage) arg1);
                    } else {
                        //do nothing..����
                        logger.info(session + "��������:" + arg1);
                    }
                }

                public void exceptionCaught(IoSession session, Throwable arg1) throws Exception {
                    logger.error("[" + serverDesc + "]" + session + "�����쳣", arg1);
                    session.close(true);
                }
            });
            ConnectFuture future = connector.connect(avaiableAddr);
            future.await();
            if (future.isConnected()) {
                curSession = future.getSession();
                logger.info(curSession + "������" + serverDesc);

            } else {
                logger.error("�޷�������" + serverDesc);
            }

        } catch (Exception e) {
            logger.error("����" + serverDesc + "�쳣", e);
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
                //�ͷ�ͬ����ȡ��
                LockUtil.releaseReadSyncLock(msgRecv.getMsgId());
            } else {
                logger.warn(msgRecv + "�޷��ҵ�֮ǰ�ķ��Ͱ�,��������հ�����!");
            }
        } catch (Exception e) {
            logger.error("����[" + serverDesc + ":" + curAvaiableAddr + "]��Ϣ�쳣", e);
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
            //Ȧ����
            for (Iterator<Map.Entry<InetSocketAddress, Boolean>> iter = ipStatusMap.entrySet()
                .iterator(); iter.hasNext();) {
                //�����ҵ���ǰ�ģ�Ȼ��������һ��
                if (curAvaiableAddr.equals(iter.next().getKey())) {
                    //����ѭ��
                    if (iter.hasNext()) {
                        beFound = true;
                        curAvaiableAddr = iter.next().getKey();
                        break;
                    } else {
                        //�����ĩβ�Ļ�Ҳ��û�ҵ�һ��,��ͷ��ʼ,do nothing
                    }
                }
            }
            //û���ҵ������¿�ʼ
            if (beFound) {
                curAvaiableAddr = ipStatusMap.keySet().iterator().next();
            }
        }

        int i = 0;
        while (++i < 3 && (curSession == null || !curSession.isConnected())) {
            logger.info("��������[" + serverDesc + "]����[" + curAvaiableAddr + "]��" + i + "��");
            connect(curAvaiableAddr);
            logger.info("����[" + serverDesc + "]����[" + curAvaiableAddr + "]��" + i + "�γɹ�!");
        }
        if (i == 3) {
            //δ�����ϵ�
            if (!ipStatusMap.get(curAvaiableAddr)) {
                ipStatusMap.put(curAvaiableAddr, Boolean.TRUE);
            }
            logger.info("����[" + serverDesc + curAvaiableAddr + "]�������Դ���3�Σ��л�����һ����ַ");
            //���¼�������
            getCurSession();

        } else {
            if (!ipStatusMap.get(curAvaiableAddr)) {
                ipStatusMap.put(curAvaiableAddr, Boolean.TRUE);
            }
        }
        return curSession;
    }

    /**
     * �����ַ
     * 
     * @param addr
     */
    public void addAddr(InetSocketAddress addr) {
        ipStatusMap.put(addr, Boolean.TRUE);
    }

    /**
     * �����ַ��
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
