/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.brucexx.aries.server.center;

import java.util.HashSet;
import java.util.Set;

/**
 * configServer管理器，主要用于cofigServer的水平扩展，向 configServerManager注册节点信息后，通过
 * @author zhao.xiong
 * @version $Id: ConfigServerManager.java, v 0.1 2013-4-11 上午11:21:58 zhao.xiong Exp $
 */

public class ConfigCenterManager {

    /**configServer管理器端口， 由configServerManager监听,由configServer启动时向configServerManager注册信息 **/
    private static final int   CONFIG_MANAGER_PORT = 13147;

    /**注册进来的configServer的机器ip列表  **/
    private Set<String>        configServerSet     = new HashSet<String>();

    /**配置 **/
    private Set<ManagerClient> managerClientMap    = new HashSet<ManagerClient>();

    public void addOrUpdateConfigServer(String configServerIp) {
        
       
        configServerSet.add(configServerIp);
    }

    public void removeConfigServer(String configServerIp) {
        configServerSet.remove(configServerIp);
    }

}
