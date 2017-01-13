package com.hotusm.fastcrawl.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 获取配置
 */
public class ConfigUtil {

    // 非特殊情况不要公开变量
    private static Properties props;

    static {
        InputStream fis = null;
        try {
            // 初始化路径
            String fileName = "crawl.properties";
            props = new Properties();
            fis=Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
            props.load(fis);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                fis.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    /**
     * 根据给定的key获取:
     *
     * @param key
     * @return
     */
    public static String getValue(String key) {
        if(props.getProperty(key) != null) {
            return props.getProperty(key).trim();
        } else {
            throw new RuntimeException();
        }
    }

    /**
     * 获取Int值
     *
     * @param key
     * @return
     */
    public static int getIntValue(String key) {
        if(props.getProperty(key) != null) {
            return Integer.parseInt(getValue(key));
        } else {
            throw new RuntimeException();
        }
    }

    /**
     * 获取Boolean类型的值
     *
     * @param key
     * @return
     */
    public static boolean getBooleanValue(String key) {
        if(props.getProperty(key) != null) {
            return Boolean.valueOf(getValue(key));
        } else {
            throw new RuntimeException();

        }
    }

}
