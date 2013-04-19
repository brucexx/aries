/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.brucexx.aries.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import com.brucexx.aries.communication.pkg.common.RecvPackage;

/**
 * 
 * @author zhao.xiong
 * @version $Id: MessageTaskSynchonizer.java, v 0.1 2013-4-12 ионГ10:03:46 zhao.xiong Exp $
 */
public class TaskUtil {

    private static ExecutorService pool = Executors.newFixedThreadPool(50);

    public static void runTask(FutureTask<RecvPackage> futureTask) {
        pool.execute(futureTask);
    }

}
