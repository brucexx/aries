/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.brucexx.aries;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 
 * @author zhao.xiong
 * @version $Id: ConfigServerStarter.java, v 0.1 2013-4-11 обнГ1:37:17 zhao.xiong Exp $
 */
public class ConfigServerStarter {

    /**
     * 
     * @param args
     */
    public static void main(String[] args) {

        String[] springConfigs = { "classpath:spring/spring.xml" };
        ApplicationContext ac = new ClassPathXmlApplicationContext(springConfigs);
        
    }

}
