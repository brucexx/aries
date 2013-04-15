package com.brucexx.aries.util;

import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Formatter;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

/**
 * ȡ���й�ϵͳ��Ϣ�Ĺ����ࡣ
 * 
 * <p>
 * �����ΪJava��ȫ��ԭ�������ȡ��<code>System.getProperty</code>������Ӧ���ֶν������ó�<code>null</code>��
 * ���������Ϣ���������<code>System.err</code>�С�
 * </p>
 *
 * @author Michael Zhou
 * @version $Id: SystemUtil.java 1130 2004-08-02 08:23:19Z baobao $
 */
public class SystemUtil {
    private static final JvmSpecInfo     JVM_SPEC_INFO     = new JvmSpecInfo();
    private static final JvmInfo         JVM_INFO          = new JvmInfo();
    private static final JavaSpecInfo    JAVA_SPEC_INFO    = new JavaSpecInfo();
    private static final JavaInfo        JAVA_INFO         = new JavaInfo();
    private static final OsInfo          OS_INFO           = new OsInfo();
    private static final UserInfo        USER_INFO         = new UserInfo();
    private static final HostInfo        HOST_INFO         = new HostInfo();
    private static final JavaRuntimeInfo JAVA_RUNTIME_INFO = new JavaRuntimeInfo();

