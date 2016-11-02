/*
 * Copyright (c) 2016. Embedded Real-Time Computation Lab Of UESTC.
 *
 * 电子科技大学・信息与软件工程学院・嵌入式实时计算研究所
 *
 * http://www.is.uestc.edu.cn
 */
package uestc.ercl.znsh.platform.restapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uestc.ercl.znsh.common.constant.Regular;

import javax.servlet.http.HttpServletRequest;

/**
 * 所有Controller的基类（任何Controller都必须继承自此类）
 */
public abstract class BaseController
{
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected int getAdminId(HttpServletRequest request)
    {
        Object obj = request.getAttribute("ADMIN_ID");
        if(obj != null)
        {
            try
            {
                return Integer.parseInt(String.valueOf(obj));
            } catch(NumberFormatException e)
            {
                logger.error("获取来访的管理员编号出错！", e);
            }
        }
        return -1;
    }

    protected String getAppId(HttpServletRequest request)
    {
        Object obj = request.getAttribute("APP_ID");
        if(obj != null)
        {
            String appId = String.valueOf(obj);
            if(Regular.match(appId, Regular.APP_ID))
            {
                return appId;
            } else
            {
                logger.error("获取来访的应用编号出错！");
            }
        }
        return null;
    }

    protected int getClusterId(HttpServletRequest request)
    {
        Object obj = request.getAttribute("CLUSTER_ID");
        if(obj != null)
        {
            try
            {
                return Integer.parseInt(String.valueOf(obj));
            } catch(NumberFormatException e)
            {
                logger.error("获取来访的集群编号出错！", e);
            }
        }
        return -1;
    }
}