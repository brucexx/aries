package com.brucexx.aries.protocol;

import java.io.Serializable;
import java.util.List;

/**
 * aries framework 客户端接收包内容,同时也是配置服务器响应内容
 * 
 * @author zhao.xiong
 * @version $Id: RecvPackage.java, v 0.1 2013-4-9 下午3:48:17 zhao.xiong Exp $
 */
public class RecvPackage implements Serializable {

    /**  */
    private static final long serialVersionUID = 7466246480324894134L;

    /**是否Service/Reference **/
    private boolean           isService        = false;

    /**节点信息 **/
    private List<String>      nodeInfoList;

    /**资源唯一标识 **/
    private String            resourceId;

    /** 协议 **/
    private String            protocol;

    /**定义组ID **/
    private String            groupId;

    /**是否注册成功/获取信息成功 **/
    private boolean           isSuccess;

    /**configServer异常信息  **/
    private Exception         serverException;

    public boolean isService() {
        return isService;
    }

    public void setService(boolean isService) {
        this.isService = isService;
    }

    public List<String> getNodeInfoList() {
        return nodeInfoList;
    }

    public void setNodeInfoList(List<String> nodeInfoList) {
        this.nodeInfoList = nodeInfoList;
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

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public Exception getServerException() {
        return serverException;
    }

    public void setServerException(Exception serverException) {
        this.serverException = serverException;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

}
