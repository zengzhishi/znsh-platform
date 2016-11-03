/*
 * Copyright (c) 2016. Embedded Real-Time Computation Lab Of UESTC.
 *
 * 版权所有：电子科技大学・信息与软件工程学院・嵌入式实时计算研究所（简称ERCL）
 * http://www.is.uestc.edu.cn
 *
 * 未经许可，任何其他组织或个人不得将此程序——
 * 1、用于商业用途。
 * 2、修改或再发布。
 */
package uestc.ercl.znsh.platform.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用于验证应用管理员的拦截器
 */
public class AdminOfAppAuthenticator implements HandlerInterceptor
{
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception
    {
        return true;//TODO 确认页面与后台的认证机制
        //        String tokenString = request.getHeader("TOKEN");
        //        if(true)
        //        {
        //            String appId = "";
        //            request.setAttribute("APP_ID", appId);
        //            return true;
        //        }
        //        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "拒绝访问！");
        //        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
            throws Exception
    {
        String requestMethod = request.getMethod();
        response.setHeader("Access-Control-Allow-Methods", requestMethod);
        switch(requestMethod)
        {
            case "GET":
                response.setHeader("Access-Control-Allow-Origin", "*");
                break;
            case "POST":
                response.setHeader("Access-Control-Allow-Origin", "*");
                response.setHeader("Access-Control-Allow-Headers", "x-requested-with,content-type");
                break;
            case "PUT":
                break;
            case "DELETE":
                break;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception
    {
    }
}