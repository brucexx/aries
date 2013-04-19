/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.brucexx.aries.communication.pkg.conf;

import com.brucexx.aries.communication.pkg.common.RecvPackage;

/**
 * 注册节点信息响应包
 *  centerServer 响应给-->configServer 
 * @author zhao.xiong
 * @version $Id: NodeRegPackage.java, v 0.1 2013-4-11 下午5:02:18 zhao.xiong Exp $
 */
public class RegRespPackage extends RecvPackage {

    /**  */
    private static final long serialVersionUID = 2451740169267558544L;
    /** 注册机器唯一标识，MAC地址   **/
    private String            macAddr;

    public String getMacAddr() {
        return macAddr;
    }

    public void setMacAddr(String macAddr) {
        this.macAddr = macAddr;
    }

}
