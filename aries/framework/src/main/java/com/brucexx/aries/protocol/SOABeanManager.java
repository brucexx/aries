package com.brucexx.aries.protocol;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

import com.brucexx.aries.communication.enums.AriesProtocol;
import com.brucexx.aries.tag.ReferenceModel;
import com.brucexx.aries.tag.ServiceModel;

public class SOABeanManager implements BeanFactoryAware {

    private static Logger                                    logger     = Logger
                                                                            .getLogger("aries-core");

    private static Map<AriesProtocol, AriesServiceManager>   serviceMap = new HashMap<AriesProtocol, AriesServiceManager>();

    private static Map<AriesProtocol, AriesReferenceManager> refMap     = new HashMap<AriesProtocol, AriesReferenceManager>();

    private BeanFactory                                      beanFactory;

    public void loadService(ServiceModel serviceModel) {
        AriesProtocol protocol = AriesProtocol.getEnumByCode(serviceModel.getProtocol());
        AriesServiceManager manager = serviceMap.get(protocol);
        if (manager == null) {
            switch (protocol) {
                case WS:
                    manager = (AriesServiceManager) beanFactory.getBean("wsServiceManager");
                    break;
                case MQ:
                    //TODO
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
        try {
            manager.registerService(serviceModel);
        } catch (Exception e) {
            logger.error("serviceModel" + serviceModel + "×¢²áÊ§°Ü", e);
        }
    }

    public void loadRef(ReferenceModel refModel) {
        AriesProtocol protocol = AriesProtocol.getEnumByCode(refModel.getProtocol());
        AriesReferenceManager manager = refMap.get(protocol);
        if (manager == null) {
            switch (protocol) {
                case WS:
                    manager = (AriesReferenceManager) beanFactory.getBean("wsRefManager");
                    break;
                case MQ:
                    //TODO
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
        try {
            manager.registerReference(refModel);
        } catch (Exception e) {
            logger.error("refModel:" + refModel + "×¢²áÊ§°Ü", e);
        }

    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
