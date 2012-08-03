/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.brucexx.springtag;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSimpleBeanDefinitionParser;
import org.w3c.dom.Element;

import com.brucexx.tagbean.ReferenceTagBean;

/**
 * 
 * @author zhao.xiong
 * @version $Id: ReferenceParser.java, v 0.1 2012-5-24 ����03:38:06 zhao.xiong Exp
 *          $
 */
public class ReferenceParser extends AbstractSimpleBeanDefinitionParser {

	@SuppressWarnings("rawtypes")
	protected Class getBeanClass(Element element) {
		return ReferenceTagBean.class;
	}

	protected void postProcess(BeanDefinitionBuilder beanDefinition,
			Element element) {

		String id = element.getAttribute("id");
		String _interface = element.getAttribute("interface");
		String protocol = element.getAttribute("protocol");

		System.out.println("id=" + id + ",interface=" + _interface
				+ ",protocol=" + protocol);
	}

}
