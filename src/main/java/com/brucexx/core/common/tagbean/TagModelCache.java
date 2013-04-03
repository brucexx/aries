package com.brucexx.core.common.tagbean;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

public class TagModelCache {

    private static Logger                      logger     = Logger.getLogger("aries-config");

    /** **/
    private static Map<String, ReferenceModel> refMap     = new HashMap<String, ReferenceModel>();

    /** **/
    private static Map<String, ServiceModel>   serviceMap = new HashMap<String, ServiceModel>();

    /**
     * 
     * 
     * @param refModel
     */
    public static void putRef(ReferenceModel refModel) {
        if (refMap.get(refModel.getId()) != null) {
            logger.error(refModel + "已经存在");
        }
        refMap.put(refModel.getId(), refModel);
        //通知加载对应的ref组件
    }

    /**
     * 
     * 
     * @param serviceModel
     */
    public static void putService(ServiceModel serviceModel) {
        if (serviceMap.get(serviceModel.getService()) != null) {
            logger.error(serviceModel + "已经存在");
        }
        serviceMap.put(serviceModel.getService(), serviceModel);
    }

}
