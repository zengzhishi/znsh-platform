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
import uestc.ercl.znsh.common.security.AuthResult;
import uestc.ercl.znsh.common.security.Authenticator;
import uestc.ercl.znsh.common.security.TokenRole;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用于验证服务集群的拦截器
 */
public class ClusterMasterAuthenticator implements HandlerInterceptor
{
    /**
     * 解析通信令牌，检查权限。
     *
     * @param request  HTTP请求（检查其头部和参数以确定用户权限）
     * @param response HTTP响应（重置连接、回写消息等）
     * @param handler  忽略不用
     *
     * @return true=允许访问/false=禁止访问
     *
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception
    {
        String tokenString = request.getHeader("TOKEN");
        AuthResult authResult = Authenticator.authenticate(tokenString, TokenRole.MASTER, null);
        if(authResult == AuthResult.PASSED)
        {
            int clusterId = 0;
            request.setAttribute("CLUSTER_ID", clusterId);
            return true;
        }
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "身份认证失败：" + authResult);
        return false;
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
    }
}