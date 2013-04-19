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
 * configServer中心，主要用于cofigServer的水平扩展，向 configServerManager注册节点信息后，通过
 * @author zhao.xiong
 * @version $Id: ConfigServerManager.java, v 0.1 2013-4-11 上午11:21:58 zhao.xiong Exp $
 */
@Component("configCenterServer")
public class CenterServer {

    private static Logger       logger          = Logger.getLogger("config-center");

    /** 配置中心服务器**/
    private static CommonServer centerServer;

    /**注册进来的configServer的机器ip列表  **/
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
             * 处理接收信息
             * 
             * @param session
             * @param sendPackage
             */
            protected void processRecv(IoSession session, SendPackage sendPackage) {
                if (sendPackage instanceof RegSendPackage) {
                    //处理注册信息
                    regProcess(session, (RegSendPackage) sendPackage);
                } else if (sendPackage instanceof NodeInfoSendPackage) {
                    //获取配置服务器
                    nodeGetProcess(session, (NodeInfoSendPackage) sendPackage);
                }
            }

        };
        centerServer.setLogger(logger);
        centerServer.startServer(ConfigContext.getCenterHostPort(), "配置中心");

    }

    /**
     * 用来获取配置服务器的信息
     * @param session
     * @param nodeSendPkg
     */
    private void nodeGetProcess(IoSession session, NodeInfoSendPackage nodeSendPkg) {
        //返回在该注册中心的配置服务器ip
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
                logger.warn(session + "的config MAC为空");
                rpp.setErrorMsg("config MAC为空");
                return;
            }
            rpp.setMacAddr(regPackage.getMacAddr());
            rpp.setMsgId(regPackage.getMsgId());
            rpp.setSuccess(true);

        } catch (Exception e) {
            logger.error("注册异常", e);
            rpp.setException(e);
        } finally {
            centerServer.writeResp(session, rpp);
        }
    }

}
