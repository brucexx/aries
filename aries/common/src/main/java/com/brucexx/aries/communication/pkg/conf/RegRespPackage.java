/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.brucexx.aries.communication.pkg.conf;

import com.brucexx.aries.communication.pkg.common.RecvPackage;

/**
 * configServer�ڵ�ע�ᵽconfigServerManager����Ӧ��ͨ�ű���
 * @author zhao.xiong
 * @version $Id: NodeRegPackage.java, v 0.1 2013-4-11 ����5:02:18 zhao.xiong Exp $
 */
public class RegRespPackage extends RecvPackage {

    /**  */
    private static final long serialVersionUID = 2451740169267558544L;
    /** ���÷�����Ψһ��ʶ��MAC��ַ   **/
    private String            configServiceId;

    public String getConfigServiceId() {
        return configServiceId;
    }

    public void setConfigServiceId(String configServiceId) {
        this.configServiceId = configServiceId;
    }

}
