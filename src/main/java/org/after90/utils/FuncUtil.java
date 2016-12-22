package org.after90.utils;

import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.net.URLDecoder;
import java.util.Properties;

/**
 * Created by zhaogj on 22/12/2016.
 */
@Slf4j
public class FuncUtil {
    // 取得本机信息
    public static String getLocalhostInfo() {
        StringBuffer sb = new StringBuffer();
        Properties props = System.getProperties(); // 获得系统属性集
        sb.append("操作系统:\n名称 os.name:");
        sb.append(props.getProperty("os.name"));
        sb.append("\n");
        sb.append("构架 os.arch:");
        sb.append(props.getProperty("os.arch"));
        sb.append("\n");
        sb.append("版本 os.version:");
        sb.append(props.getProperty("os.version"));
        sb.append("\n");
        URL url = FuncUtil.class.getProtectionDomain().getCodeSource().getLocation();
        try {
            sb.append("jar信息:\n");
            sb.append(URLDecoder.decode(url.getPath(), "utf-8"));
        } catch (Exception e) {
            log.error("", e);
        }
        return sb.toString();
    }

}
