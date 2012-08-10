/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.brucexx;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * @author zhao.xiong
 * @version $Id: DhServiceImpl.java, v 0.1 2012-8-10 ÏÂÎç03:36:29 zhao.xiong Exp $
 */
public class DhServiceImpl implements DhService {

    /** 
     * @see com.brucexx.DhService#sy(java.lang.String)
     */
    @Override
    public void sy(String ok) {
        System.out.println("ok==>" + ok);
    }

    /** 
     * @see com.brucexx.DhService#sk(java.lang.String)
     */
    @Override
    public String sk(String doIt) {
        System.out.println("doIt==>" + doIt);
        return new SimpleDateFormat("yyyy:MM:dd HH:mm:ss").format(new Date());
    }

}
