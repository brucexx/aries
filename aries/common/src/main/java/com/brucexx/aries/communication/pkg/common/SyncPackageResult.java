/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.brucexx.aries.communication.pkg.common;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * ͬ�����ؽ��
 * @author zhao.xiong
 * @version $Id: SynPackageResult.java, v 0.1 2013-4-18 ����1:40:56 zhao.xiong Exp $
 */
public class SyncPackageResult {

    /**��Ӧ�� **/
    private RecvPackage recvPackage;

    private boolean     isTimeout;

    public SyncPackageResult(RecvPackage recvPackage) {
        this.recvPackage = recvPackage;
    }

    public boolean isTimeout() {
        return isTimeout;
    }

    public void setTimeout(boolean isTimeout) {
        this.isTimeout = isTimeout;
    }

    public RecvPackage getRecvPackage() {
        return recvPackage;
    }

    public void setRecvPackage(RecvPackage recvPackage) {
        this.recvPackage = recvPackage;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
