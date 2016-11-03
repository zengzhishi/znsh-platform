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

import cn.sel.jutil.lang.JText;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import uestc.ercl.znsh.common.data.JsonUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 日志记录拦截器
 *
 * @apiNote 程序日志，不是系统日志。
 */
public class AccessLogger implements HandlerInterceptor
{
    private static final Logger LOGGER = LoggerFactory.getLogger("ZNSH系统访问日志");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception
    {
        i(request);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
            throws Exception
    {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception
    {
        o(request, response);
    }

    private String getRealIPAddress(HttpServletRequest request)
    {
        String ipFromProxy = request.getHeader("X-Real-IP");
        return JText.isNullOrEmpty(ipFromProxy) ? request.getRemoteAddr() : ipFromProxy;
    }

    private String getHeaders(HttpServletRequest request)
    {
        Enumeration<String> headerNames = request.getHeaderNames();
        List<String> result = new ArrayList<>();
        while(headerNames.hasMoreElements())
        {
            String name = headerNames.nextElement();
            result.add(String.format("%s=%s", name, getEnumerationString(request.getHeaders(name))));
        }
        return Arrays.toString(result.toArray());
    }

    private String getHeaders(HttpServletResponse response)
    {
        Iterator<String> headerNames = response.getHeaderNames().iterator();
        List<String> result = new ArrayList<>();
        while(headerNames.hasNext())
        {
            String name = headerNames.next();
            result.add(String.format("%s=%s", name, response.getHeader(name)));
        }
        return Arrays.toString(result.toArray());
    }

    private String getEnumerationString(Enumeration enumeration)
    {
        List<String> result = new ArrayList<>();
        while(enumeration.hasMoreElements())
        {
            result.add(String.valueOf(enumeration.nextElement()));
        }
        return Arrays.toString(result.toArray());
    }

    private void i(HttpServletRequest request)
    {
        LOGGER.info("{} -> {}\t[{}]\tToken:{}\tHeaders:{}\tParameters:{}", getRealIPAddress(request), request.getRequestURI(), request.getMethod(),
                request.getHeader("Token"), getHeaders(request), JsonUtil.getJson(request.getParameterMap()));
    }

    private void o(HttpServletRequest request, HttpServletResponse response)
    {
        LOGGER.info("{} -> {}\t[{}]\tStatus:{}\tHeaders:{}", getRealIPAddress(request), request.getRequestURI(), request.getMethod(),
                response.getStatus(), getHeaders(response));
    }
}