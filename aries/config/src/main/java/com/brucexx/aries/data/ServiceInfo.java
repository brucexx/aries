/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.brucexx.aries.data;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * ע�������Ϣ�б�
 * @author zhao.xiong
 * @version $Id: ServiceInfo.java, v 0.1 2013-4-9 ����5:36:09 zhao.xiong Exp $
 */
public class ServiceInfo {

    /**��Դ����ip **/
    private String hostIp;

    /**��Դid ����com.brucexx.aries.xx �ӿ����� ,������topic+eventCode**/
    private String resourceId;

    /**����id **/
    private String groupId;

    /**ʹ��Э�� **/
    private String protocol;

    public String getHostIp() {
        return hostIp;
    }

    public void setHostIp(String hostIp) {
        this.hostIp = hostIp;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
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

    /**
     * ��ȡ����provider��Ψһ��ʶ
     * @return
     */
    public String getKey() {
        return protocol + "@" + groupId + "@" + resourceId + "@" + hostIp;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
