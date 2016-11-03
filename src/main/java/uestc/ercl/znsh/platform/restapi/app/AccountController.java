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
package uestc.ercl.znsh.platform.restapi.app;

import cn.sel.jutil.lang.JText;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import uestc.ercl.znsh.common.entity.App;
import uestc.ercl.znsh.common.entity.Config;
import uestc.ercl.znsh.common.exception.ZNSH_IllegalArgumentException;
import uestc.ercl.znsh.common.exception.ZNSH_ServiceException;
import uestc.ercl.znsh.common.security.Token;
import uestc.ercl.znsh.common.security.TokenRole;
import uestc.ercl.znsh.platform.component.AppManagerImpl;
import uestc.ercl.znsh.platform.component.def.AppManager;
import uestc.ercl.znsh.platform.constants.AppConfigName;
import uestc.ercl.znsh.platform.restapi.BaseController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * 应用管理接口
 */
@Controller
@RequestMapping(path = "api/")
public class AccountController extends BaseController
{
    private final AppManager appManager;

    @Autowired
    public AccountController(AppManagerImpl appManager)
    {
        Assert.notNull(appManager, "AppService注入失败！不能为空！");
        this.appManager = appManager;
    }

    @ResponseBody
    @RequestMapping(path = "app", method = RequestMethod.GET)
    public App retrieve(HttpServletRequest request, HttpServletResponse response, @RequestAttribute String appId)
            throws IOException
    {
        if(JText.isNormal(appId))
        {
            App app = null;
            try
            {
                app = appManager.get(appId);
            } catch(ZNSH_IllegalArgumentException e)
            {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "获取应用信息失败！原因：" + e.getMessage());
            } catch(ZNSH_ServiceException e)
            {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "获取应用信息失败！原因：" + e.getMessage());
            }
            if(app != null)
            {
                return app;
            }
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "获取应用信息失败！");
        } else
        {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "系统异常！");
        }
        return null;
    }

    @ResponseBody
    @RequestMapping(path = "app", method = RequestMethod.PUT)
    public String update(HttpServletRequest request, HttpServletResponse response, @RequestAttribute String appId, String name, String desc,
            String master, String pid, String phone, String email, String account)
            throws IOException
    {
        if(JText.isNormal(appId))
        {
            try
            {
                appManager.update(appId, name, desc, master, pid, phone, email, account);
                return "修改应用信息成功！";
            } catch(ZNSH_IllegalArgumentException e)
            {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "修改应用信息失败！原因：" + e.getMessage());
            } catch(ZNSH_ServiceException e)
            {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "修改应用信息失败！原因：" + e.getMessage());
            }
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "更新应用失败！");
        } else
        {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "系统异常！");
        }
        return null;
    }

    @ResponseBody
    @RequestMapping(path = "password", method = RequestMethod.PUT)
    public String password(HttpServletRequest request, HttpServletResponse response, @RequestAttribute String appId, @RequestParam String origPwd,
            @RequestParam String newPwd)
            throws IOException
    {
        if(JText.isNormal(appId))
        {
            try
            {
                App app = appManager.get(appId);
                if(app != null)
                {
                    if(app.getPassword().equals(origPwd))
                    {
                        appManager.updatePassword(appId, newPwd);
                        return "修改密码成功！";
                    } else
                    {
                        response.sendError(HttpServletResponse.SC_FORBIDDEN, "修改密码失败！原因：原密码错误！");
                    }
                } else
                {
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "修改密码失败！原因：系统异常！");
                }
            } catch(ZNSH_IllegalArgumentException e)
            {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "修改密码失败！原因：" + e.getMessage());
            } catch(ZNSH_ServiceException e)
            {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "修改密码失败！原因：" + e.getMessage());
            }
        } else
        {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "系统异常！");
        }
        return null;
    }

    @ResponseBody
    @RequestMapping(path = "activate", method = RequestMethod.POST)
    public String activate(HttpServletRequest request, HttpServletResponse response, @RequestAttribute String appId)
            throws IOException
    {
        if(JText.isNormal(appId))
        {
            try
            {
                appManager.activate(appId);
                return "激活应用成功！";
            } catch(ZNSH_IllegalArgumentException e)
            {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "激活应用失败！原因：" + e.getMessage());
            } catch(ZNSH_ServiceException e)
            {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "激活应用失败！原因：" + e.getMessage());
            }
        } else
        {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "系统异常！");
        }
        return null;
    }

    @ResponseBody
    @RequestMapping(path = "suspend", method = RequestMethod.POST)
    public String suspend(HttpServletRequest request, HttpServletResponse response, @RequestAttribute String appId)
            throws IOException
    {
        if(JText.isNormal(appId))
        {
            try
            {
                appManager.suspend(appId);
                return "挂起应用成功！";
            } catch(ZNSH_IllegalArgumentException e)
            {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "挂起应用失败！原因：" + e.getMessage());
            } catch(ZNSH_ServiceException e)
            {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "挂起应用失败！原因：" + e.getMessage());
            }
        } else
        {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "系统异常！");
        }
        return null;
    }

    @ResponseBody
    @RequestMapping(path = "token", method = RequestMethod.PUT)
    public String token(HttpServletRequest request, HttpServletResponse response, @RequestAttribute String appId, @RequestParam String key)
            throws IOException
    {
        if(JText.isNormal(appId))
        {
            Token token = new Token(appId, TokenRole.TERMINAL, key);
            try
            {
                Config config = new Config(AppConfigName.TOKEN, token.token(), new Date());
                boolean success = appManager.setConfig(appId, config);
                if(success)
                {
                    return "设置TOKEN成功！";
                }
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "设置TOKEN失败！");
            } catch(ZNSH_IllegalArgumentException e)
            {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "无法设置TOKEN！原因：");
            }
        } else
        {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "系统异常！");
        }
        return null;
    }
}