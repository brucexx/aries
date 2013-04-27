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

    /**ip��ַ��,keyΪresourceId,valueΪip��ַ��   **/
    private Map<String, Set<String>> ipMap = new HashMap<String, Set<String>>();

    /**
     * 
     * @see com.brucexx.aries.protocol.manager.AbstractReferenceManager#setProcessor(com.brucexx.aries.protocol.adaptor.SOAInfoAdaptor)
     */
    @Override
    protected void setProcessor(SOAInfoAdaptor pbc) {
        logger.info("����webservice����==>" + pbc.getTagModel());
 
        
        ReferenceModel model = (ReferenceModel) pbc.getTagModel();
        try {
            Object client = createService(Class.forName(model.getInterface()),
                model.getProperties());
            //����client
            pbc.setProtocolProcessor(client);

        } catch (Exception e) {
            logger.error("refenceModel" + model + "����ws�ͻ���ʧ��!");
        }
    }

    /**
     * ����URL�ֶ�����XFIRE����
     * 
     * @param <T>
     * @param clazz �ӿ���
     * @param ����
     * @param url �����URL
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
        logger.info("�ɹ�����XFIRE�ͻ���:properties" + properties);
        return (T) factory.create(service, url);
    }

    /**
     * ����XFIRE������Ϣ
     * 
     * @param service
     * @param timeout
     */
    private void setXfireProperty(Service service, Map<String, String> properties) {
        // �ȴ�����˷��س�ʱʱ��
        service.setProperty("http.timeout", properties.get("ws.http.timeout"));
        //�ȴ�SOCKET���ӽ�����ʱʱ��
        service
            .setProperty("http.connection.timeout", properties.get("ws.http.connection.timeout"));
        // ���ӵ�����������������������
        service.setProperty("max.connections.per.host",
            properties.get("ws.max.connections.per.host"));
        // ���ӵ����з����������Ӹ�������
        service.setProperty("max.total.connections", properties.get("ws.max.total.connections"));
        // HTTP�ײ�ʧ�����Դ���
        service.setProperty("http.retry.count", properties.get("ws.http.retry.count"));

        //�����������Բ���ȥ,��ʱ������
        //        for (Map.Entry<String, String> entry : properties.entrySet()) {
        //            if (StringUtils.startsWithIgnoreCase(entry.getKey(), "ws.")) {
        //                service.setProperty(
        //                    StringUtils.substring(entry.getKey(),
        //                        StringUtils.indexOf(entry.getKey(), "ws.") + 3), entry.getValue());
        //            }
        //        }
    }

}
