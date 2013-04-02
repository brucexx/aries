/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.brucexx.springtag;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSimpleBeanDefinitionParser;
import org.w3c.dom.Element;

import com.brucexx.core.common.tagbean.ReferenceModel;

/**
 * 
 * @author zhao.xiong
 * @version $Id: ReferenceParser.java, v 0.1 2012-5-24 ÏÂÎç03:38:06 zhao.xiong Exp
 *          $
 */
public class ReferenceParser extends AbstractSimpleBeanDefinitionParser {

    private static Logger                logger = Logger.getLogger("aries-config");

    private static T Map<String, Class<T>> clzMap = new HashMap<String, Class<T>>();

    @SuppressWarnings("rawtypes")
    protected Class getBeanClass(Element element) {
        String _interface = element.getAttribute("interface");
        try {
            return Class.forName(_interface);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    protected void postProcess(BeanDefinitionBuilder beanDefinition, Element element) {

        String id = element.getAttribute("id");
        String _interface = element.getAttribute("interface");
        String protocol = element.getAttribute("protocol");

        System.out.println("id=" + id + ",interface=" + _interface + ",protocol=" + protocol);
    }

}
