package com.brucexx.aries.communication.pkg.soa;

import java.util.List;

import com.brucexx.aries.communication.pkg.common.RecvPackage;

/**
 * aries framework 协议交互响应包，
 * 
 * @author zhao.xiong
 * @version $Id: RecvPackage.java, v 0.1 2013-4-9 下午3:48:17 zhao.xiong Exp $
 */
public class ProtocolRespPackage extends RecvPackage {

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

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

}
