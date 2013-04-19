/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.brucexx.aries.tag;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

import com.brucexx.aries.tag.parser.ReferenceParser;
import com.brucexx.aries.tag.parser.ServiceParser;

/**
 * 
 * @author zhao.xiong
 * @version $Id: TagsNamespaceHandler.java, v 0.1 2012-5-24 ÏÂÎç01:43:19 zhao.xiong Exp $
 */
public class TagsNamespaceHandler extends NamespaceHandlerSupport implements BeanFactoryAware {

    private BeanFactory beanFactory;

    /** 
     * @see org.springframework.beans.factory.xml.NamespaceHandler#init()
     */
    @Override
    public void init() {

        registerBeanDefinitionParser("reference", new ReferenceParser(beanFactory));
        registerBeanDefinitionParser("service", new ServiceParser(beanFactory));
    }

    /** 
     * @see org.springframework.beans.factory.BeanFactoryAware#setBeanFactory(org.springframework.beans.factory.BeanFactory)
     */
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

}
