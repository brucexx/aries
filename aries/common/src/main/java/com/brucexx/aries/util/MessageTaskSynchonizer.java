/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.brucexx.aries.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.commons.lang.StringUtils;

import com.brucexx.aries.exception.Assert;
import com.brucexx.aries.exception.ResultCode;
import com.brucexx.aries.protocol.MsgPackage;

/**
 * 
 * @author zhao.xiong
 * @version $Id: MessageTaskSynchonizer.java, v 0.1 2013-4-12 上午10:03:46 zhao.xiong Exp $
 */
public class MessageTaskSynchonizer {

    private static final Map<String, MsgPackage> map  = new HashMap<String, MsgPackage>();

    private static ExecutorService               pool = Executors.newFixedThreadPool(50);

    public static void setExecutor(ExecutorService pool) throws InterruptedException {
        Assert.arrayNotNull(ResultCode.NOT_EMPTY, pool);
        if (MessageTaskSynchonizer.pool != null) {
            MessageTaskSynchonizer.pool.awaitTermination(10, TimeUnit.SECONDS);
        }

        MessageTaskSynchonizer.pool = pool;
    }

    public static void send(MsgPackage sendPackage) {
        map.put(sendPackage.getMsgId(), sendPackage);
    }
    
    /** 
     * 接收到消息后
     * 
     * @param recvPackage
     */
    public static void recvMsg(MsgPackage recvPackage){
        if(StringUtils.isEmpty(recvPackage.getMsgId())){
            return;
        }
        
    }
    
}
