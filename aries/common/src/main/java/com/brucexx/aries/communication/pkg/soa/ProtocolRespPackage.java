package com.brucexx.aries.communication.pkg.soa;

import java.util.List;

import com.brucexx.aries.communication.pkg.common.RecvPackage;

/**
 * aries framework Э�齻����Ӧ����
 * 
 * @author zhao.xiong
 * @version $Id: RecvPackage.java, v 0.1 2013-4-9 ����3:48:17 zhao.xiong Exp $
 */
public class ProtocolRespPackage extends RecvPackage {

    /**  */
    private static final long serialVersionUID = 7466246480324894134L;

    /**�Ƿ�Service/Reference **/
    private boolean           isService        = false;

    /**�ڵ���Ϣ **/
    private List<String>      nodeInfoList;

    /**��ԴΨһ��ʶ **/
    private String            resourceId;

    /** Э�� **/
    private String            protocol;

    /**������ID **/
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
