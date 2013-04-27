/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.brucexx.aries.communication.pkg.conf;

import com.brucexx.aries.communication.pkg.common.SendPackage;

/**
 * ע��ڵ���Ϣ���Ͱ�
 * configServer ���͸�-->centerServer 
 * @author zhao.xiong
 * @version $Id: RegSendPackage.java, v 0.1 2013-4-18 ����4:30:42 zhao.xiong Exp $
 */
public class RegSendPackage extends SendPackage {

    /**  */
    private static final long serialVersionUID = 8005935630471503794L;

    /** ���÷�����Ψһ��ʶ��MAC��ַ   **/
    private String            macAddr;

    public String getMacAddr() {
        return macAddr;
    }

    public void setMacAddr(String macAddr) {
        this.macAddr = macAddr;
    }

}
