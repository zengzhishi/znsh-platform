/*
 * Copyright (c) 2016. Embedded Real-Time Computation Lab Of UESTC.
 *
 * 电子科技大学・信息与软件工程学院・嵌入式实时计算研究所
 *
 * http://www.is.uestc.edu.cn
 */
package uestc.ercl.znsh.platform.restapi.app;

import cn.sel.jutil.lang.JText;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import uestc.ercl.znsh.common.entity.Config;
import uestc.ercl.znsh.common.exception.ZNSH_IllegalFieldValueException;
import uestc.ercl.znsh.platform.component.AppManagerImpl;
import uestc.ercl.znsh.platform.component.def.AppManager;
import uestc.ercl.znsh.platform.restapi.BaseController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(path = "api/config")
public class AppConfigController extends BaseController
{
    private final AppManager appManager;

    @Autowired
    public AppConfigController(AppManagerImpl appManager)
    {
        Assert.notNull(appManager, "AppService注入失败！不能为空！");
        this.appManager = appManager;
    }

    @ResponseBody
    @RequestMapping(path = "set", method = RequestMethod.PUT)
    public String set(HttpServletRequest request, HttpServletResponse response, @RequestParam String name, @RequestParam String value)
            throws IOException
    {
        String appId = getAppId(request);
        if(JText.isNormal(appId))
        {
            try
            {
                Config config = new Config();
                config.setName(name);
                config.setValue(value);
                config.setDatetime(new Date());
                boolean success = appManager.setConfig(appId, config);
                if(success)
                {
                    return "设置配置成功！";
                } else
                {
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "设置配置失败！");
                }
            } catch(ZNSH_IllegalFieldValueException e)
            {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "系统异常！");
            }
        } else
        {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "系统异常！");
        }
        return null;
    }

    @ResponseBody
    @RequestMapping(path = "find", method = RequestMethod.GET)
    public Config get(HttpServletRequest request, HttpServletResponse response, @RequestParam String name)
            throws IOException
    {
        String appId = getAppId(request);
        if(JText.isNormal(appId))
        {
            Config config = appManager.getConfig(appId, name);
            if(config != null)
            {
                return config;
            } else
            {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "获取配置失败！");
            }
        } else
        {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "系统异常！");
        }
        return null;
    }

    @ResponseBody
    @RequestMapping(path = "list", method = RequestMethod.GET)
    public List<Config> list(HttpServletRequest request, HttpServletResponse response)
            throws IOException
    {
        String appId = getAppId(request);
        if(JText.isNormal(appId))
        {
            List<Config> list = appManager.getConfigList(appId);
            if(list != null && !list.isEmpty())
            {
                return list;
            } else
            {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "获取配置列表失败！");
            }
        } else
        {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "系统异常！");
        }
        return null;
    }
}