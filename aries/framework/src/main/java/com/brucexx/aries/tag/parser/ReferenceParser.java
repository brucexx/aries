/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.brucexx.aries.tag.parser;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.w3c.dom.Element;

import com.brucexx.aries.protocol.adaptor.SOAInfoAdaptor;
import com.brucexx.aries.protocol.factory.SOABeanFactory;
import com.brucexx.aries.tag.model.ReferenceModel;

/**
 * 服务引入解析器
 * @author zhao.xiong
 * @version $Id: ReferenceParser.java, v 0.1 2012-5-24 下午03:38:06 zhao.xiong Exp
 *          $
 */
public class ReferenceParser extends AbstractTagParser {

    public ReferenceParser(BeanFactory beanFactory) {
        super(beanFactory);
    }

    /**
     * 
     * @see org.springframework.beans.factory.xml.AbstractSimpleBeanDefinitionParser#postProcess(org.springframework.beans.factory.support.BeanDefinitionBuilder, org.w3c.dom.Element)
     */
    @Override
    protected void postProcess(BeanDefinitionBuilder beanDefinition, Element element) {
        //1.向
        SOAInfoAdaptor pbc = regAndGet(element);
        if (pbc == null) {
            logger.error("创建服务引用bean[" + element.getAttribute("id") + "]失败!");
        }
        regSpringBean(pbc, beanDefinition);
    }

    /**
     * 获取注册
     * 
     * @param element
     * @return
     */
    protected SOAInfoAdaptor regAndGet(Element element) {

        ReferenceModel rm = new ReferenceModel();
        rm.setId(element.getAttribute("id"));
        rm.setGroupId(element.getAttribute("groupId"));
        rm.setInterface(element.getAttribute("interface"));
        rm.setProtocol(element.getAttribute("protocol"));
        rm.setProperties(getProperties(element));

        logger.info("add Reference Bean Id:" + rm.getId() + ",interface:" + rm.getInterface());
        return ((SOABeanFactory) beanFactory.getBean("soaBeanFactory")).regAndGet(rm);
    }

}
