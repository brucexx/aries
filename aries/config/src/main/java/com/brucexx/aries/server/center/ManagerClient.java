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
 * ���ﱣ��һ�����Ӿͺ���
 * @author zhao.xiong
 * @version $Id: ConfigServerManagerClient.java, v 0.1 2013-4-12 ����1:24:41 zhao.xiong Exp $
 */
public class ManagerClient {

    /**configServer�������˿ڣ� ��configServerManager����,��configServer����ʱ��configServerManagerע����Ϣ **/
    private static final int              CONFIG_MANAGER_PORT = 13147;

    private static Logger                 logger              = Logger.getLogger("aries-config");

    /** configserver��������**/
    private Map<ConfigServerEnum, String> map                 = new HashMap<ConfigServerEnum, String>();

    /** ��Ϣ���ͻ���map�������첽תͬ��**/
    private final Map<String, MsgPackage> msgSendMap          = new HashMap<String, MsgPackage>();

    /** ��Ϣ���ܻ���map�������첽תͬ��**/
    private final Map<String, MsgPackage> msgRecvMap          = new ConcurrentHashMap<String, MsgPackage>();

    /** ��������configManager�������� **/
    private NioSocketConnector            connector;

    /** ֻ����һ������ **/
    private IoSession                     curSession;

    /** ���������ʼ��**/
    private boolean                       init                = false;

    /**����������ip **/
    private String                        managerIp;

    public void startHeartBeat() {
        if (!init) {
            Timer timer = new Timer(true);
            timer.schedule(new TimerTask() {

                @Override
                public void run() {
                    //�˴�
                    IoSession session;
                    try {
                        session = getCurSession();
                        session.write(new HeartBeatPackage());
                        logger.info("����ά�ַ���--->");
                    } catch (InterruptedException e) {
                        logger.error("��ȡ��ǰsession��ʱ", e);
                    }

                }
            }, 5000, 30000);
            init = true;
        }
    }

    public void regConfigCenter() {
        RecvPackage recv = sendAndRecv();
        if (!recv.isSuccess()) {
            logger.error("ע��ʧ�ܣ�");
        }
    }

    /**
     * ����������ע�����÷�����
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
                logger.error("ע�����÷�������д���쳣", e);
            }
        }
        if (i == 5 && !isWriteOK) {
            logger.info(regPackage.getMsgId() + "ע��ʧ�ܣ�");
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
                        logger.info("�����÷�����������[" + managerIp + "]����ע�����ýڵ�ɹ�!");
                        return rp;
                    }
                }
            }
            logger.warn("�����÷�����������[" + managerIp + "]����ע�����ýڵ�ʧ��!");

        } catch (Exception e) {
            logger.error("ע�����÷��������쳣", e);
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
                logger.warn(msgRecv + "�޷��ҵ�֮ǰ�ķ��Ͱ�,��������հ�����!");
            }

        } catch (Exception e) {
            logger.error("�������÷���������Ϣ�쳣", e);
        }
    }

    /**
     * ���ӹ���������
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

            logger.info("�ͻ��˿�ʼ���ӷ�����!" + managerIp);

            connector.setHandler(new IoHandlerAdapter() {

                public void sessionCreated(IoSession session) throws Exception {
                    super.sessionCreated(session);
                }

                public void sessionClosed(IoSession session) throws Exception {
                    logger.info(session + "�ر�" + MsgUtil.LINE_END);
                }

                public void messageSent(IoSession session, Object arg1) throws Exception {

                    logger.info("��" + session + "������Ϣ:" + arg1);
                }

                public void messageReceived(IoSession session, Object arg1) throws Exception {
                    logger.info("���յ�" + session + "��Ϣ:" + arg1);
                    if (arg1 instanceof MsgPackage) {
                        recvMsg((MsgPackage) arg1);
                    } else {
                        //do nothing..����
                        logger.info(session + "��������:" + arg1);
                    }

                }

                public void exceptionCaught(IoSession session, Throwable arg1) throws Exception {
                    logger.error(session + "�ͻ����쳣", arg1);
                    session.close(true);
                }
            });
            ConnectFuture future = connector.connect(new InetSocketAddress(managerIp,
                CONFIG_MANAGER_PORT));
            try {
                future.await();
                if (future.isConnected()) {
                    curSession = future.getSession();
                    logger.info(curSession + "���������÷�������������!");
                    startHeartBeat();
                } else {
                    logger.error("�޷����������÷�������������!");
                }
            } catch (InterruptedException ex) {
                logger.error("�������÷��������������쳣", ex);
            }
        } catch (Exception e) {
            logger.error("�������÷��������������쳣", e);
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
            logger.info("�����������ӵ�" + i + "��");
            connectManager();
            logger.info("�������ӵ�" + i + "�γɹ�!");
        }
        if (i == 20) {
            logger.info("�������Դ���20�Σ�10���Ӻ���������");
            Thread.sleep(600000);
            getCurSession();
        }
        return curSession;
    }
}
