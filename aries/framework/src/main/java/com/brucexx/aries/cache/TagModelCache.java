package com.brucexx.aries.cache;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.brucexx.aries.tag.model.ReferenceModel;
import com.brucexx.aries.tag.model.ServiceModel;

public class TagModelCache {

    private static Logger                      logger     = Logger.getLogger("aries-core");

    /** �ͻ��� keyΪid**/
    private static Map<String, ReferenceModel> refMap     = new HashMap<String, ReferenceModel>();

    /**  **/
    private static Map<String, ServiceModel>   serviceMap = new HashMap<String, ServiceModel>();

    /**
     * 
     * 
     * @param refModel
     */
    public static void putRef(ReferenceModel refModel) {
        if (refMap.get(refModel.getId()) != null) {
            logger.error(refModel + "�Ѿ�����");
        }
        refMap.put(refModel.getId(), refModel);
        //֪ͨ���ض�Ӧ��ref���
    }

    public static ServiceModel getService(String serviceId) {
        return serviceMap.get(serviceId);
    }

    public static ReferenceModel getRef(String refId) {
        return refMap.get(refId);
    }

    /**
     * 
     * 
     * @param serviceModel
     */
    public static void putService(ServiceModel serviceModel) {
        if (serviceMap.get(serviceModel.getId()) != null) {
            logger.error(serviceModel + "�Ѿ�����");
        }
        serviceMap.put(serviceModel.getId(), serviceModel);
    }

}
