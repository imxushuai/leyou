package com.leyou.cart.interceptor;

import com.leyou.auth.entity.UserInfo;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.cart.config.JwtProperties;
import com.leyou.common.util.CookieUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户鉴权拦截器
 */
@Slf4j
public class UserInterceptor extends HandlerInterceptorAdapter {

    private JwtProperties jwtProperties;

    // 将用户信息存放到当前线程中
    private static ThreadLocal<UserInfo> userInfoThreadLocal = new ThreadLocal<>();

    public UserInterceptor(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    /**
     * 前置拦截, 从cookie中获取User信息
     *
     * @param request  http请求
     * @param response http响应
     * @param handler  响应的处理器, 可以自定义controller处理响应
     * @return boolean 获取到User信息返回true, 否则返回false
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            // 从cookie中获取token
            String token = CookieUtils.getCookieValue(request, jwtProperties.getCookieName());
            // 从token中解析User信息
            UserInfo userInfo = JwtUtils.getUserInfo(jwtProperties.getPublicKey(), token);
            if (userInfo.getId() == null) {
                log.warn("[购物车服务] 解析用户凭证失败");
                return false;
            }
            userInfoThreadLocal.set(userInfo);

            return true;
        } catch (Exception e) {
            log.error("[购物车服务] 用户权发生异常, ", e);
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        userInfoThreadLocal.remove();
    }

    /**
     * 获取用户信息
     *
     * @return UserInfo 用户信息
     */
    public static UserInfo getUserInfo() {
        return userInfoThreadLocal.get();
    }
}
