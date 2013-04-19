/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.brucexx.aries.communication.pkg.conf;

import com.brucexx.aries.communication.pkg.common.SendPackage;

/**
 * 注册节点信息发送包
 * configServer 发送给-->centerServer 
 * @author zhao.xiong
 * @version $Id: RegSendPackage.java, v 0.1 2013-4-18 下午4:30:42 zhao.xiong Exp $
 */
public class RegSendPackage extends SendPackage {

    /**  */
    private static final long serialVersionUID = 8005935630471503794L;

    /** 配置服务器唯一标识，MAC地址   **/
    private String            macAddr;

    public String getMacAddr() {
        return macAddr;
    }

    public void setMacAddr(String macAddr) {
        this.macAddr = macAddr;
    }

}
