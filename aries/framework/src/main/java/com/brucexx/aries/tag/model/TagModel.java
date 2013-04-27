/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.brucexx.aries.tag.model;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * ��ǩmode,ֻ��������ʶ
 * @author zhao.xiong
 * @version $Id: TagModel.java, v 0.1 2013-4-26 ����3:49:15 zhao.xiong Exp $
 */
public class TagModel {
    /**Ψһ��ʶ **/
    private String              id;

    /** ����groupId**/
    private String              groupId;

    /**Э��  **/
    private String              protocol;

    /**�ӿ���,һ�㶨��Ϊ��Դ **/
    private String              _interface;

    /** mq��Ϣ **/
    private MqModel             mqInfo;

    /** ���Լ�**/
    private Map<String, String> properties = new HashMap<String, String>();

    public String getInterface() {
        return _interface;
    }

    public void setInterface(String _interface) {
        this._interface = _interface;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public MqModel getMqInfo() {
        return mqInfo;
    }

    public void setMqInfo(MqModel mqInfo) {
        this.mqInfo = mqInfo;
    }

    /** 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
