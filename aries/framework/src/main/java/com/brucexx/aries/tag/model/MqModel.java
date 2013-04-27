/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.brucexx.aries.tag.model;

/**
 * mqÄ£ÐÍ
 * @author zhao.xiong
 * @version $Id: MqModel.java, v 0.1 2013-4-27 ÉÏÎç10:32:58 zhao.xiong Exp $
 */
public class MqModel {

    private String  topic;

    private String  eventCode;

    private boolean persistent = false;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getEventCode() {
        return eventCode;
    }

    public void setEventCode(String eventCode) {
        this.eventCode = eventCode;
    }

    public boolean isPersistent() {
        return persistent;
    }

    public void setPersistent(boolean persistent) {
        this.persistent = persistent;
    }

}
