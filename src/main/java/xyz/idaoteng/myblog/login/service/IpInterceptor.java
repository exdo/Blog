package xyz.idaoteng.myblog.login.service;

import org.springframework.web.servlet.HandlerInterceptor;
import xyz.idaoteng.auth.utils.NetUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.ConcurrentHashMap;

public class IpInterceptor implements HandlerInterceptor {
    private final ConcurrentHashMap<String, Integer> ipCounter = new ConcurrentHashMap<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String realIp = NetUtil.tryToGetRealIp(request);
        if (realIp != null) {
            if (ipCounter.containsKey(realIp)) {
                Integer count = ipCounter.get(realIp);
                if (count > 30) {
                    return false;
                }
                count++;
                ipCounter.put(realIp, count);
            } else {
                ipCounter.put(realIp, 1);
            }
        }
        return true;
    }
}
