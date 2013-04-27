/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.brucexx.aries.server.node;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.mina.core.session.IoSession;
import org.springframework.stereotype.Component;

import com.brucexx.aries.communication.client.CommonServer;
import com.brucexx.aries.communication.pkg.common.SendPackage;
import com.brucexx.aries.communication.pkg.conf.RegRespPackage;
import com.brucexx.aries.communication.pkg.soa.ProtocolRespPackage;
import com.brucexx.aries.communication.pkg.soa.ProtocolSendPackage;
import com.brucexx.aries.context.ConfigContext;
import com.brucexx.aries.data.ConfigDataRepository;
import com.brucexx.aries.data.ServiceInfo;

/**
 * ��������server�����ڸ��ͻ��˷���ע����Ϣ�Լ�����service�Ļ�����Ϣ��
 * һ����
 * @author zhao.xiong
 * @version $Id: ConfigServer.java, v 0.1 2013-4-9 ����4:09:53 zhao.xiong Exp $
 */
@Component("configServer")
public class ConfigServer {

    private static Logger          logger = Logger.getLogger("config-server");

    /**������������ **/
    private ConfigDataRepository   configDataRepository;

    /** �������ķ�����**/
    private static CommonServer    centerServer;

    /**��������ע��ͻ��� **/
    private static CenterRegClient centerRegClient;

    /**
     * 
     */
    public void startConfigServer() {
        try {

            centerServer = new CommonServer() {
                /**
                 * ���������Ϣ
                 * 
                 * @param session
                 * @param sendPackage
                 */
                protected void processRecv(IoSession session, SendPackage sendPackage) {
                    if (sendPackage instanceof ProtocolSendPackage) {
                        //����ע���Ӧ����Դ��Ϣ
                        commandProcess(session, (ProtocolSendPackage) sendPackage);
                    }
                }
            };

            centerServer.setLogger(logger);
            centerServer.startServer(ConfigContext.getConfigHostPort(), "���÷�����");

            //����������ע��
            centerRegClient = new CenterRegClient();
            //ͨ��ע������
            centerRegClient.startHeartBeat();
            //
            centerRegClient.regConfigCenter();

        } catch (Exception e) {
            logger.error("��ע������ע���쳣", e);
        }

    }

    /**
     * 
     * @param session
     * @param arg1
     */
    protected void commandProcess(IoSession session, ProtocolSendPackage sendPackage) {
        //�����service�Ļ�ע����Դ�����򷵻���ע�Ტ���Ǵ��״̬��service�б�
        ProtocolRespPackage rp = new ProtocolRespPackage();
        rp.setResourceId(sendPackage.getResourceId());
        rp.setGroupId(sendPackage.getGroupId());
        rp.setProtocol(sendPackage.getProtocol());
        rp.setMsgId(sendPackage.getMsgId());
        rp.setService(sendPackage.isService());

        if (sendPackage.isService()) {
            try {
                logger.info(session + "ע��service[" + sendPackage + "]");
                //ע��service��Դ
                ServiceInfo serviceInfo = new ServiceInfo();
                serviceInfo.setGroupId(sendPackage.getGroupId());
                serviceInfo.setHostIp(((InetSocketAddress) session.getRemoteAddress()).getAddress()
                    .getHostAddress());
                serviceInfo.setProtocol(sendPackage.getProtocol());
                serviceInfo.setResourceId(sendPackage.getResourceId());

                configDataRepository.regService(serviceInfo);
                logger.info(session + "[" + sendPackage + "]ע��service�ɹ���");
                //����ע��ɹ�����Ϣ
                rp.setSuccess(true);
            } catch (Exception e) {
                logger.error(session + "[" + sendPackage + "]ע��service�쳣��", e);
                rp.setException(e);
            } finally {
                centerServer.writeResp(session, rp);
            }

        } else {
            logger.info(session + "��ѯref[" + sendPackage + "]");
            //ref�Ļ���ȡ�б�
            List<String> nodeInfoList = new ArrayList<String>();
            try {

                List<ServiceInfo> hostList = configDataRepository.getServiceNodeList(
                    sendPackage.getGroupId(), sendPackage.getResourceId(),
                    sendPackage.getProtocol());

                for (ServiceInfo serviceInfo : hostList) {
                    nodeInfoList.add(serviceInfo.getHostIp());
                }
                rp.setSuccess(true);
                rp.setNodeInfoList(nodeInfoList);
            } catch (Exception e) {
                logger.error(session + "[" + sendPackage + "]��ȡserviceList�쳣��", e);
                rp.setException(e);
            } finally {
                centerServer.writeResp(session, rp);
                logger.info(session + "��Ӧservice�б�:" + nodeInfoList);
            }
        }
    }

    public void setConfigDataRepository(ConfigDataRepository configDataRepository) {
        this.configDataRepository = configDataRepository;
    }

}
