package com.brucexx.aries.protocol;

import java.io.Serializable;

/**
 * aries framework 客户端 发送包内容,同时也是配置服务器接收内容
 * 
 * @author zhao.xiong
 * @version $Id: SendPackage.java, v 0.1 2013-4-9 下午3:46:09 zhao.xiong Exp $
 */
public class SendPackage implements Serializable {

    /**  */
    private static final long serialVersionUID = 7829638192747834144L;

    /**是否Service/Reference **/
    private boolean           isService        = false;

    /** 协议 **/
    private String     protocol;

    /** service服务注册资源唯一标识id，像 ws一般用接口，mq用topic+eventCode **/
    private String            resourceId;

    /** service分组id,也可以自己定义,让机器在一个定义的组里,ws默认为系统维度从System.getProperties("appName")获取,mq用topic来分隔 **/
    private String            groupId;

    public boolean isService() {
        return isService;
    }

    public void setService(boolean isService) {
        this.isService = isService;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
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

}
