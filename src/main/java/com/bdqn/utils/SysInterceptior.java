package com.bdqn.utils;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截处理器
 */
public class SysInterceptior implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取 session
        Object devUserSession = request.getSession().getAttribute("devUserSession");
        if (devUserSession != null) {
            return true;
        }
        // 没有登录就跳转 index.jsp页面
        response.sendRedirect("/index.jsp");
        return false;
    }
}
