/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.brucexx;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 
 * @author zhao.xiong
 * @version $Id: TestTag.java, v 0.1 2012-5-24 ÏÂÎç04:46:19 zhao.xiong Exp $
 */
public class TestTag {

    /**
     * 
     * @param args
     */
    public static void main(String[] args) {

        System.out.println(TestTag.class.getClassLoader().getResource(""));
        
        ApplicationContext ac = new ClassPathXmlApplicationContext(  
        "classpath:spring/springtest.xml");  
        Test test=(Test)ac.getBean("test1");
        System.out.println(test.getName());
    }

}
