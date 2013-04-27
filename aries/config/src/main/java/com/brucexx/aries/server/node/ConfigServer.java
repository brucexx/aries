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
 * 配置中心server，用于给客户端返回注册信息以及保存service的机器信息，
 * 一旦和
 * @author zhao.xiong
 * @version $Id: ConfigServer.java, v 0.1 2013-4-9 下午4:09:53 zhao.xiong Exp $
 */
@Component("configServer")
public class ConfigServer {

    private static Logger          logger = Logger.getLogger("config-server");

    /**配置中心数据 **/
    private ConfigDataRepository   configDataRepository;

    /** 配置中心服务器**/
    private static CommonServer    centerServer;

    /**配置中心注册客户端 **/
    private static CenterRegClient centerRegClient;

    /**
     * 
     */
    public void startConfigServer() {
        try {

            centerServer = new CommonServer() {
                /**
                 * 处理接收信息
                 * 
                 * @param session
                 * @param sendPackage
                 */
                protected void processRecv(IoSession session, SendPackage sendPackage) {
                    if (sendPackage instanceof ProtocolSendPackage) {
                        //处理注册对应的资源信息
                        commandProcess(session, (ProtocolSendPackage) sendPackage);
                    }
                }
            };

            centerServer.setLogger(logger);
            centerServer.startServer(ConfigContext.getConfigHostPort(), "配置服务器");

            //向配置中心注册
            centerRegClient = new CenterRegClient();
            //通过注册心跳
            centerRegClient.startHeartBeat();
            //
            centerRegClient.regConfigCenter();

        } catch (Exception e) {
            logger.error("向注册中心注册异常", e);
        }

    }

    /**
     * 
     * @param session
     * @param arg1
     */
    protected void commandProcess(IoSession session, ProtocolSendPackage sendPackage) {
        //如果是service的话注册资源，否则返回已注册并且是存活状态的service列表
        ProtocolRespPackage rp = new ProtocolRespPackage();
        rp.setResourceId(sendPackage.getResourceId());
        rp.setGroupId(sendPackage.getGroupId());
        rp.setProtocol(sendPackage.getProtocol());
        rp.setMsgId(sendPackage.getMsgId());
        rp.setService(sendPackage.isService());

        if (sendPackage.isService()) {
            try {
                logger.info(session + "注册service[" + sendPackage + "]");
                //注册service资源
                ServiceInfo serviceInfo = new ServiceInfo();
                serviceInfo.setGroupId(sendPackage.getGroupId());
                serviceInfo.setHostIp(((InetSocketAddress) session.getRemoteAddress()).getAddress()
                    .getHostAddress());
                serviceInfo.setProtocol(sendPackage.getProtocol());
                serviceInfo.setResourceId(sendPackage.getResourceId());

                configDataRepository.regService(serviceInfo);
                logger.info(session + "[" + sendPackage + "]注册service成功！");
                //返回注册成功的消息
                rp.setSuccess(true);
            } catch (Exception e) {
                logger.error(session + "[" + sendPackage + "]注册service异常！", e);
                rp.setException(e);
            } finally {
                centerServer.writeResp(session, rp);
            }

        } else {
            logger.info(session + "查询ref[" + sendPackage + "]");
            //ref的话获取列表
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
                logger.error(session + "[" + sendPackage + "]获取serviceList异常！", e);
                rp.setException(e);
            } finally {
                centerServer.writeResp(session, rp);
                logger.info(session + "响应service列表:" + nodeInfoList);
            }
        }
    }

    public void setConfigDataRepository(ConfigDataRepository configDataRepository) {
        this.configDataRepository = configDataRepository;
    }

}
