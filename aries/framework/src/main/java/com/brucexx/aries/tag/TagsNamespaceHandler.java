/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.brucexx.aries.tag;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;


/**
 * 
 * @author zhao.xiong
 * @version $Id: TagsNamespaceHandler.java, v 0.1 2012-5-24 обнГ01:43:19 zhao.xiong Exp $
 */
public class TagsNamespaceHandler extends NamespaceHandlerSupport {

    /** 
     * @see org.springframework.beans.factory.xml.NamespaceHandler#init()
     */
    @Override
    public void init() {

        registerBeanDefinitionParser("reference", new ReferenceParser());
        registerBeanDefinitionParser("service", new ServiceParser());
    }

}
