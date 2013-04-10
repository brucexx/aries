package com.brucexx.aries.protocol;

import java.io.Serializable;
import java.util.List;

/**
 * aries framework �ͻ��˽��հ�����,ͬʱҲ�����÷�������Ӧ����
 * 
 * @author zhao.xiong
 * @version $Id: RecvPackage.java, v 0.1 2013-4-9 ����3:48:17 zhao.xiong Exp $
 */
public class RecvPackage implements Serializable {

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

    /**�Ƿ�ע��ɹ�/��ȡ��Ϣ�ɹ� **/
    private boolean           isSuccess;

    /**configServer�쳣��Ϣ  **/
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
