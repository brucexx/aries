/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.brucexx.aries.protocol.adaptor;

import com.brucexx.aries.tag.model.TagModel;

/**
 * Э��bean������,����spring tagbean��������Э�黯
 * @author zhao.xiong
 * @version $Id: ProtocolBeanCreator.java, v 0.1 2013-4-26 ����3:29:10 zhao.xiong Exp $
 */
public class SOAInfoAdaptor {

    /**�ͻ��˻��߷���� **/
    private Object   protocolProcessor;

    /**��ǩģ������ **/
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
