/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package core.common.enums;

/**
 * 
 * @author zhao.xiong
 * @version $Id: AriesProtocol.java, v 0.1 2012-8-10 ����03:48:27 zhao.xiong Exp $
 */
public enum AriesProtocol {

    TCP,

    HESSIAN,

    WS;

    /**
     * ����ö�ٴ��룬��ȡö��
     * 
     * @param code
     * @return
     */
    public static AriesProtocol getEnumByCode(String code) {
        for (AriesProtocol protocol : AriesProtocol.values()) {
            if (protocol.getCode().equalsIgnoreCase(code)) {
                return protocol;
            }
        }

        return WS;
    }

    /**
     * 
     * @return
     */
    public String getCode() {
        return this.name();
    }

}
