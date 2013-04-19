/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.brucexx.aries.server.node;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;

import com.brucexx.aries.communication.client.HATcpClient;
import com.brucexx.aries.communication.pkg.common.SyncPackageResult;
import com.brucexx.aries.communication.pkg.conf.RegRespPackage;
import com.brucexx.aries.context.ConfigContext;
import com.brucexx.aries.exception.Assert;
import com.brucexx.aries.exception.ResultCode;
import com.brucexx.aries.protocol.CommonConstants;
import com.brucexx.aries.server.ConfigServerEnum;
import com.brucexx.aries.util.MsgUtil;
import com.brucexx.aries.util.SystemUtil;

/**
 * ��������ע��ͻ�����
 * @author zhao.xiong
 * @version $Id: ConfigServerManagerClient.java, v 0.1 2013-4-12 ����1:24:41 zhao.xiong Exp $
 */
public class CenterRegClient {

    private static Logger logger = Logger.getLogger("aries-config");

    private boolean       init   = false;

    private HATcpClient   client;

    public CenterRegClient() {

        client = new HATcpClient(new HashSet<InetSocketAddress>(),
            CommonConstants.CENTER_SERVER_DESC);

        if (StringUtils.isNotEmpty(ConfigContext.get(ConfigServerEnum.CENTER_HOST_POOL))) {
            logger.info("��⵽ע���������ã���ע��������ӵ�ַ��");
            String[] managerIpList = StringUtils.split(
                ConfigContext.get(ConfigServerEnum.CENTER_HOST_POOL), ';');
            Assert.isTrue(managerIpList != null && managerIpList.length > 0,
                ResultCode.NO_HOST_ADDR);
            for (String addr : managerIpList) {
                String[] addrInfo = StringUtils.split(addr, ":");
                InetSocketAddress ipAddr = new InetSocketAddress(addrInfo[0], NumberUtils.toInt(
                    addrInfo[1], ConfigContext.getCenterHostPort()));
                client.addAddr(ipAddr);
            }
            logger.info("���ע�����ĵ�ַ��:" + client.getIpStatusMap());
            startHeartBeat();
        }

    }

    public void startHeartBeat() {
        if (!init) {
            Timer timer = new Timer(true);
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    //�˴�
                    client.heartBeat();
                }
            }, 5000, 30000);
            init = true;
        }
    }

    /**
     * ע���������ģ�����
     */
    public boolean regConfigCenter() {
        RegRespPackage regPackage = new RegRespPackage();
        regPackage.setMsgId(MsgUtil.genMsgId());
        regPackage.setConfigServiceId(SystemUtil.getMAC());
        SyncPackageResult recv = client.sendAndRecv(regPackage, 5000);
        if (!recv.isTimeout() && recv.getRecvPackage().isSuccess()) {
            logger.info("ע������" + client.getCurAddr() + "�ɹ���");
            return true;
        } else {
            logger.warn("ע������" + client.getCurAddr() + "ʧ�ܣ�");
            return false;
        }
    }

}
