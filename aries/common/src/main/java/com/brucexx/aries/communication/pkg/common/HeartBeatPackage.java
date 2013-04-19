/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.brucexx.aries.communication.pkg.common;

import java.io.Serializable;

/**
 * 
 * @author zhao.xiong
 * @version $Id: HeartBeatPackage.java, v 0.1 2013-4-10 ÏÂÎç2:46:10 zhao.xiong Exp $
 */
public class HeartBeatPackage implements Serializable {

    /**  */
    private static final long serialVersionUID = 6828433385389756704L;

    private String            str              = "aries";

    private String            remoteIp;

    public String getStr() {
        return str;
    }

    public void setStr(String s) {
        this.str = s;
    }

    public String getRemoteIp() {
        return remoteIp;
    }

    public void setRemoteIp(String remoteIp) {
        this.remoteIp = remoteIp;
    }

}
