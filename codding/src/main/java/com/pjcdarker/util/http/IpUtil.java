package com.pjcdarker.util.http;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

/**
 * x-forwarded-for(XFF): 用来识别通过HTTP代理或负载均衡方式连接到Web服务器的客户端最原始的IP地址的HTTP请求头字段
 * Remote Address: 来自 TCP 连接，表示与服务端建立 TCP 连接的设备 IP
 * Proxy-Client-IP与WL-Proxy-Client-IP：apache + Weblogic
 * HTTP_CLIENT_IP: 代理服务器发送请求头
 *
 * @author pjcdarker
 * @created 10/21/2017.
 */
public class IpUtil {
    private static final List<String> HEADERS = Arrays.asList("X-Forwarded-For", "X-Real-IP", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP",
                                                              "HTTP_X_FORWARDED_FOR"
                                                             );

    private static final List<String> LOCAL_HOSTS = Arrays.asList("127.0.0.1", "localhost");

    public static String getIp(HttpServletRequest request) {
        String ip = null;
        for (String header : HEADERS) {
            ip = request.getHeader(header);
            if (!isBlankOrUnknown(ip)) {
                return ip;
            }
        }

        if (isBlankOrUnknown(ip)) {
            ip = request.getRemoteAddr();
            if (LOCAL_HOSTS.contains(ip)) {
                try {
                    InetAddress inetAddress = InetAddress.getLocalHost();
                    ip = inetAddress.getHostAddress();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        }
        if (ip.contains(",")) {
            int firstIndex = ip.indexOf(",");
            if (firstIndex != -1) {
                ip = ip.substring(0, firstIndex);
            }
        }
        return ip;
    }

    private static boolean isBlankOrUnknown(String ip) {
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            return true;
        }
        return false;
    }

}
