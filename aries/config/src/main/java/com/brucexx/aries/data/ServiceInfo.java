/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.brucexx.aries.data;

/**
 * 
 * @author zhao.xiong
 * @version $Id: ServiceInfo.java, v 0.1 2013-4-9 ÏÂÎç5:36:09 zhao.xiong Exp $
 */
public class ServiceInfo {

    private String hostIp;

    private String resourceId;

    private String groupId;

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

}
