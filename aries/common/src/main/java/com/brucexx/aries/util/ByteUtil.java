package com.brucexx.aries.util;

/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2010 All Rights Reserved.
 */
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * 字节操作工具
 * 
 * @author zhao.xiong
 * @version $Id: ByteUtil.java, v 0.1 2013-4-11 下午5:26:10 zhao.xiong Exp $
 */
public final class ByteUtil {

    /**
     * 禁用构造函数
     */
    private ByteUtil() {
        // 禁用构造函数
    }

    /**
     * 把一个字节数组的串格式化成十六进制形式, 格式化后的样式如下:
     * 
     * <pre> 
     *  00000H  61 62 63 64 D6 D0 B9 FA 73 73 73 73 73 73 73 73 ; abcd中国ssssssss 
     *  00016H  73 73 73 73 73 73 73 73 73 B1 B1 BE A9 64 64 64 ; sssssssss北京ddd 
     *  00032H  64 64 64 64 64 64 64 64 64 64 64 64 64 64 64 64 ; dddddddddddddddd  
     * </pre> 
     * 
     * @param by 需要格式化的字节数组
     * @return 格式化后的字符串
     */
    public static String formatByte(byte[] by) {
        if (by == null || by.length == 0) {
            return "";
        }
        StringBuilder result = new StringBuilder(20);

        // 只保存十六进制串后面的字符串 (" : " 就占了三个字节，后面为16个字节)
        byte[] chdata = new byte[19];
        for (int i = 0; i < by.length; i++) {
            String hexStr = Integer.toHexString(by[i]).toUpperCase();

            if (i % 16 == 0) {
                result.append(new String(chdata)).append("\n ");
                Arrays.fill(chdata, (byte) 0x00);
                System.arraycopy(" ; ".getBytes(), 0, chdata, 0, 3);
                for (int j = 0; j < 5 - String.valueOf(i).length(); j++) {
                    result.append("0");
                }

                result.append(i).append("H ");
            }

            if (hexStr.length() >= 2) {
                result.append(" ").append(hexStr.substring(hexStr.length() - 2));
            } else {
                result.append(" 0").append(hexStr.substring(hexStr.length() - 1));
            }

            System.arraycopy(by, i, chdata, 3 + (i % 16), 1);
        }

        for (int j = 0; j < (16 - (by.length % 16)) % 16; j++) {
            result.append("   ");
        }

        result.append(new String(chdata));

        return result.toString();
    }

    public static String binToMAC(byte[] binary) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < binary.length; i++) {
            if (i != 0) {
                result.append("-");
            }
            String hexStr = Integer.toHexString(binary[i]).toUpperCase();
            if (hexStr.length() >= 2) {
                result.append("").append(hexStr.substring(hexStr.length() - 2));
            } else {
                result.append("0").append(hexStr.substring(hexStr.length() - 1));
            }

        }
        return result.toString().trim();
    }

    public static byte[] macToBin(String macAddr) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for (String s : macAddr.trim().split("\\-")) {
            baos.write(Integer.parseInt(s, 16));
        }
        return baos.toByteArray();
    }

    public static String binary2Hex(byte[] binary) {
        StringBuilder result = new StringBuilder();
        result.append("\r\n");
        for (int i = 0; i < binary.length; i++) {
            String hexStr = Integer.toHexString(binary[i]).toUpperCase();
            if (hexStr.length() >= 2) {
                result.append(" ").append(hexStr.substring(hexStr.length() - 2));
            } else {
                result.append(" 0").append(hexStr.substring(hexStr.length() - 1));
            }

            if ((i + 1) % 16 == 0) {
                result.append("\r\n");
            }
        }
        return result.toString();
    }

    public static byte[] copyOfRange(byte[] original, int from, int to) {
        int newLength = to - from;
        if (newLength < 0) {
            throw new IllegalArgumentException(from + " > " + to);
        }
        byte[] copy = new byte[newLength];
        System.arraycopy(original, from, copy, 0, Math.min(original.length - from, newLength));
        return copy;
    }

    public static byte[] copyOf(byte[] original, int newLength) {
        byte[] copy = new byte[newLength];
        System.arraycopy(original, 0, copy, 0, Math.min(original.length, newLength));
        return copy;
    }

    public static byte[] concat(byte[]... args) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for (byte[] arg : args) {
            baos.write(arg);
        }
        return baos.toByteArray();
    }

    public static byte[] hex2Binary(String hexStr) {
        String trimStr = hexStr.replaceAll(" ", "").replaceAll("\r", "").replaceAll("\n", "");
        return hex2byte(trimStr);
    }

    private static byte[] hex2byte(String str) {
        if (str == null) {
            return null;
        }
        str = str.trim();
        int len = str.length();
        if (len == 0 || len % 2 == 1) {
            return null;
        }
        byte[] b = new byte[len / 2];
        try {
            for (int i = 0; i < str.length(); i += 2) {
                b[i / 2] = (byte) Integer.decode("0x" + str.substring(i, i + 2)).intValue();
            }
            return b;
        } catch (Exception e) {
            return null;
        }
    }

    public final static long toLong(byte[] buf) {
        long result = 0;
        for (int i = 0; i < buf.length; i++) {
            long k = buf[i] & 0xff;
            long t = k << (i * 8);
            result |= t;
        }
        return result;
    }

    public final static int toInt(byte[] buf) {
        int result = 0;
        for (int i = 0; i < buf.length; i++) {
            int k = buf[i] & 0xff;
            int t = k << (i * 8);
            result |= t;
        }
        return result;
    }

    public final static byte[] getBytes(long s) {
        byte[] buf = new byte[8];

        for (int i = 0; i < buf.length; i++) {
            buf[i] = (byte) (s & 0x00000000000000ff);
            s >>= 8;
        }
        return buf;
    }

    public final static byte[] getBytes(short s) {
        byte[] buf = new byte[2];

        for (int i = 0; i < buf.length; i++) {
            buf[i] = (byte) (s & 0x00000000000000ff);
            s >>= 8;
        }
        return buf;
    }

    public static void main(String[] args) {
        short port = 6666;
        byte[] portBytes = getBytes(port);
        System.out.println(binToMAC(portBytes));
        // System.out.println((short)toInt(portBytes));
    }
}
