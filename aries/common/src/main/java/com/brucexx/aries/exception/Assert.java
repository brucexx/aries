/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.brucexx.aries.exception;

import java.util.Collection;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

/**
 * 公用的Assert
 * 
 * @author zhao.xiong
 * @version $Id: Assert.java, v 0.1 2012-11-22 下午07:07:46 lei.wl Exp $
 */
public class Assert extends org.springframework.util.Assert {

    /** logger */
    private static Logger      logger    = Logger.getLogger("aries-common");
    /**分割符 **/
    public static final String DELIMITER = ":";

    /**
     * assert null object
     *
     * @param obj
     * @param resultCode
     * @throws SignException
     */
    public static void notNull(Object obj, ResultCode resultCode) throws AriesException {
        if (obj == null) {
            fail(resultCode);
        }
    }

    /**
     * 
     * 
     * @param obj
     * @param resultCode
     * @param fieldVar 字段变量名
     * @throws AriesException
     */
    @SuppressWarnings("rawtypes")
    public static void notNull(Object obj, ResultCode resultCode, Class clz) throws AriesException {
        if (obj == null) {
            fail(resultCode.getCode(), resultCode.getMessage() + DELIMITER + clz.getSimpleName());
        }
    }

    /**
     *
     * @param obj
     * @param resultCode
     * @param desc
     * @throws AriesException
     */
    public static void notNull(Object obj, ResultCode resultCode, String desc)
                                                                              throws AriesException {
        if (obj == null) {
            fail(resultCode.getCode(), desc);
        }
    }

    /**
     * assert null objects
     * 
     * @param resultCode
     * @param objects
     */
    public static void arrayNotNull(ResultCode resultCode, Object... objects) {
        for (Object o : objects) {
            notNull(o, resultCode);
        }
    }

    /**
     * assert empty string
     *
     * @param string
     * @param resultCode
     * @throws SignException
     */
    public static void notEmpty(String string, ResultCode resultCode) throws AriesException {
        if (StringUtils.isBlank(string)) {
            fail(resultCode);
        }
    }

    /**
     * assert empty string
     *
     * @param string
     * @param resultCode
     * @throws SignException
     */
    public static void fieldNotEmpty(String string, ResultCode resultCode, String fieldVar)
                                                                                           throws AriesException {
        notEmpty(string, resultCode, resultCode.getMessage() + DELIMITER + fieldVar);
    }

    /**
     *  
     * 
     * @param string
     * @param code
     * @param desc
     * @throws AriesException
     */
    public static void notEmpty(String string, ResultCode resultCode, String desc)
                                                                                  throws AriesException {
        if (StringUtils.isEmpty(string)) {
            fail(resultCode.getCode(), desc);
        }
    }

    /**
     * assert empty string
     * 
     * @param resultCode
     * @param objects
     */
    public static void arrayNotEmpty(ResultCode resultCode, String... stringArray) {
        for (String str : stringArray) {
            notEmpty(str, resultCode);
        }
    }

    /**
     * map字段中不为空
     * 
     * @param resultCode
     * @param map
     * @param fields
     */
    public static void mapFieldNotEmpty(ResultCode resultCode, Map<String, String> map,
                                        String... fields) {
        for (String fieldVar : fields) {
            if (StringUtils.isEmpty(map.get(fieldVar))) {
                fail(resultCode.getCode(), resultCode.getMessage() + DELIMITER + fieldVar);
            }
        }
    }

    /**
     * assert true
     *
     * @param expression
     * @param resultCode
     * @throws SignException
     */
    public static void isTrue(boolean expression, ResultCode resultCode) throws AriesException {
        if (!expression) {
            fail(resultCode);
        }
    }

    /**
     * assert false
     *
     * @param expression
     * @param resultCode
     * @throws SignException
     */
    public static void isFalse(boolean expression, ResultCode resultCode) throws AriesException {
        if (expression) {
            fail(resultCode);
        }
    }

    /**
     * assert collection not empty
     * 
     * @param collection
     * @param resultCode
     */
    public static void notEmpty(Collection<?> collection, ResultCode resultCode) {
        if (CollectionUtils.isEmpty(collection)) {
            fail(resultCode);
        }
    }

    /**
     * assert array not empty
     * 
     * @param array
     * @param resultCode
     */
    public static void notEmpty(Object[] array, ResultCode resultCode) {
        if (ObjectUtils.isEmpty(array)) {
            fail(resultCode);
        }
    }

    /**
     * force to throw exception
     *
     * @param resultCode
     * @throws SignException
     */
    public static void fail(ResultCode resultCode) throws AriesException {
        logger.warn("Assert异常" + resultCode);
        throw new AriesException(resultCode);
    }

    /**
     * 
     * 
     * @param code
     * @param desc
     */
    public static void fail(String code, String desc) {
        throw new AriesException(code, desc);
    }

}
