/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.brucexx.aries.util;

import java.util.Random;
import java.util.UUID;

/**
 * 
 * @author zhao.xiong
 * @version $Id: MsgUtil.java, v 0.1 2013-4-11 ионГ10:40:53 zhao.xiong Exp $
 */
public class MsgUtil {

    private static Random      r        = new Random();

    public static final String LINE_END = System.getProperty("line.separator");

    public static String genMsgId() {
        return UUID.randomUUID().toString() + r.nextInt(10);
    }

}
