/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.brucexx.aries.tag.parser;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import com.brucexx.aries.protocol.adaptor.SOAInfoAdaptor;
import com.brucexx.aries.protocol.factory.SOABeanFactory;
import com.brucexx.aries.tag.model.ServiceModel;

/**
 * 服务发布解析器
 * @author zhao.xiong
 * @version $Id: ServiceParser.java, v 0.1 2012-5-24 下午03:41:17 zhao.xiong Exp $
 */
public class ServiceParser extends AbstractTagParser {

    private static int n = 0;

    public ServiceParser(BeanFactory beanFactory) {
        super(beanFactory);
    }

    /**
     * 
     * @see org.springframework.beans.factory.xml.AbstractBeanDefinitionParser#resolveId(org.w3c.dom.Element, org.springframework.beans.factory.support.AbstractBeanDefinition, org.springframework.beans.factory.xml.ParserContext)
     */
    @Override
    protected String resolveId(Element element, AbstractBeanDefinition definition,
                               ParserContext parserContext) throws BeanDefinitionStoreException {
        // do nothing
        SOAInfoAdaptor pbc = regAndGet(element);
        if (pbc == null) {
            logger.error("创建发布服务bean[" + element.getAttribute("id") + "]失败!");
        } else {
            logger.info("创建发布服务bean[" + element.getAttribute("id") + "]成功!");
        }
        return pbc.getTagModel().getId();
    }

    private String getId(Element element) {
        if (StringUtils.isEmpty(element.getAttribute("id"))) {
            return element.getAttribute("ref") + n++;
        }
        return element.getAttribute("id");
    }

    protected SOAInfoAdaptor regAndGet(Element element) {

        String id = getId(element);
        ServiceModel sm = new ServiceModel();
        sm.setId(id);
        sm.setGroupId(element.getAttribute("groupId"));
        sm.setRefBean(element.getAttribute("ref"));
        sm.setInterface(element.getAttribute("interface"));
        sm.setProtocol(element.getAttribute("protocol"));

        logger.info("add Service Bean Id:" + sm.getId() + ",interface:" + sm.getInterface());
        return ((SOABeanFactory) beanFactory.getBean("soaBeanFactory")).regAndGet(sm);
    }

}