    public static final String getMAC() {

        try {
            InetAddress address = InetAddress.getLocalHost();
            NetworkInterface ni = NetworkInterface.getByInetAddress(address);

            byte[] mac = ni.getHardwareAddress();
            if (mac == null) {
                mac = ni.getInetAddresses().nextElement().getAddress();
            }

            String sMAC = "";

            for (int i = 0; i < mac.length; i++) {
                sMAC = new Formatter().format(Locale.getDefault(), "%02X%s", mac[i],
                    (i < mac.length - 1) ? "-" : "").toString();
            }

            return sMAC;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * ȡ��Java Virtual Machine Specification����Ϣ��
     *
     * @return <code>JvmSpecInfo</code>����
     */
    public static final JvmSpecInfo getJvmSpecInfo() {
        return JVM_SPEC_INFO;
    }

    /**
     * ȡ��Java Virtual Machine Implementation����Ϣ��
     *
     * @return <code>JvmInfo</code>����
     */
    public static final JvmInfo getJvmInfo() {
        return JVM_INFO;
    }

    /**
     * ȡ��Java Specification����Ϣ��
     *
     * @return <code>JavaSpecInfo</code>����
     */
    public static final JavaSpecInfo getJavaSpecInfo() {
        return JAVA_SPEC_INFO;
    }

    /**
     * ȡ��Java Implementation����Ϣ��
     *
     * @return <code>JavaInfo</code>����
     */
    public static final JavaInfo getJavaInfo() {
        return JAVA_INFO;
    }

    /**
     * ȡ�õ�ǰ���е�JRE����Ϣ��
     *
     * @return <code>JreInfo</code>����
     */
    public static final JavaRuntimeInfo getJavaRuntimeInfo() {
        return JAVA_RUNTIME_INFO;
    }

    /**
     * ȡ��OS����Ϣ��
     *
     * @return <code>OsInfo</code>����
     */
    public static final OsInfo getOsInfo() {
        return OS_INFO;
    }

    /**
     * ȡ��User����Ϣ��
     *
     * @return <code>UserInfo</code>����
     */
    public static final UserInfo getUserInfo() {
        return USER_INFO;
    }

    /**
     * ȡ��Host����Ϣ��
     *
     * @return <code>HostInfo</code>����
     */
    public static final HostInfo getHostInfo() {
        return HOST_INFO;
    }

    /**
     * ����Java Virutal Machine Specification����Ϣ��
     */
    public static final class JvmSpecInfo {
        private final String JAVA_VM_SPECIFICATION_NAME    = getSystemProperty(
                                                               "java.vm.specification.name", false);
        private final String JAVA_VM_SPECIFICATION_VERSION = getSystemProperty(
                                                               "java.vm.specification.version",
                                                               false);
        private final String JAVA_VM_SPECIFICATION_VENDOR  = getSystemProperty(
                                                               "java.vm.specification.vendor",
                                                               false);

        /**
         * ��ֹ����紴���˶���
         */
        private JvmSpecInfo() {
        }

        /**
         * ȡ�õ�ǰJVM spec.�����ƣ�ȡ��ϵͳ���ԣ�<code>java.vm.specification.name</code>����
         * 
         * <p>
         * ����Sun JDK 1.4.2��<code>"Java Virtual Machine Specification"</code>
         * </p>
         *
         * @return ����ֵ���������ȡ�ã���ΪJava��ȫ���ƣ���ֵ�����ڣ��򷵻�<code>null</code>��
         *
         * @since Java 1.2
         */
        public final String getName() {
            return JAVA_VM_SPECIFICATION_NAME;
        }

        /**
         * ȡ�õ�ǰJVM spec.�İ汾��ȡ��ϵͳ���ԣ�<code>java.vm.specification.version</code>����
         * 
         * <p>
         * ����Sun JDK 1.4.2��<code>"1.0"</code>
         * </p>
         *
         * @return ����ֵ���������ȡ�ã���ΪJava��ȫ���ƣ���ֵ�����ڣ��򷵻�<code>null</code>��
         *
         * @since Java 1.2
         */
        public final String getVersion() {
            return JAVA_VM_SPECIFICATION_VERSION;
        }

        /**
         * ȡ�õ�ǰJVM spec.�ĳ��̣�ȡ��ϵͳ���ԣ�<code>java.vm.specification.vendor</code>����
         * 
         * <p>
         * ����Sun JDK 1.4.2��<code>"Sun Microsystems Inc."</code>
         * </p>
         *
         * @return ����ֵ���������ȡ�ã���ΪJava��ȫ���ƣ���ֵ�����ڣ��򷵻�<code>null</code>��
         *
         * @since Java 1.2
         */
        public final String getVendor() {
            return JAVA_VM_SPECIFICATION_VENDOR;
        }

        /**
         * ��Java Virutal Machine Specification����Ϣת�����ַ�����
         *
         * @return JVM spec.���ַ�����ʾ
         */
        public final String toString() {
            StringBuffer buffer = new StringBuffer();

            append(buffer, "JavaVM Spec. Name:    ", getName());
            append(buffer, "JavaVM Spec. Version: ", getVersion());
            append(buffer, "JavaVM Spec. Vendor:  ", getVendor());

            return buffer.toString();
        }
    }

    /**
     * ����Java Virtual Machine Implementation����Ϣ��
     */
    public static final class JvmInfo {
        private final String JAVA_VM_NAME    = getSystemProperty("java.vm.name", false);
        private final String JAVA_VM_VERSION = getSystemProperty("java.vm.version", false);
        private final String JAVA_VM_VENDOR  = getSystemProperty("java.vm.vendor", false);
        private final String JAVA_VM_INFO    = getSystemProperty("java.vm.info", false);

        /**
         * ��ֹ����紴���˶���
         */
        private JvmInfo() {
        }

        /**
         * ȡ�õ�ǰJVM impl.�����ƣ�ȡ��ϵͳ���ԣ�<code>java.vm.name</code>����
         * 
         * <p>
         * ����Sun JDK 1.4.2��<code>"Java HotSpot(TM) Client VM"</code>
         * </p>
         *
         * @return ����ֵ���������ȡ�ã���ΪJava��ȫ���ƣ���ֵ�����ڣ��򷵻�<code>null</code>��
         *
         * @since Java 1.2
         */
        public final String getName() {
            return JAVA_VM_NAME;
        }

        /**
         * ȡ�õ�ǰJVM impl.�İ汾��ȡ��ϵͳ���ԣ�<code>java.vm.version</code>����
         * 
         * <p>
         * ����Sun JDK 1.4.2��<code>"1.4.2-b28"</code>
         * </p>
         *
         * @return ����ֵ���������ȡ�ã���ΪJava��ȫ���ƣ���ֵ�����ڣ��򷵻�<code>null</code>��
         *
         * @since Java 1.2
         */
        public final String getVersion() {
            return JAVA_VM_VERSION;
        }

        /**
         * ȡ�õ�ǰJVM impl.�ĳ��̣�ȡ��ϵͳ���ԣ�<code>java.vm.vendor</code>����
         * 
         * <p>
         * ����Sun JDK 1.4.2��<code>"Sun Microsystems Inc."</code>
         * </p>
         *
         * @return ����ֵ���������ȡ�ã���ΪJava��ȫ���ƣ���ֵ�����ڣ��򷵻�<code>null</code>��
         *
         * @since Java 1.2
         */
        public final String getVendor() {
            return JAVA_VM_VENDOR;
        }

        /**
         * ȡ�õ�ǰJVM impl.����Ϣ��ȡ��ϵͳ���ԣ�<code>java.vm.info</code>����
         * 
         * <p>
         * ����Sun JDK 1.4.2��<code>"mixed mode"</code>
         * </p>
         *
         * @return ����ֵ���������ȡ�ã���ΪJava��ȫ���ƣ���ֵ�����ڣ��򷵻�<code>null</code>��
         *
         * @since Java 1.2
         */
        public final String getInfo() {
            return JAVA_VM_INFO;
        }

        /**
         * ��Java Virutal Machine Implementation����Ϣת�����ַ�����
         *
         * @return JVM impl.���ַ�����ʾ
         */
        public final String toString() {
            StringBuffer buffer = new StringBuffer();

            append(buffer, "JavaVM Name:    ", getName());
            append(buffer, "JavaVM Version: ", getVersion());
            append(buffer, "JavaVM Vendor:  ", getVendor());
            append(buffer, "JavaVM Info:    ", getInfo());

            return buffer.toString();
        }
    }

    /**
     * ����Java Specification����Ϣ��
     */
    public static final class JavaSpecInfo {
        private final String JAVA_SPECIFICATION_NAME    = getSystemProperty(
                                                            "java.specification.name", false);
        private final String JAVA_SPECIFICATION_VERSION = getSystemProperty(
                                                            "java.specification.version", false);
        private final String JAVA_SPECIFICATION_VENDOR  = getSystemProperty(
                                                            "java.specification.vendor", false);

        /**
         * ��ֹ����紴���˶���
         */
        private JavaSpecInfo() {
        }

        /**
         * ȡ�õ�ǰJava Spec.�����ƣ�ȡ��ϵͳ���ԣ�<code>java.specification.name</code>����
         * 
         * <p>
         * ����Sun JDK 1.4.2��<code>"Java Platform API Specification"</code>
         * </p>
         *
         * @return ����ֵ���������ȡ�ã���ΪJava��ȫ���ƣ���ֵ�����ڣ��򷵻�<code>null</code>��
         *
         * @since Java 1.2
         */
        public final String getName() {
            return JAVA_SPECIFICATION_NAME;
        }

        /**
         * ȡ�õ�ǰJava Spec.�İ汾��ȡ��ϵͳ���ԣ�<code>java.specification.version</code>����
         * 
         * <p>
         * ����Sun JDK 1.4.2��<code>"1.4"</code>
         * </p>
         *
         * @return ����ֵ���������ȡ�ã���ΪJava��ȫ���ƣ���ֵ�����ڣ��򷵻�<code>null</code>��
         *
         * @since Java 1.3
         */
        public final String getVersion() {
            return JAVA_SPECIFICATION_VERSION;
        }

        /**
         * ȡ�õ�ǰJava Spec.�ĳ��̣�ȡ��ϵͳ���ԣ�<code>java.specification.vendor</code>����
         * 
         * <p>
         * ����Sun JDK 1.4.2��<code>"Sun Microsystems Inc."</code>
         * </p>
         *
         * @return ����ֵ���������ȡ�ã���ΪJava��ȫ���ƣ���ֵ�����ڣ��򷵻�<code>null</code>��
         *
         * @since Java 1.2
         */
        public final String getVendor() {
            return JAVA_SPECIFICATION_VENDOR;
        }

        /**
         * ��Java Specification����Ϣת�����ַ�����
         *
         * @return JVM spec.���ַ�����ʾ
         */
        public final String toString() {
            StringBuffer buffer = new StringBuffer();

            append(buffer, "Java Spec. Name:    ", getName());
            append(buffer, "Java Spec. Version: ", getVersion());
            append(buffer, "Java Spec. Vendor:  ", getVendor());

            return buffer.toString();
        }
    }

    /**
     * ����Java Implementation����Ϣ��
     */
    public static final class JavaInfo {
        private final String  JAVA_VERSION       = getSystemProperty("java.version", false);
        private final float   JAVA_VERSION_FLOAT = getJavaVersionAsFloat();
        private final int     JAVA_VERSION_INT   = getJavaVersionAsInt();
        private final String  JAVA_VENDOR        = getSystemProperty("java.vendor", false);
        private final String  JAVA_VENDOR_URL    = getSystemProperty("java.vendor.url", false);
        private final boolean IS_JAVA_1_1        = getJavaVersionMatches("1.1");
        private final boolean IS_JAVA_1_2        = getJavaVersionMatches("1.2");
        private final boolean IS_JAVA_1_3        = getJavaVersionMatches("1.3");
        private final boolean IS_JAVA_1_4        = getJavaVersionMatches("1.4");
        private final boolean IS_JAVA_1_5        = getJavaVersionMatches("1.5");

        /**
         * ��ֹ����紴���˶���
         */
        private JavaInfo() {
        }

        /**
         * ȡ�õ�ǰJava impl.�İ汾��ȡ��ϵͳ���ԣ�<code>java.version</code>����
         * 
         * <p>
         * ����Sun JDK 1.4.2��<code>"1.4.2"</code>
         * </p>
         *
         * @return ����ֵ���������ȡ�ã���ΪJava��ȫ���ƣ���ֵ�����ڣ��򷵻�<code>null</code>��
         *
         * @since Java 1.1
         */
        public final String getVersion() {
            return JAVA_VERSION;
        }

        /**
         * ȡ�õ�ǰJava impl.�İ汾��ȡ��ϵͳ���ԣ�<code>java.version</code>����
         * 
         * <p>
         * ���磺
         * 
         * <ul>
         * <li>
         * JDK 1.2��<code>1.2f</code>��
         * </li>
         * <li>
         * JDK 1.3.1��<code>1.31f</code>
         * </li>
         * </ul>
         * </p>
         *
         * @return ����ֵ���������ȡ�ã���ΪJava��ȫ���ƣ���ֵ�����ڣ��򷵻�<code>0</code>��
         *
         * @since Java 1.1
         */
        public final float getVersionFloat() {
            return JAVA_VERSION_FLOAT;
        }

        /**
         * ȡ�õ�ǰJava impl.�İ汾��ȡ��ϵͳ���ԣ�<code>java.version</code>����
         * 
         * <p>
         * ���磺
         * 
         * <ul>
         * <li>
         * JDK 1.2��<code>120</code>��
         * </li>
         * <li>
         * JDK 1.3.1��<code>131</code>
         * </li>
         * </ul>
         * </p>
         *
         * @return ����ֵ���������ȡ�ã���ΪJava��ȫ���ƣ���ֵ�����ڣ��򷵻�<code>0</code>��
         *
         * @since Java 1.1
         */
        public final int getVersionInt() {
            return JAVA_VERSION_INT;
        }

        /**
         * ȡ�õ�ǰJava impl.�İ汾��<code>float</code>ֵ��
         *
         * @return Java�汾��<code>float</code>ֵ��<code>0</code>
         */
        private final float getJavaVersionAsFloat() {
            if (JAVA_VERSION == null) {
                return 0f;
            }

            String str = JAVA_VERSION.substring(0, 3);

            if (JAVA_VERSION.length() >= 5) {
                str = str + JAVA_VERSION.substring(4, 5);
            }

            return Float.parseFloat(str);
        }

        /**
         * ȡ�õ�ǰJava impl.�İ汾��<code>int</code>ֵ��
         *
         * @return Java�汾��<code>int</code>ֵ��<code>0</code>
         */
        private final int getJavaVersionAsInt() {
            if (JAVA_VERSION == null) {
                return 0;
            }

            String str = JAVA_VERSION.substring(0, 1);

            str = str + JAVA_VERSION.substring(2, 3);

            if (JAVA_VERSION.length() >= 5) {
                str = str + JAVA_VERSION.substring(4, 5);
            } else {
                str = str + "0";
            }

            return Integer.parseInt(str);
        }

        /**
         * ȡ�õ�ǰJava impl.�ĳ��̣�ȡ��ϵͳ���ԣ�<code>java.vendor</code>����
         * 
         * <p>
         * ����Sun JDK 1.4.2��<code>"Sun Microsystems Inc."</code>
         * </p>
         *
         * @return ����ֵ���������ȡ�ã���ΪJava��ȫ���ƣ���ֵ�����ڣ��򷵻�<code>null</code>��
         *
         * @since Java 1.1
         */
        public final String getVendor() {
            return JAVA_VENDOR;
        }

        /**
         * ȡ�õ�ǰJava impl.�ĳ�����վ��URL��ȡ��ϵͳ���ԣ�<code>java.vendor.url</code>����
         * 
         * <p>
         * ����Sun JDK 1.4.2��<code>"http://java.sun.com/"</code>
         * </p>
         *
         * @return ����ֵ���������ȡ�ã���ΪJava��ȫ���ƣ���ֵ�����ڣ��򷵻�<code>null</code>��
         *
         * @since Java 1.1
         */
        public final String getVendorURL() {
            return JAVA_VENDOR_URL;
        }

        /**
         * �жϵ�ǰJava�İ汾��
         * 
         * <p>
         * �������ȡ��ϵͳ����<code>java.version</code>����ΪJava��ȫ���ƣ��������Ƿ���<code>false</code>
         * </p>
         *
         * @return �����ǰJava�汾Ϊ1.1���򷵻�<code>true</code>
         */
        public final boolean isJava11() {
            return IS_JAVA_1_1;
        }

        /**
         * �жϵ�ǰJava�İ汾��
         * 
         * <p>
         * �������ȡ��ϵͳ����<code>java.version</code>����ΪJava��ȫ���ƣ��������Ƿ���<code>false</code>
         * </p>
         *
         * @return �����ǰJava�汾Ϊ1.2���򷵻�<code>true</code>
         */
        public final boolean isJava12() {
            return IS_JAVA_1_2;
        }

        /**
         * �жϵ�ǰJava�İ汾��
         * 
         * <p>
         * �������ȡ��ϵͳ����<code>java.version</code>����ΪJava��ȫ���ƣ��������Ƿ���<code>false</code>
         * </p>
         *
         * @return �����ǰJava�汾Ϊ1.3���򷵻�<code>true</code>
         */
        public final boolean isJava13() {
            return IS_JAVA_1_3;
        }

        /**
         * �жϵ�ǰJava�İ汾��
         * 
         * <p>
         * �������ȡ��ϵͳ����<code>java.version</code>����ΪJava��ȫ���ƣ��������Ƿ���<code>false</code>
         * </p>
         *
         * @return �����ǰJava�汾Ϊ1.4���򷵻�<code>true</code>
         */
        public final boolean isJava14() {
            return IS_JAVA_1_4;
        }

        /**
         * �жϵ�ǰJava�İ汾��
         * 
         * <p>
         * �������ȡ��ϵͳ����<code>java.version</code>����ΪJava��ȫ���ƣ��������Ƿ���<code>false</code>
         * </p>
         *
         * @return �����ǰJava�汾Ϊ1.5���򷵻�<code>true</code>
         */
        public final boolean isJava15() {
            return IS_JAVA_1_5;
        }

        /**
         * ƥ�䵱ǰJava�İ汾��
         *
         * @param versionPrefix Java�汾ǰ׺
         *
         * @return ����汾ƥ�䣬�򷵻�<code>true</code>
         */
        private final boolean getJavaVersionMatches(String versionPrefix) {
            if (JAVA_VERSION == null) {
                return false;
            }

            return JAVA_VERSION.startsWith(versionPrefix);
        }

        /**
         * �ж���ǰJava�İ汾�Ƿ���ڵ���ָ���İ汾�š�
         * 
         * <p>
         * ���磺
         * </p>
         * 
         * <ul>
         * <li>
         * ����JDK 1.2��<code>isJavaVersionAtLeast(1.2f)</code>
         * </li>
         * <li>
         * ����JDK 1.2.1��<code>isJavaVersionAtLeast(1.31f)</code>
         * </li>
         * </ul>
         * 
         *
         * @param requiredVersion ��Ҫ�İ汾
         *
         * @return �����ǰJava�汾���ڻ����ָ���İ汾���򷵻�<code>true</code>
         */
        public final boolean isJavaVersionAtLeast(float requiredVersion) {
            return getVersionFloat() >= requiredVersion;
        }

        /**
         * �ж���ǰJava�İ汾�Ƿ���ڵ���ָ���İ汾�š�
         * 
         * <p>
         * ���磺
         * </p>
         * 
         * <ul>
         * <li>
         * ����JDK 1.2��<code>isJavaVersionAtLeast(120)</code>
         * </li>
         * <li>
         * ����JDK 1.2.1��<code>isJavaVersionAtLeast(131)</code>
         * </li>
         * </ul>
         * 
         *
         * @param requiredVersion ��Ҫ�İ汾
         *
         * @return �����ǰJava�汾���ڻ����ָ���İ汾���򷵻�<code>true</code>
         */
        public final boolean isJavaVersionAtLeast(int requiredVersion) {
            return getVersionInt() >= requiredVersion;
        }

        /**
         * ��Java Implementation����Ϣת�����ַ�����
         *
         * @return JVM impl.���ַ�����ʾ
         */
        public final String toString() {
            StringBuffer buffer = new StringBuffer();

            append(buffer, "Java Version:    ", getVersion());
            append(buffer, "Java Vendor:     ", getVendor());
            append(buffer, "Java Vendor URL: ", getVendorURL());

            return buffer.toString();
        }
    }

    /**
     * ����ǰ���е�JRE����Ϣ��
     */
    public static final class JavaRuntimeInfo {
        private final String JAVA_RUNTIME_NAME    = getSystemProperty("java.runtime.name", false);
        private final String JAVA_RUNTIME_VERSION = getSystemProperty("java.runtime.version", false);
        private final String JAVA_HOME            = getSystemProperty("java.home", false);
        private final String JAVA_EXT_DIRS        = getSystemProperty("java.ext.dirs", false);
        private final String JAVA_ENDORSED_DIRS   = getSystemProperty("java.endorsed.dirs", false);
        private final String JAVA_CLASS_PATH      = getSystemProperty("java.class.path", false);
        private final String JAVA_CLASS_VERSION   = getSystemProperty("java.class.version", false);
        private final String JAVA_LIBRARY_PATH    = getSystemProperty("java.library.path", false);

        /**
         * ��ֹ����紴���˶���
         */
        private JavaRuntimeInfo() {
        }

        /**
         * ȡ�õ�ǰJRE�����ƣ�ȡ��ϵͳ���ԣ�<code>java.runtime.name</code>����
         * 
         * <p>
         * ����Sun JDK 1.4.2��<code>"Java(TM) 2 Runtime Environment, Standard Edition"</code>
         * </p>
         *
         * @return ����ֵ���������ȡ�ã���ΪJava��ȫ���ƣ���ֵ�����ڣ��򷵻�<code>null</code>��
         *
         * @since Java 1.3
         */
        public final String getName() {
            return JAVA_RUNTIME_NAME;
        }

        /**
         * ȡ�õ�ǰJRE�İ汾��ȡ��ϵͳ���ԣ�<code>java.runtime.version</code>����
         * 
         * <p>
         * ����Sun JDK 1.4.2��<code>"1.4.2-b28"</code>
         * </p>
         *
         * @return ����ֵ���������ȡ�ã���ΪJava��ȫ���ƣ���ֵ�����ڣ��򷵻�<code>null</code>��
         *
         * @since Java 1.3
         */
        public final String getVersion() {
            return JAVA_RUNTIME_VERSION;
        }

        /**
         * ȡ�õ�ǰJRE�İ�װĿ¼��ȡ��ϵͳ���ԣ�<code>java.home</code>����
         * 
         * <p>
         * ����Sun JDK 1.4.2��<code>"/opt/jdk1.4.2/jre"</code>
         * </p>
         *
         * @return ����ֵ���������ȡ�ã���ΪJava��ȫ���ƣ���ֵ�����ڣ��򷵻�<code>null</code>��
         *
         * @since Java 1.1
         */
        public final String getHomeDir() {
            return JAVA_HOME;
        }

        /**
         * ȡ�õ�ǰJRE����չĿ¼�б�ȡ��ϵͳ���ԣ�<code>java.ext.dirs</code>����
         * 
         * <p>
         * ����Sun JDK 1.4.2��<code>"/opt/jdk1.4.2/jre/lib/ext:..."</code>
         * </p>
         *
         * @return ����ֵ���������ȡ�ã���ΪJava��ȫ���ƣ���ֵ�����ڣ��򷵻�<code>null</code>��
         *
         * @since Java 1.3
         */
        public final String getExtDirs() {
            return JAVA_EXT_DIRS;
        }

        /**
         * ȡ�õ�ǰJRE��endorsedĿ¼�б�ȡ��ϵͳ���ԣ�<code>java.endorsed.dirs</code>����
         * 
         * <p>
         * ����Sun JDK 1.4.2��<code>"/opt/jdk1.4.2/jre/lib/endorsed:..."</code>
         * </p>
         *
         * @return ����ֵ���������ȡ�ã���ΪJava��ȫ���ƣ���ֵ�����ڣ��򷵻�<code>null</code>��
         *
         * @since Java 1.4
         */
        public final String getEndorsedDirs() {
            return JAVA_ENDORSED_DIRS;
        }

        /**
         * ȡ�õ�ǰJRE��ϵͳclasspath��ȡ��ϵͳ���ԣ�<code>java.class.path</code>����
         * 
         * <p>
         * ���磺<code>"/home/admin/myclasses:/home/admin/..."</code>
         * </p>
         *
         * @return ����ֵ���������ȡ�ã���ΪJava��ȫ���ƣ���ֵ�����ڣ��򷵻�<code>null</code>��
         *
         * @since Java 1.1
         */
        public final String getClassPath() {
            return JAVA_CLASS_PATH;
        }

        /**
         * ȡ�õ�ǰJRE��ϵͳclasspath��ȡ��ϵͳ���ԣ�<code>java.class.path</code>����
         * 
         * <p>
         * ���磺<code>"/home/admin/myclasses:/home/admin/..."</code>
         * </p>
         *
         * @return ����ֵ���������ȡ�ã���ΪJava��ȫ���ƣ���ֵ�����ڣ��򷵻�<code>null</code>��
         *
         * @since Java 1.1
         */
        public final String[] getClassPathArray() {
            return StringUtils.split(getClassPath(), getOsInfo().getPathSeparator());
        }

        /**
         * ȡ�õ�ǰJRE��class�ļ���ʽ�İ汾��ȡ��ϵͳ���ԣ�<code>java.class.version</code>����
         * 
         * <p>
         * ����Sun JDK 1.4.2��<code>"48.0"</code>
         * </p>
         *
         * @return ����ֵ���������ȡ�ã���ΪJava��ȫ���ƣ���ֵ�����ڣ��򷵻�<code>null</code>��
         *
         * @since Java 1.1
         */
        public final String getClassVersion() {
            return JAVA_CLASS_VERSION;
        }

        /**
         * ȡ�õ�ǰJRE��library����·����ȡ��ϵͳ���ԣ�<code>java.library.path</code>����
         * 
         * <p>
         * ����Sun JDK 1.4.2��<code>"/opt/jdk1.4.2/bin:..."</code>
         * </p>
         *
         * @return ����ֵ���������ȡ�ã���ΪJava��ȫ���ƣ���ֵ�����ڣ��򷵻�<code>null</code>��
         *
         * @since Java 1.2
         */
        public final String getLibraryPath() {
            return JAVA_LIBRARY_PATH;
        }

        /**
         * ȡ�õ�ǰJRE��library����·����ȡ��ϵͳ���ԣ�<code>java.library.path</code>����
         * 
         * <p>
         * ����Sun JDK 1.4.2��<code>"/opt/jdk1.4.2/bin:..."</code>
         * </p>
         *
         * @return ����ֵ���������ȡ�ã���ΪJava��ȫ���ƣ���ֵ�����ڣ��򷵻�<code>null</code>��
         *
         * @since Java 1.2
         */
        public final String[] getLibraryPathArray() {
            return StringUtils.split(getLibraryPath(), getOsInfo().getPathSeparator());
        }

        /**
         * ȡ�õ�ǰJRE��URLЭ��packages�б�ȡ��ϵͳ���ԣ�<code>java.library.path</code>����
         * 
         * <p>
         * ����Sun JDK 1.4.2��<code>"sun.net.www.protocol|..."</code>
         * </p>
         *
         * @return ����ֵ���������ȡ�ã���ΪJava��ȫ���ƣ���ֵ�����ڣ��򷵻�<code>null</code>��
         *
         * @since Java 1.2
         */
        public final String getProtocolPackages() {
            return getSystemProperty("java.protocol.handler.pkgs", true);
        }

        /**
         * ����ǰ���е�JRE��Ϣת�����ַ�����
         *
         * @return JRE��Ϣ���ַ�����ʾ
         */
        public final String toString() {
            StringBuffer buffer = new StringBuffer();

            append(buffer, "Java Runtime Name:      ", getName());
            append(buffer, "Java Runtime Version:   ", getVersion());
            append(buffer, "Java Home Dir:          ", getHomeDir());
            append(buffer, "Java Extension Dirs:    ", getExtDirs());
            append(buffer, "Java Endorsed Dirs:     ", getEndorsedDirs());
            append(buffer, "Java Class Path:        ", getClassPath());
            append(buffer, "Java Class Version:     ", getClassVersion());
            append(buffer, "Java Library Path:      ", getLibraryPath());
            append(buffer, "Java Protocol Packages: ", getProtocolPackages());

            return buffer.toString();
        }
    }

    /**
     * ����ǰOS����Ϣ��
     */
    public static final class OsInfo {
        private final String  OS_VERSION         = getSystemProperty("os.version", false);
        private final String  OS_ARCH            = getSystemProperty("os.arch", false);
        private final String  OS_NAME            = getSystemProperty("os.name", false);
        private final boolean IS_OS_AIX          = getOSMatches("AIX");
        private final boolean IS_OS_HP_UX        = getOSMatches("HP-UX");
        private final boolean IS_OS_IRIX         = getOSMatches("Irix");
        private final boolean IS_OS_LINUX        = getOSMatches("Linux") || getOSMatches("LINUX");
        private final boolean IS_OS_MAC          = getOSMatches("Mac");
        private final boolean IS_OS_MAC_OSX      = getOSMatches("Mac OS X");
        private final boolean IS_OS_OS2          = getOSMatches("OS/2");
        private final boolean IS_OS_SOLARIS      = getOSMatches("Solaris");
        private final boolean IS_OS_SUN_OS       = getOSMatches("SunOS");
        private final boolean IS_OS_WINDOWS      = getOSMatches("Windows");
        private final boolean IS_OS_WINDOWS_2000 = getOSMatches("Windows", "5.0");
        private final boolean IS_OS_WINDOWS_95   = getOSMatches("Windows 9", "4.0");
        private final boolean IS_OS_WINDOWS_98   = getOSMatches("Windows 9", "4.1");
        private final boolean IS_OS_WINDOWS_ME   = getOSMatches("Windows", "4.9");
        private final boolean IS_OS_WINDOWS_NT   = getOSMatches("Windows NT");
        private final boolean IS_OS_WINDOWS_XP   = getOSMatches("Windows", "5.1");

        // ���ڸı�file.encoding���Բ�����ı�ϵͳ�ַ����룬Ϊ�˱���һ�£�ͨ��LocaleUtilȡϵͳĬ�ϱ��롣
        private final String  FILE_ENCODING      = Charset.defaultCharset().toString();
        private final String  FILE_SEPARATOR     = getSystemProperty("file.separator", false);
        private final String  LINE_SEPARATOR     = getSystemProperty("line.separator", false);
        private final String  PATH_SEPARATOR     = getSystemProperty("path.separator", false);

        /**
         * ��ֹ����紴���˶���
         */
        private OsInfo() {
        }

        /**
         * ȡ�õ�ǰOS�ļܹ���ȡ��ϵͳ���ԣ�<code>os.arch</code>����
         * 
         * <p>
         * ���磺<code>"x86"</code>
         * </p>
         *
         * @return ����ֵ���������ȡ�ã���ΪJava��ȫ���ƣ���ֵ�����ڣ��򷵻�<code>null</code>��
         *
         * @since Java 1.1
         */
        public final String getArch() {
            return OS_ARCH;
        }

        /**
         * ȡ�õ�ǰOS�����ƣ�ȡ��ϵͳ���ԣ�<code>os.name</code>����
         * 
         * <p>
         * ���磺<code>"Windows XP"</code>
         * </p>
         *
         * @return ����ֵ���������ȡ�ã���ΪJava��ȫ���ƣ���ֵ�����ڣ��򷵻�<code>null</code>��
         *
         * @since Java 1.1
         */
        public final String getName() {
            return OS_NAME;
        }

        /**
         * ȡ�õ�ǰOS�İ汾��ȡ��ϵͳ���ԣ�<code>os.version</code>����
         * 
         * <p>
         * ���磺<code>"5.1"</code>
         * </p>
         *
         * @return ����ֵ���������ȡ�ã���ΪJava��ȫ���ƣ���ֵ�����ڣ��򷵻�<code>null</code>��
         *
         * @since Java 1.1
         */
        public final String getVersion() {
            return OS_VERSION;
        }

        /**
         * �жϵ�ǰOS�����͡�
         * 
         * <p>
         * �������ȡ��ϵͳ����<code>os.name</code>����ΪJava��ȫ���ƣ��������Ƿ���<code>false</code>
         * </p>
         *
         * @return �����ǰOS����ΪAIX���򷵻�<code>true</code>
         */
        public final boolean isAix() {
            return IS_OS_AIX;
        }

        /**
         * �жϵ�ǰOS�����͡�
         * 
         * <p>
         * �������ȡ��ϵͳ����<code>os.name</code>����ΪJava��ȫ���ƣ��������Ƿ���<code>false</code>
         * </p>
         *
         * @return �����ǰOS����ΪHP-UX���򷵻�<code>true</code>
         */
        public final boolean isHpUx() {
            return IS_OS_HP_UX;
        }

        /**
         * �жϵ�ǰOS�����͡�
         * 
         * <p>
         * �������ȡ��ϵͳ����<code>os.name</code>����ΪJava��ȫ���ƣ��������Ƿ���<code>false</code>
         * </p>
         *
         * @return �����ǰOS����ΪIRIX���򷵻�<code>true</code>
         */
        public final boolean isIrix() {
            return IS_OS_IRIX;
        }

        /**
         * �жϵ�ǰOS�����͡�
         * 
         * <p>
         * �������ȡ��ϵͳ����<code>os.name</code>����ΪJava��ȫ���ƣ��������Ƿ���<code>false</code>
         * </p>
         *
         * @return �����ǰOS����ΪLinux���򷵻�<code>true</code>
         */
        public final boolean isLinux() {
            return IS_OS_LINUX;
        }

        /**
         * �жϵ�ǰOS�����͡�
         * 
         * <p>
         * �������ȡ��ϵͳ����<code>os.name</code>����ΪJava��ȫ���ƣ��������Ƿ���<code>false</code>
         * </p>
         *
         * @return �����ǰOS����ΪMac���򷵻�<code>true</code>
         */
        public final boolean isMac() {
            return IS_OS_MAC;
        }

        /**
         * �жϵ�ǰOS�����͡�
         * 
         * <p>
         * �������ȡ��ϵͳ����<code>os.name</code>����ΪJava��ȫ���ƣ��������Ƿ���<code>false</code>
         * </p>
         *
         * @return �����ǰOS����ΪMacOS X���򷵻�<code>true</code>
         */
        public final boolean isMacOsX() {
            return IS_OS_MAC_OSX;
        }

        /**
         * �жϵ�ǰOS�����͡�
         * 
         * <p>
         * �������ȡ��ϵͳ����<code>os.name</code>����ΪJava��ȫ���ƣ��������Ƿ���<code>false</code>
         * </p>
         *
         * @return �����ǰOS����ΪOS2���򷵻�<code>true</code>
         */
        public final boolean isOs2() {
            return IS_OS_OS2;
        }

        /**
         * �жϵ�ǰOS�����͡�
         * 
         * <p>
         * �������ȡ��ϵͳ����<code>os.name</code>����ΪJava��ȫ���ƣ��������Ƿ���<code>false</code>
         * </p>
         *
         * @return �����ǰOS����ΪSolaris���򷵻�<code>true</code>
         */
        public final boolean isSolaris() {
            return IS_OS_SOLARIS;
        }

        /**
         * �жϵ�ǰOS�����͡�
         * 
         * <p>
         * �������ȡ��ϵͳ����<code>os.name</code>����ΪJava��ȫ���ƣ��������Ƿ���<code>false</code>
         * </p>
         *
         * @return �����ǰOS����ΪSun OS���򷵻�<code>true</code>
         */
        public final boolean isSunOS() {
            return IS_OS_SUN_OS;
        }

        /**
         * �жϵ�ǰOS�����͡�
         * 
         * <p>
         * �������ȡ��ϵͳ����<code>os.name</code>����ΪJava��ȫ���ƣ��������Ƿ���<code>false</code>
         * </p>
         *
         * @return �����ǰOS����ΪWindows���򷵻�<code>true</code>
         */
        public final boolean isWindows() {
            return IS_OS_WINDOWS;
        }

        /**
         * �жϵ�ǰOS�����͡�
         * 
         * <p>
         * �������ȡ��ϵͳ����<code>os.name</code>����ΪJava��ȫ���ƣ��������Ƿ���<code>false</code>
         * </p>
         *
         * @return �����ǰOS����ΪWindows 2000���򷵻�<code>true</code>
         */
        public final boolean isWindows2000() {
            return IS_OS_WINDOWS_2000;
        }

        /**
         * �жϵ�ǰOS�����͡�
         * 
         * <p>
         * �������ȡ��ϵͳ����<code>os.name</code>����ΪJava��ȫ���ƣ��������Ƿ���<code>false</code>
         * </p>
         *
         * @return �����ǰOS����ΪWindows 95���򷵻�<code>true</code>
         */
        public final boolean isWindows95() {
            return IS_OS_WINDOWS_95;
        }

        /**
         * �жϵ�ǰOS�����͡�
         * 
         * <p>
         * �������ȡ��ϵͳ����<code>os.name</code>����ΪJava��ȫ���ƣ��������Ƿ���<code>false</code>
         * </p>
         *
         * @return �����ǰOS����ΪWindows 98���򷵻�<code>true</code>
         */
        public final boolean isWindows98() {
            return IS_OS_WINDOWS_98;
        }

        /**
         * �жϵ�ǰOS�����͡�
         * 
         * <p>
         * �������ȡ��ϵͳ����<code>os.name</code>����ΪJava��ȫ���ƣ��������Ƿ���<code>false</code>
         * </p>
         *
         * @return �����ǰOS����ΪWindows ME���򷵻�<code>true</code>
         */
        public final boolean isWindowsME() {
            return IS_OS_WINDOWS_ME;
        }

        /**
         * �жϵ�ǰOS�����͡�
         * 
         * <p>
         * �������ȡ��ϵͳ����<code>os.name</code>����ΪJava��ȫ���ƣ��������Ƿ���<code>false</code>
         * </p>
         *
         * @return �����ǰOS����ΪWindows NT���򷵻�<code>true</code>
         */
        public final boolean isWindowsNT() {
            return IS_OS_WINDOWS_NT;
        }

        /**
         * �жϵ�ǰOS�����͡�
         * 
         * <p>
         * �������ȡ��ϵͳ����<code>os.name</code>����ΪJava��ȫ���ƣ��������Ƿ���<code>false</code>
         * </p>
         *
         * @return �����ǰOS����ΪWindows XP���򷵻�<code>true</code>
         */
        public final boolean isWindowsXP() {
            return IS_OS_WINDOWS_XP;
        }

        /**
         * ƥ��OS���ơ�
         *
         * @param osNamePrefix OS����ǰ׺
         *
         * @return ���ƥ�䣬�򷵻�<code>true</code>
         */
        private final boolean getOSMatches(String osNamePrefix) {
            if (OS_NAME == null) {
                return false;
            }

            return OS_NAME.startsWith(osNamePrefix);
        }

        /**
         * ƥ��OS���ơ�
         *
         * @param osNamePrefix OS����ǰ׺
         * @param osVersionPrefix OS�汾ǰ׺
         *
         * @return ���ƥ�䣬�򷵻�<code>true</code>
         */
        private final boolean getOSMatches(String osNamePrefix, String osVersionPrefix) {
            if ((OS_NAME == null) || (OS_VERSION == null)) {
                return false;
            }

            return OS_NAME.startsWith(osNamePrefix) && OS_VERSION.startsWith(osVersionPrefix);
        }

        /**
         * ȡ��OS��Ĭ���ַ����뼯��ȡ��ϵͳ���ԣ�<code>file.encoding</code>����
         * 
         * <p>
         * ��������ַ���������Ϊ��ǰJVMת���ֽ�/�ַ���Ĭ�Ϸ�ʽ�� ���磺<code>GBK</code>��
         * </p>
         *
         * @return ����ֵ���������ȡ�ã���ΪJava��ȫ���ƣ���ֵ�����ڣ��򷵻�<code>null</code>��
         *
         * @since Java 1.2
         */
        public final String getFileEncoding() {
            return FILE_ENCODING;
        }

        /**
         * ȡ��OS���ļ�·���ķָ�����ȡ��ϵͳ���ԣ�<code>file.separator</code>����
         * 
         * <p>
         * ���磺UnixΪ<code>"/"</code>��WindowsΪ<code>"\\"</code>��
         * </p>
         *
         * @return ����ֵ���������ȡ�ã���ΪJava��ȫ���ƣ���ֵ�����ڣ��򷵻�<code>null</code>��
         *
         * @since Java 1.1
         */
        public final String getFileSeparator() {
            return FILE_SEPARATOR;
        }

        /**
         * ȡ��OS���ı��ļ����з���ȡ��ϵͳ���ԣ�<code>line.separator</code>����
         * 
         * <p>
         * ���磺UnixΪ<code>"\n"</code>��WindowsΪ<code>"\r\n"</code>��
         * </p>
         *
         * @return ����ֵ���������ȡ�ã���ΪJava��ȫ���ƣ���ֵ�����ڣ��򷵻�<code>null</code>��
         *
         * @since Java 1.1
         */
        public final String getLineSeparator() {
            return LINE_SEPARATOR;
        }

        /**
         * ȡ��OS������·���ָ�����ȡ��ϵͳ���ԣ�<code>path.separator</code>����
         * 
         * <p>
         * ���磺UnixΪ<code>":"</code>��WindowsΪ<code>";"</code>��
         * </p>
         *
         * @return ����ֵ���������ȡ�ã���ΪJava��ȫ���ƣ���ֵ�����ڣ��򷵻�<code>null</code>��
         *
         * @since Java 1.1
         */
        public final String getPathSeparator() {
            return PATH_SEPARATOR;
        }

        /**
         * ��OS����Ϣת�����ַ�����
         *
         * @return OS���ַ�����ʾ
         */
        public final String toString() {
            StringBuffer buffer = new StringBuffer();

            append(buffer, "OS Arch:        ", getArch());
            append(buffer, "OS Name:        ", getName());
            append(buffer, "OS Version:     ", getVersion());
            append(buffer, "File Encoding:  ", getFileEncoding());
            append(buffer, "File Separator: ", getFileSeparator());
            append(buffer, "Line Separator: ", getLineSeparator());
            append(buffer, "Path Separator: ", getPathSeparator());

            return buffer.toString();
        }
    }

    /**
     * ����ǰ�û�����Ϣ��
     */
    public static final class UserInfo {
        private final String USER_NAME      = getSystemProperty("user.name", false);
        private final String USER_HOME      = getSystemProperty("user.home", false);
        private final String USER_DIR       = getSystemProperty("user.dir", false);
        private final String USER_LANGUAGE  = getSystemProperty("user.language", false);
        private final String USER_COUNTRY   = ((getSystemProperty("user.country", false) == null) ? getSystemProperty(
                                                "user.region", false) : getSystemProperty(
                                                "user.country", false));
        private final String JAVA_IO_TMPDIR = getSystemProperty("java.io.tmpdir", false);

        /**
         * ��ֹ����紴���˶���
         */
        private UserInfo() {
        }

        /**
         * ȡ�õ�ǰ��¼�û������֣�ȡ��ϵͳ���ԣ�<code>user.name</code>����
         * 
         * <p>
         * ���磺<code>"admin"</code>
         * </p>
         *
         * @return ����ֵ���������ȡ�ã���ΪJava��ȫ���ƣ���ֵ�����ڣ��򷵻�<code>null</code>��
         *
         * @since Java 1.1
         */
        public final String getName() {
            return USER_NAME;
        }

        /**
         * ȡ�õ�ǰ��¼�û���homeĿ¼��ȡ��ϵͳ���ԣ�<code>user.home</code>����
         * 
         * <p>
         * ���磺<code>"/home/admin"</code>
         * </p>
         *
         * @return ����ֵ���������ȡ�ã���ΪJava��ȫ���ƣ���ֵ�����ڣ��򷵻�<code>null</code>��
         *
         * @since Java 1.1
         */
        public final String getHomeDir() {
            return USER_HOME;
        }

        /**
         * ȡ�õ�ǰĿ¼��ȡ��ϵͳ���ԣ�<code>user.dir</code>����
         * 
         * <p>
         * ���磺<code>"/home/admin/working"</code>
         * </p>
         *
         * @return ����ֵ���������ȡ�ã���ΪJava��ȫ���ƣ���ֵ�����ڣ��򷵻�<code>null</code>��
         *
         * @since Java 1.1
         */
        public final String getCurrentDir() {
            return USER_DIR;
        }

        /**
         * ȡ����ʱĿ¼��ȡ��ϵͳ���ԣ�<code>java.io.tmpdir</code>����
         * 
         * <p>
         * ���磺<code>"/tmp"</code>
         * </p>
         *
         * @return ����ֵ���������ȡ�ã���ΪJava��ȫ���ƣ���ֵ�����ڣ��򷵻�<code>null</code>��
         *
         * @since Java 1.2
         */
        public final String getTempDir() {
            return JAVA_IO_TMPDIR;
        }

        /**
         * ȡ�õ�ǰ��¼�û����������ã�ȡ��ϵͳ���ԣ�<code>user.language</code>����
         * 
         * <p>
         * ���磺<code>"zh"</code>��<code>"en"</code>��
         * </p>
         *
         * @return ����ֵ���������ȡ�ã���ΪJava��ȫ���ƣ���ֵ�����ڣ��򷵻�<code>null</code>��
         *
         * @since Java 1.2
         */
        public final String getLanguage() {
            return USER_LANGUAGE;
        }

        /**
         * ȡ�õ�ǰ��¼�û��Ĺ��һ��������ã�ȡ��ϵͳ���ԣ�JDK1.4 <code>user.country</code>��JDK1.2
         * <code>user.region</code>����
         * 
         * <p>
         * ���磺<code>"CN"</code>��<code>"US"</code>��
         * </p>
         *
         * @return ����ֵ���������ȡ�ã���ΪJava��ȫ���ƣ���ֵ�����ڣ��򷵻�<code>null</code>��
         *
         * @since Java 1.2
         */
        public final String getCountry() {
            return USER_COUNTRY;
        }

        /**
         * ����ǰ�û�����Ϣת�����ַ�����
         *
         * @return �û���Ϣ���ַ�����ʾ
         */
        public final String toString() {
            StringBuffer buffer = new StringBuffer();

            append(buffer, "User Name:        ", getName());
            append(buffer, "User Home Dir:    ", getHomeDir());
            append(buffer, "User Current Dir: ", getCurrentDir());
            append(buffer, "User Temp Dir:    ", getTempDir());
            append(buffer, "User Language:    ", getLanguage());
            append(buffer, "User Country:     ", getCountry());

            return buffer.toString();
        }
    }

    /**
     * ����ǰ��������Ϣ��
     */
    public static final class HostInfo {
        private final String HOST_NAME;
        private final String HOST_ADDRESS;

        /**
         * ��ֹ����紴���˶���
         */
        private HostInfo() {
            String hostName;
            String hostAddress;

            try {
                InetAddress localhost = InetAddress.getLocalHost();

                hostName = localhost.getHostName();
                hostAddress = localhost.getHostAddress();
            } catch (UnknownHostException e) {
                hostName = "localhost";
                hostAddress = "127.0.0.1";
            }

            HOST_NAME = hostName;
            HOST_ADDRESS = hostAddress;
        }

        /**
         * ȡ�õ�ǰ���������ơ�
         * 
         * <p>
         * ���磺<code>"webserver1"</code>
         * </p>
         *
         * @return ������
         */
        public final String getName() {
            return HOST_NAME;
        }

        /**
         * ȡ�õ�ǰ�����ĵ�ַ��
         * 
         * <p>
         * ���磺<code>"192.168.0.1"</code>
         * </p>
         *
         * @return ������ַ
         */
        public final String getAddress() {
            return HOST_ADDRESS;
        }

        /**
         * ����ǰ��������Ϣת�����ַ�����
         *
         * @return ������Ϣ���ַ�����ʾ
         */
        public final String toString() {
            StringBuffer buffer = new StringBuffer();

            append(buffer, "Host Name:    ", getName());
            append(buffer, "Host Address: ", getAddress());

            return buffer.toString();
        }
    }

    /**
     * ��ϵͳ��Ϣ�����ָ��<code>PrintWriter</code>�С�
     */
    public static final void dumpSystemInfo() {
        dumpSystemInfo(new PrintWriter(System.out));
    }

    /**
     * ��ϵͳ��Ϣ�����ָ��<code>PrintWriter</code>�С�
     *
     * @param out <code>PrintWriter</code>�����
     */
    public static final void dumpSystemInfo(PrintWriter out) {
        out.println("--------------");
        out.println(getJvmSpecInfo());
        out.println("--------------");
        out.println(getJvmInfo());
        out.println("--------------");
        out.println(getJavaSpecInfo());
        out.println("--------------");
        out.println(getJavaInfo());
        out.println("--------------");
        out.println(getJavaRuntimeInfo());
        out.println("--------------");
        out.println(getOsInfo());
        out.println("--------------");
        out.println(getUserInfo());
        out.println("--------------");
        out.println(getHostInfo());
        out.println("--------------");
        out.flush();
    }

    /**
     * ȡ��ϵͳ���ԣ������ΪJava��ȫ�����ƶ�ʧ�ܣ��򽫴������<code>System.err</code>�У�Ȼ�󷵻�<code>null</code>��
     *
     * @param name ������
     * @param quiet ����ģʽ������������Ϣ����<code>System.err</code>��
     *
     * @return ����ֵ��<code>null</code>
     */
    private static String getSystemProperty(String name, boolean quiet) {
        try {
            return System.getProperty(name);
        } catch (SecurityException e) {
            if (!quiet) {
                System.err.println("Caught a SecurityException reading the system property '"
                                   + name
                                   + "'; the SystemUtil property value will default to null.");
            }

            return null;
        }
    }

    /**
     * �����<code>StringBuffer</code>��
     *
     * @param buffer <code>StringBuffer</code>����
     * @param caption ����
     * @param value ֵ
     */
    private static void append(StringBuffer buffer, String caption, String value) {
        buffer.append(caption)
            .append(StringUtils.defaultIfBlank(StringEscapeUtils.escapeJava(value), "[n/a]"))
            .append("\n");
    }

    public static void main(String[] args) {
        dumpSystemInfo();

        List list = new ArrayList(System.getProperties().keySet());

        Collections.sort(list);

        for (Iterator i = list.iterator(); i.hasNext();) {
            String key = (String) i.next();
            String value = System.getProperty(key);

            System.out.println(key
                               + " = "
                               + StringUtils.defaultIfBlank(StringEscapeUtils.escapeJava(value),
                                   "[n/a]"));
        }
    }
}
