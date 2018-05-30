package cn.sprivacy.template.common.utils;

import com.alibaba.druid.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * @author fanglang
 * @date 2018-05-29 13:33
 * @desc IP地址工具类
 */
public class IpUtils {

    private final static Logger LOGGER = LoggerFactory.getLogger(IpUtils.class);

    private final static String UNKNOWN = "unknown";

    private final static int IP_LENGTH = 15;

    private final static String COMMA = ",";

    /**
     * 获取IP地址
     * <p>
     * 使用Nginx等反向代理软件,则不能通过request.getRemoteAddr()获取IP地址
     * 如果使用了多级反向代理的话,X-Forwarded-For的值并不止一个,而是一串IP地址,X-Forwarded-For中第一个非unknown的有效IP字符串,则为真实IP地址
     *
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = null;
        try {
            ip = request.getHeader("x-forwarded-for");
            if (StringUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (StringUtils.isEmpty(ip) || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (StringUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (StringUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (StringUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        } catch (Exception e) {
            LOGGER.error("IpUtils Error:", e);
        }

       // 使用代理,则获取第一个IP地址
       // if(!StringUtils.isEmpty(ip) && ip.length() > IP_LENGTH) {
		// 	if(ip.indexOf(COMMA) > 0) {
		// 		ip = ip.substring(0, ip.indexOf(","));
		// 	}
		// }

        return ip;
    }
}
