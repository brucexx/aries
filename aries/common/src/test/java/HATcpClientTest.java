import java.util.HashMap;
import java.util.Map;

/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */

/**
 * 
 * @author zhao.xiong
 * @version $Id: HATcpClientTest.java, v 0.1 2013-4-18 обнГ4:23:36 zhao.xiong Exp $
 */
public class HATcpClientTest {

    /**
     * 
     * @param args
     */
    public static void main(String[] args) {

        Map<String, String> map = new HashMap<String, String>();
        map.put("123", "222");
        String v = "";
        try {
            v = map.get("123");
        } finally {
            map.remove("123");
        }
        System.out.println(v);

    }

}
