package com.user.base.until;

import java.text.MessageFormat;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @author ：mmzs
 * @date ：Created in 2020/3/6 10:51
 * @description：读取配置文件的工具类
 * @modified By：
 * @version: 1.0$
 */
public class msg {
    private static final String BUNDLE_NAME = "messages";
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("messages");

    public msg() {
    }

    public static String getString(String key) {
        try {
            return RESOURCE_BUNDLE.getString(key);
        } catch (MissingResourceException var2) {
            var2.printStackTrace();
            return '!' + key + '!';
        }
    }

    public static String getString(String key, String[] paras) {
        try {
            String message = RESOURCE_BUNDLE.getString(key);
            return MessageFormat.format(message, paras);
        } catch (MissingResourceException var3) {
            return '!' + key + '!';
        }
    }

    public static String getString(String key, List arg) {
        try {
            if (arg.isEmpty()) {
                return "";
            } else {
                String[] paras = new String[arg.size()];

                for(int i = 0; i < arg.size(); ++i) {
                    paras[i] = (String)arg.get(i);
                }

                return getString(key, paras);
            }
        } catch (MissingResourceException var4) {
            return '!' + key + '!';
        }
    }
}
