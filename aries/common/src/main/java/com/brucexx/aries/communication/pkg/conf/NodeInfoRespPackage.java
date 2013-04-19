/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.brucexx.aries.communication.pkg.conf;

import java.util.Set;

import com.brucexx.aries.communication.pkg.common.RecvPackage;

/**
 * 节点信息响应包
 * @author zhao.xiong
 * @version $Id: NodeInfoRespPackage.java, v 0.1 2013-4-24 下午1:54:51 zhao.xiong Exp $
 */
public class NodeInfoRespPackage extends RecvPackage {

    /**  */
    private static final long serialVersionUID = -1034655118701419904L;

    /**获取在注册中心的配置服务器列表 **/
    private Set<String>       confServerSet;

    public Set<String> getConfServerSet() {
        return confServerSet;
    }

    public void setConfServerSet(Set<String> confServerSet) {
        this.confServerSet = confServerSet;
    }

}
