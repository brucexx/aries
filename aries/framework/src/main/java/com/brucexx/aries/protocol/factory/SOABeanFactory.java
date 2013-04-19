package com.brucexx.aries.protocol.factory;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Component;

import com.brucexx.aries.communication.enums.AriesProtocol;
import com.brucexx.aries.protocol.adaptor.SOAInfoAdaptor;
import com.brucexx.aries.protocol.manager.AriesReferenceManager;
import com.brucexx.aries.protocol.manager.AriesServiceManager;
import com.brucexx.aries.tag.model.ReferenceModel;
import com.brucexx.aries.tag.model.ServiceModel;

@Component("soaBeanFactory")
public class SOABeanFactory implements BeanFactoryAware {

    private static Logger                                    logger     = Logger
                                                                            .getLogger("aries-core");

    private static Map<AriesProtocol, AriesServiceManager>   serviceMap = new HashMap<AriesProtocol, AriesServiceManager>();

    private static Map<AriesProtocol, AriesReferenceManager> refMap     = new HashMap<AriesProtocol, AriesReferenceManager>();

    private BeanFactory                                      beanFactory;

    /**
     * 注册serviceModel
     * 
     * @param serviceModel
     * @return
     */
    public SOAInfoAdaptor regAndGet(ServiceModel serviceModel) {

        AriesProtocol protocol = AriesProtocol.getEnumByCode(serviceModel.getProtocol());
        AriesServiceManager manager = serviceMap.get(protocol);
        if (manager == null) {
            switch (protocol) {
                case WS:
                    manager = (AriesServiceManager) beanFactory.getBean("wsServiceManager");
                    break;
                case MQ:
                    //TODO
                    manager = (AriesServiceManager) beanFactory.getBean("mqServiceManager");
                    break;
                case HESSIAN:
                    manager = (AriesServiceManager) beanFactory.getBean("hessianServiceManager");
                    break;
                case JVM:
                default:
                    manager = (AriesServiceManager) beanFactory.getBean("jvmServiceManager");

            }
            serviceMap.put(protocol, manager);
        }
        SOAInfoAdaptor pbc = null;
        try {
            if ((pbc = manager.getCreator(serviceModel.getId())) != null) {
                return pbc;
            }
            pbc = manager.registerService(serviceModel);

        } catch (Exception e) {
            logger.error("serviceMode:" + serviceModel + "创建失败", e);
        }
        return pbc;
    }

    /**
     * 
     * 
     * @param refModel
     * @return
     */
    public SOAInfoAdaptor regAndGet(ReferenceModel refModel) {
        AriesProtocol protocol = AriesProtocol.getEnumByCode(refModel.getProtocol());
        AriesReferenceManager manager = refMap.get(protocol);
        if (manager == null) {
            switch (protocol) {
                case WS:
                    manager = (AriesReferenceManager) beanFactory.getBean("wsRefManager");
                    break;
                case MQ:
                    manager = (AriesReferenceManager) beanFactory.getBean("mqRefManager");
                    break;
                case HESSIAN:
                    manager = (AriesReferenceManager) beanFactory.getBean("hessianRefManager");
                    break;
                case JVM:
                default:
                    manager = (AriesReferenceManager) beanFactory.getBean("jvmRefManager");
            }

            refMap.put(protocol, manager);
        }
        SOAInfoAdaptor pbc = null;
        try {
            if ((pbc = manager.getCreator(refModel.getId())) != null) {
                return pbc;
            }
            pbc = manager.registerReference(refModel);

        } catch (Exception e) {
            logger.error("refModel:" + refModel + "创建失败", e);
        }
        return pbc;

    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
