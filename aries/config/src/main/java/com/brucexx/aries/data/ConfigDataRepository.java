/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.brucexx.aries.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;

import com.brucexx.aries.communication.enums.AriesProtocol;
import com.brucexx.aries.db.JdbcManager;
import com.brucexx.aries.exception.Assert;
import com.brucexx.aries.exception.ResultCode;
import com.brucexx.aries.util.SystemUtil;

/**
 * 配置中心数据仓库
 * @author zhao.xiong
 * @version $Id: ConfigDataRepository.java, v 0.1 2013-4-9 下午5:20:11 zhao.xiong Exp $
 */
public class ConfigDataRepository {

    private static Logger logger = Logger.getLogger("aries-config");

    private JdbcManager   configDBManager;

    /**
     * 注册serviceInfo,之前需要校验参数
     * 
     * @param serviceInfo
     */
    @SuppressWarnings("unchecked")
    public void regService(ServiceInfo serviceInfo) {

        Assert.notNull(serviceInfo);

        Assert.arrayNotEmpty(ResultCode.NOT_EMPTY, serviceInfo.getGroupId(),
            serviceInfo.getHostIp(), serviceInfo.getProtocol(), serviceInfo.getResourceId());

        AriesProtocol protocol = AriesProtocol.getEnumByCode(serviceInfo.getProtocol());
        Assert.notNull(protocol, ResultCode.ILLEGAL_PROTOCOL);

        String selectCountSql = "select count(*) as _count  from host_info where host_ip=? and resource_id=? and protocol=? and group_id=?";

        Map<String, String> map = configDBManager.preExecuteForMap(selectCountSql,
            serviceInfo.getHostIp(), serviceInfo.getResourceId(), serviceInfo.getProtocol(),
            serviceInfo.getGroupId());

        int c = NumberUtils.toInt(map.get("_count"), 0);
        if (c == 0) {
            configDBManager
                .preExecuteSql(
                    "insert into host_info(resource_id,host_ip,group_id,"
                            + "protocol,config_server_ip,is_alive,gmt_create,gmt_modified) values(?,?,?,?,?,?,sysdate,sysdate)",
                    serviceInfo.getResourceId(), serviceInfo.getHostIp(), serviceInfo.getGroupId(),
                    serviceInfo.getProtocol(), SystemUtil.getHostInfo().getAddress(), true);
        } else {
            //这里可以看出哪些系统重启过，有最后修改时间
            configDBManager
                .preExecuteSql(
                    "update host_info set  gmt_modified=sysdate where resource_id=? and group_id=? and protocol=? and host_ip=?",
                    serviceInfo.getResourceId(), serviceInfo.getGroupId(),
                    serviceInfo.getProtocol(), serviceInfo.getHostIp());
        }

    }

    /**
     * 获取服务service列表
     * 
     * @param groupId
     * @return
     */
    public List<ServiceInfo> getServiceNodeList(String groupId) {
        List<ServiceInfo> serviceList = new ArrayList<ServiceInfo>();
        String selectSql = "select *  from host_info where groupId=?";
        List<Map<String, Object>> list = configDBManager.preExecuteSqlForList(selectSql, groupId);
        for (Map<String, Object> map : list) {
            try {
                ServiceInfo serviceInfo = new ServiceInfo();
                serviceInfo.setGroupId(groupId);
                serviceInfo.setHostIp(StringUtils.defaultString(map.get("host_ip").toString()));
                serviceInfo.setProtocol(StringUtils.defaultString(map.get("protocol").toString()));
                serviceInfo.setResourceId(StringUtils.defaultString(map.get("resource_id")
                    .toString()));

            } catch (Exception e) {
                logger.error("groupId获取数据错误" + map, e);
            }
        }
        return serviceList;
    }
    
    

    public void setConfigDBManager(JdbcManager configDBManager) {
        this.configDBManager = configDBManager;
    }

}
