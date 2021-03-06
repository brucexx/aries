/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.brucexx.aries;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 
 * @author zhao.xiong
 * @version $Id: TestTag.java, v 0.1 2012-5-24 ����04:46:19 zhao.xiong Exp $
 */
public class TestTag {

    /**
     * 
     * @param args
     */
    public static void main(String[] args) {
        String[] springConfigs = { "classpath:META-INF/spring/springtest-ref.xml",
                "classpath:META-INF/spring/springtest-service.xml" };
        ApplicationContext ac = new ClassPathXmlApplicationContext(springConfigs);
        DhService refer = (DhService) ac.getBean("myService");
        System.out.println(refer.sk("reference 1--->"));

        System.out.println(refer.sk("reference2 --->"));

        DhService service = (DhService) ac.getBean("dhService");
        System.out.println(service.sk("service --->"));
    }

}
