package com.brucexx.aries.protocol;

import java.io.Serializable;

/**
 * aries framework �ͻ��� ���Ͱ�����,ͬʱҲ�����÷�������������
 * 
 * @author zhao.xiong
 * @version $Id: SendPackage.java, v 0.1 2013-4-9 ����3:46:09 zhao.xiong Exp $
 */
public class SendPackage implements Serializable {

    /**  */
    private static final long serialVersionUID = 7829638192747834144L;

    /**�Ƿ�Service/Reference **/
    private boolean           isService        = false;

    /** Э�� **/
    private String     protocol;

    /** service����ע����ԴΨһ��ʶid���� wsһ���ýӿڣ�mq��topic+eventCode **/
    private String            resourceId;

    /** service����id,Ҳ�����Լ�����,�û�����һ�����������,wsĬ��Ϊϵͳά�ȴ�System.getProperties("appName")��ȡ,mq��topic���ָ� **/
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
