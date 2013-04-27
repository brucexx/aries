/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.brucexx.aries.tag.parser;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.AbstractSimpleBeanDefinitionParser;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.brucexx.aries.exception.AriesException;
import com.brucexx.aries.protocol.adaptor.SOAInfoAdaptor;

/**
 * ����ͨ����
 * @author zhao.xiong
 * @version $Id: AbstractParser.java, v 0.1 2013-4-26 ����2:57:17 zhao.xiong Exp $
 */
public abstract class AbstractTagParser extends AbstractSimpleBeanDefinitionParser {

    protected static Logger logger = Logger.getLogger("aries-core");

    protected BeanFactory   beanFactory;

    public AbstractTagParser(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    /**
     *  
     * @see org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser#getBeanClass(org.w3c.dom.Element)
     */
    @Override
    protected Class<?> getBeanClass(Element element) {
        return SOAInfoAdaptor.class;
    }

    /**
     * �������л�ȡproperties����
     * 
     * @param element
     * @return
     */
    protected Map<String, String> getProperties(Element element) {
        Map<String, String> map = new HashMap<String, String>();
        NodeList nodeList = element.getElementsByTagName("props");
        if (nodeList.getLength() > 0) {
            Node propsElement = nodeList.item(0);
            NodeList propList = propsElement.getChildNodes();
            for (int i = 0; i < propList.getLength(); i++) {
                Node propNode = propList.item(i);
                NamedNodeMap nnMap = propNode.getAttributes();
                if (nnMap != null
                    && StringUtils.isNotEmpty(nnMap.getNamedItem("key").getNodeValue())) {
                    map.put(nnMap.getNamedItem("key").getNodeValue(),
                        StringUtils.trim(propNode.getTextContent()));
                }
            }
        }
        return map;
    }

    /**
     * ע��
     * 
     * @param creator
     * @param beanDefinition
     */
    protected void regSpringBean(SOAInfoAdaptor creator, BeanDefinitionBuilder beanDefinition) {
        //����Ӧ��bean������spring������,������Ҫ��֤processor�Ľӿ�һ����ע����
        Class<?> interfaceClz = null;
        try {
            interfaceClz = Class.forName(creator.getTagModel().getInterface());
        } catch (ClassNotFoundException e) {
            logger.error("�޷��ҵ�" + creator.getTagModel().getInterface() + "��Ӧ������", e);
            return;
        }

        if (interfaceClz.isAssignableFrom(creator.getProtocolProcessor().getClass())) {

            ((DefaultListableBeanFactory) beanFactory).registerSingleton(creator.getTagModel()
                .getId(), creator.getProtocolProcessor());
            logger.info("��springע��bean>>" + creator.getTagModel().getId());

        } else {
            throw new AriesException("��������" + creator.getProtocolProcessor().getClass().getName()
                                     + "����" + interfaceClz.getName() + "��ʵ���࣡");
        }

    }

}
