/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.brucexx.aries.server.center;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.mina.core.session.IoSession;
import org.springframework.stereotype.Component;

import com.brucexx.aries.communication.client.CommonServer;
import com.brucexx.aries.communication.pkg.common.SendPackage;
import com.brucexx.aries.communication.pkg.conf.NodeInfoRespPackage;
import com.brucexx.aries.communication.pkg.conf.NodeInfoSendPackage;
import com.brucexx.aries.communication.pkg.conf.RegRespPackage;
import com.brucexx.aries.communication.pkg.conf.RegSendPackage;
import com.brucexx.aries.context.ConfigContext;

/**
 * configServer���ģ���Ҫ����cofigServer��ˮƽ��չ���� configServerManagerע��ڵ���Ϣ��ͨ��
 * @author zhao.xiong
 * @version $Id: ConfigServerManager.java, v 0.1 2013-4-11 ����11:21:58 zhao.xiong Exp $
 */
@Component("configCenterServer")
public class CenterServer {

    private static Logger       logger          = Logger.getLogger("config-center");

    /** �������ķ�����**/
    private static CommonServer centerServer;

    /**ע�������configServer�Ļ���ip�б�  **/
    private Set<String>         configServerSet = new HashSet<String>();

    public void addOrUpdateConfigServer(String configServerIp) {
        configServerSet.add(configServerIp);
    }

    public void removeConfigServer(String configServerIp) {
        configServerSet.remove(configServerIp);
    }

    /**
     * 
     */
    public void startCenterServer() {

        centerServer = new CommonServer() {
            /**
             * ���������Ϣ
             * 
             * @param session
             * @param sendPackage
             */
            protected void processRecv(IoSession session, SendPackage sendPackage) {
                if (sendPackage instanceof RegSendPackage) {
                    //����ע����Ϣ
                    regProcess(session, (RegSendPackage) sendPackage);
                } else if (sendPackage instanceof NodeInfoSendPackage) {
                    //��ȡ���÷�����
                    nodeGetProcess(session, (NodeInfoSendPackage) sendPackage);
                }
            }

        };
        centerServer.setLogger(logger);
        centerServer.startServer(ConfigContext.getCenterHostPort(), "��������");

    }

    /**
     * ������ȡ���÷���������Ϣ
     * @param session
     * @param nodeSendPkg
     */
    private void nodeGetProcess(IoSession session, NodeInfoSendPackage nodeSendPkg) {
        //�����ڸ�ע�����ĵ����÷�����ip
        NodeInfoRespPackage resp = new NodeInfoRespPackage();
        resp.setConfServerSet(configServerSet);
        resp.setMsgId(nodeSendPkg.getMsgId());
        centerServer.writeResp(session, resp);
    }

    /**
     * 
     * @param session
     * @param arg1
     */
    private void regProcess(IoSession session, RegSendPackage regPackage) {
        RegRespPackage rpp = new RegRespPackage();
        try {
            //1.check msg
            if (StringUtils.isEmpty(regPackage.getMacAddr())) {
                logger.warn(session + "��config MACΪ��");
                rpp.setErrorMsg("config MACΪ��");
                return;
            }
            rpp.setMacAddr(regPackage.getMacAddr());
            rpp.setMsgId(regPackage.getMsgId());
            rpp.setSuccess(true);

        } catch (Exception e) {
            logger.error("ע���쳣", e);
            rpp.setException(e);
        } finally {
            centerServer.writeResp(session, rpp);
        }
    }

}
