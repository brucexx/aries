/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.brucexx.aries.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 
 * @author zhao.xiong
 * @version $Id: LockUtil.java, v 0.1 2013-4-19 ����10:43:44 zhao.xiong Exp $
 */
public class LockUtil {

    private static Map<String, ReentrantLock> lockPool = new HashMap<String, ReentrantLock>();

    /**
     * ��ȡͬ��ֱ��synRelease,�����첽תͬ��
     * 
     * @param id
     * @throws Exception
     */
    public static void syncLock(String id) {
        if (lockPool.containsKey(id)) {
            return;
        }
        ReentrantLock lock = new ReentrantLock();
        lockPool.put(id, lock);
        lock.lock();
    }

    /**
     * д����
     * 
     * @param id
     */
    public static void readSyncLock(String id) {
        if (lockPool.containsKey(id)) {
            lockPool.get(id).lock();
        }
    }

    /**
     *  
     * 
     * @param id
     */
    public static void releaseReadSyncLock(String id) {
        if (lockPool.containsKey(id)) {
            lockPool.get(id).unlock();
        }
    }

    /**
     *  
     * 
     * @param id
     */
    public static void releaseSyncLock(String id) {
        if (lockPool.containsKey(id)) {
            lockPool.get(id).unlock();
        }
    }

    /**
     * 
     * @param args
     */
    public static void main(String[] args) {

    }

}
