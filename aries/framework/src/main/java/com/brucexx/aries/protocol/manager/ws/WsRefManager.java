package com.brucexx.aries.protocol.manager.ws;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.codehaus.xfire.XFireFactory;
import org.codehaus.xfire.client.XFireProxyFactory;
import org.codehaus.xfire.service.Service;
import org.codehaus.xfire.service.binding.ObjectServiceFactory;

import com.brucexx.aries.protocol.adaptor.SOAInfoAdaptor;
import com.brucexx.aries.protocol.manager.AbstractReferenceManager;
import com.brucexx.aries.tag.model.ReferenceModel;

public class WsRefManager extends AbstractReferenceManager {

    /**ip地址池,key为resourceId,value为ip地址池   **/
    private Map<String, Set<String>> ipMap = new HashMap<String, Set<String>>();

    /**
     * 
     * @see com.brucexx.aries.protocol.manager.AbstractReferenceManager#setProcessor(com.brucexx.aries.protocol.adaptor.SOAInfoAdaptor)
     */
    @Override
    protected void setProcessor(SOAInfoAdaptor pbc) {
        logger.info("引用webservice服务==>" + pbc.getTagModel());
 
        
        ReferenceModel model = (ReferenceModel) pbc.getTagModel();
        try {
            Object client = createService(Class.forName(model.getInterface()),
                model.getProperties());
            //设置client
            pbc.setProtocolProcessor(client);

        } catch (Exception e) {
            logger.error("refenceModel" + model + "创建ws客户端失败!");
        }
    }

    /**
     * 根据URL手动创建XFIRE链接
     * 
     * @param <T>
     * @param clazz 接口类
     * @param 属性
     * @param url 服务端URL
     * @return
     * @throws MalformedURLException 
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public <T> T createService(Class<T> clazz, Map<String, String> properties, String url)
                                                                                          throws MalformedURLException {
        Service service = new ObjectServiceFactory().create(clazz);
        setXfireProperty(service, properties);
        XFireProxyFactory factory = new XFireProxyFactory(XFireFactory.newInstance().getXFire());
        logger.info("成功创建XFIRE客户端:properties" + properties);
        return (T) factory.create(service, url);
    }

    /**
     * 设置XFIRE参数信息
     * 
     * @param service
     * @param timeout
     */
    private void setXfireProperty(Service service, Map<String, String> properties) {
        // 等待服务端返回超时时间
        service.setProperty("http.timeout", properties.get("ws.http.timeout"));
        //等待SOCKET链接建立超时时间
        service
            .setProperty("http.connection.timeout", properties.get("ws.http.connection.timeout"));
        // 连接到单个服务器的连接数上限
        service.setProperty("max.connections.per.host",
            properties.get("ws.max.connections.per.host"));
        // 连接到所有服务器的连接个数上限
        service.setProperty("max.total.connections", properties.get("ws.max.total.connections"));
        // HTTP底层失败重试次数
        service.setProperty("http.retry.count", properties.get("ws.http.retry.count"));

        //把其它的属性补进去,暂时不开启
        //        for (Map.Entry<String, String> entry : properties.entrySet()) {
        //            if (StringUtils.startsWithIgnoreCase(entry.getKey(), "ws.")) {
        //                service.setProperty(
        //                    StringUtils.substring(entry.getKey(),
        //                        StringUtils.indexOf(entry.getKey(), "ws.") + 3), entry.getValue());
        //            }
        //        }
    }

}
