/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.brucexx.aries.protocol;

import java.io.Serializable;

/**
 * 
 * @author zhao.xiong
 * @version $Id: ConfigNodeInfo.java, v 0.1 2013-4-15 обнГ5:15:56 zhao.xiong Exp $
 */
public class ConfigNodeInfo implements Serializable {

    /**  */
    private static final long serialVersionUID = -263136438402334692L;

    private int               connectionCount;

    public int getConnectionCount() {
        return connectionCount;
    }

    public void setConnectionCount(int connectionCount) {
        this.connectionCount = connectionCount;
    }

}
