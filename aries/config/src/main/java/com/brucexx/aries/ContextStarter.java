/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.brucexx.aries;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.brucexx.aries.context.ConfigContext;
import com.brucexx.aries.server.center.ConfigCenterServer;
import com.brucexx.aries.server.node.ConfigServer;

/**
 * 
 * @author zhao.xiong
 * @version $Id: ContextStarter.java, v 0.1 2013-4-19 下午3:51:41 zhao.xiong Exp $
 */
public class ContextStarter {

    /**
     * 
     * 
     * @param args
     */
    public static void startContext(String args[]) {

        if (!StringUtils.trim(args[0]).equals("aries")
            || (args.length > 2 && (args.length - 1) / 2 != 0)) {
            System.out.println("error params! use :aries --help");
            return;
        }

        if (args.length == 2 && StringUtils.trim(args[0]).equals("aries")
            && StringUtils.trim(args[1]).equals("--help")) {
            System.out.println("aries ");
            System.out
                .println("usage-->aries -conf /home/admin/conf.properties aries -type config ");
            System.out
                .println(" -type config means start node as configServer center means configCenter ");
            System.out
                .println("-conf  filepath : such as --> aries -conf /home/admin/conf.properties  ");

            System.out.println("-type  config/center  : such as --> aries -type config  ");

            return;
        }

        Map<String, String> paramsMap = new HashMap<String, String>();
        for (int i = 1; i < args.length; i = i + 2) {
            paramsMap.put(StringUtils.trim(args[i]), StringUtils.trim(args[i + 1]));
        }

        if (!StringUtils.equals(paramsMap.get("-type"), "config")
            || !StringUtils.equals(paramsMap.get("-type"), "center")) {
            System.out.println("no server type specified!");
            return;
        }
        //开启服务
        start(paramsMap.get("-conf"), paramsMap.get("-type"));

    }

    public static void start(String confPath, String nodeType) {
        File f = new File(confPath);
        if (!f.exists()) {
            System.out.println(confPath + " not exist!");
            return;
        }

        //1.加载配置
        ConfigContext.loadProperties(confPath);
        System.out.println("load [" + confPath + "]finished!");

        //2.启动spring容器
        String[] springConfigs = { "classpath:spring/spring.xml" };
        ClassPathXmlApplicationContext ac = new ClassPathXmlApplicationContext(springConfigs);
        ac.start();

        //3.启动服务器
        if (nodeType.equals("config")) {
            ((ConfigServer) ac.getBean("configServer")).startConfigServer();

        } else if (nodeType.equals("center")) {
            ((ConfigCenterServer) ac.getBean("configCenterServer")).startCenterServer();
        }
    }

    public static void main(String args[]) {
        startContext(args);
    }

}
