/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.brucexx.aries.protocol.adaptor;

import com.brucexx.aries.tag.model.TagModel;

/**
 * 协议bean创建器,用于spring tagbean代理，消除协议化
 * @author zhao.xiong
 * @version $Id: ProtocolBeanCreator.java, v 0.1 2013-4-26 下午3:29:10 zhao.xiong Exp $
 */
public class SOAInfoAdaptor {

    /**客户端或者服务端 **/
    private Object   protocolProcessor;

    /**标签模型数据 **/
    private TagModel tagModel;

    public Object getProtocolProcessor() {
        return protocolProcessor;
    }

    public void setProtocolProcessor(Object protocolProcessor) {
        this.protocolProcessor = protocolProcessor;
    }

    public TagModel getTagModel() {
        return tagModel;
    }

    public void setTagModel(TagModel tagModel) {
        this.tagModel = tagModel;
    }

}
