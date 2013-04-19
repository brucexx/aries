/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.brucexx.aries.db;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import com.brucexx.aries.context.ConfigContext;
import com.brucexx.aries.protocol.CommonConstants;
import com.brucexx.aries.server.ConfigServerEnum;

/**
 * 数据源管理器
 * @author zhao.xiong
 * @version $Id: DataSourceManager.java, v 0.1 2013-4-19 下午2:48:47 zhao.xiong Exp $
 */
@Component("dataSourceManager")
public class DataSourceManager implements InitializingBean, DisposableBean {

    private static Logger                logger = Logger.getLogger("config-dal");

    /** 这里暂时只支持注册单个数据源 **/
    private Map<String, BasicDataSource> dsMap  = new HashMap<String, BasicDataSource>();

    /** 
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        //从configContext来读取配置
        if (StringUtils.isEmpty(ConfigContext.get(ConfigServerEnum.DS_PWD))
            || StringUtils.isEmpty(ConfigContext.get(ConfigServerEnum.DS_URL))
            || StringUtils.isEmpty(ConfigContext.get(ConfigServerEnum.DS_USER))) {
            logger.error("数据源未配置，启动失败!");
            return;
        }
        //注册ds
        BasicDataSource bds = new BasicDataSource();
        bds.setUrl(ConfigContext.get(ConfigServerEnum.DS_URL));
        bds.setUsername(ConfigContext.get(ConfigServerEnum.DS_USER));
        bds.setPassword(ConfigContext.get(ConfigServerEnum.DS_PWD));
        bds.setDriverClassName("com.mysql.jdbc.Driver");
        dsMap.put(CommonConstants.DS_KEY, bds);
    }

    /**
     * 
     * 
     * @return
     */
    public BasicDataSource getMainDS() {
        return dsMap.get(CommonConstants.DS_KEY);
    }

    /**
     * 根据key获取ds
     * 
     * @param key
     * @return
     */
    public BasicDataSource geDS(String key) {
        return dsMap.get(key);
    }

    /** 
     * @see org.springframework.beans.factory.DisposableBean#destroy()
     */
    @Override
    public void destroy() throws Exception {
        for (BasicDataSource bds : dsMap.values()) {
            try {
                bds.close();
            } catch (Exception e) {
                logger.error(bds + "关闭失败!", e);
            }

        }
    }
}
