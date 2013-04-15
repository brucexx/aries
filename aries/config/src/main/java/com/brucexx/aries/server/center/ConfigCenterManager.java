/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.brucexx.aries.server.center;

import java.util.HashSet;
import java.util.Set;

/**
 * configServer����������Ҫ����cofigServer��ˮƽ��չ���� configServerManagerע��ڵ���Ϣ��ͨ��
 * @author zhao.xiong
 * @version $Id: ConfigServerManager.java, v 0.1 2013-4-11 ����11:21:58 zhao.xiong Exp $
 */

public class ConfigCenterManager {

    /**configServer�������˿ڣ� ��configServerManager����,��configServer����ʱ��configServerManagerע����Ϣ **/
    private static final int   CONFIG_MANAGER_PORT = 13147;

    /**ע�������configServer�Ļ���ip�б�  **/
    private Set<String>        configServerSet     = new HashSet<String>();

    /**���� **/
    private Set<ManagerClient> managerClientMap    = new HashSet<ManagerClient>();

    public void addOrUpdateConfigServer(String configServerIp) {
        
       
        configServerSet.add(configServerIp);
    }

    public void removeConfigServer(String configServerIp) {
        configServerSet.remove(configServerIp);
    }

}
