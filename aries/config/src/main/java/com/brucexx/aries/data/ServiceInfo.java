/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.brucexx.aries.data;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 注册服务信息列表
 * @author zhao.xiong
 * @version $Id: ServiceInfo.java, v 0.1 2013-4-9 下午5:36:09 zhao.xiong Exp $
 */
public class ServiceInfo {

    /**来源主机ip **/
    private String hostIp;

    /**资源id 比如com.brucexx.aries.xx 接口类名 ,或者是topic+eventCode**/
    private String resourceId;

    /**分组id **/
    private String groupId;

    /**使用协议 **/
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
     * 获取服务provider的唯一标识
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
