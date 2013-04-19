package com.brucexx.aries.tag.model;

import java.util.HashMap;
import java.util.Map;

/**
 * service模型
 * 
 * @author zhao.xiong
 * @version $Id: ServiceModel.java, v 0.1 2013-4-26 下午1:16:41 zhao.xiong Exp $
 */
public class ServiceModel extends TagModel {

    /**引用的bean **/
    private String               refBean;

    /** mq map **/
    private Map<String, MqModel> mqMap = new HashMap<String, MqModel>();

    public Map<String, MqModel> getMqMap() {
        return mqMap;
    }

    public void setMqMap(Map<String, MqModel> mqMap) {
        this.mqMap = mqMap;
    }

    public String getRefBean() {
        return refBean;
    }

    public void setRefBean(String refBean) {
        this.refBean = refBean;
    }

}
