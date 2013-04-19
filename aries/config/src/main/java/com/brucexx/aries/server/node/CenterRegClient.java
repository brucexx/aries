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
 * 配置中心注册客户端子
 * @author zhao.xiong
 * @version $Id: ConfigServerManagerClient.java, v 0.1 2013-4-12 下午1:24:41 zhao.xiong Exp $
 */
public class CenterRegClient {

    private static Logger logger = Logger.getLogger("aries-config");

    private boolean       init   = false;

    private HATcpClient   client;

    public CenterRegClient() {

        client = new HATcpClient(new HashSet<InetSocketAddress>(),
            CommonConstants.CENTER_SERVER_DESC);

        if (StringUtils.isNotEmpty(ConfigContext.get(ConfigServerEnum.CENTER_HOST_POOL))) {
            logger.info("检测到注册中心配置，向注册中心添加地址池");
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
            logger.info("添加注册中心地址池:" + client.getIpStatusMap());
            startHeartBeat();
        }

    }

    public void startHeartBeat() {
        if (!init) {
            Timer timer = new Timer(true);
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    //此处
                    client.heartBeat();
                }
            }, 5000, 30000);
            init = true;
        }
    }

    /**
     * 注册配置中心，返回
     */
    public boolean regConfigCenter() {
        RegRespPackage regPackage = new RegRespPackage();
        regPackage.setMsgId(MsgUtil.genMsgId());
        regPackage.setConfigServiceId(SystemUtil.getMAC());
        SyncPackageResult recv = client.sendAndRecv(regPackage, 5000);
        if (!recv.isTimeout() && recv.getRecvPackage().isSuccess()) {
            logger.info("注册中心" + client.getCurAddr() + "成功！");
            return true;
        } else {
            logger.warn("注册中心" + client.getCurAddr() + "失败！");
            return false;
        }
    }

}
