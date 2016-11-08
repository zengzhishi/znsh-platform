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
package uestc.ercl.znsh.platform.restapi.service;

import cn.sel.jutil.security.SecureUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import uestc.ercl.znsh.common.constant.AppStatus;
import uestc.ercl.znsh.common.constant.AppType;
import uestc.ercl.znsh.common.entity.App;
import uestc.ercl.znsh.common.exception.ZNSH_IllegalArgumentException;
import uestc.ercl.znsh.common.exception.ZNSH_ServiceException;
import uestc.ercl.znsh.platform.component.AppManagerImpl;
import uestc.ercl.znsh.platform.component.VerifyManagerImpl;
import uestc.ercl.znsh.platform.component.def.AppManager;
import uestc.ercl.znsh.platform.component.def.VerifyManager;
import uestc.ercl.znsh.platform.constant.AppConfigName;
import uestc.ercl.znsh.platform.restapi.BaseController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 公开接口
 */
@Controller
@RequestMapping(path = "api")
public class OpenController extends BaseController
{
    private final AppManager appManager;
    private final VerifyManager verifyManager;

    @Autowired
    public OpenController(AppManagerImpl appManager, VerifyManagerImpl verifyManager)
    {
        Assert.notNull(appManager, "AppService 注入失败！不能为空！");
        Assert.notNull(verifyManager, "VerifierService 注入失败！不能为空！");
        this.appManager = appManager;
        this.verifyManager = verifyManager;
    }

    @ResponseBody
    @RequestMapping(path = "signup", method = RequestMethod.POST)
    public String signup(HttpServletRequest request, HttpServletResponse response, @RequestParam String name, String desc, @RequestParam int type,
            @RequestParam String master, @RequestParam String pid, @RequestParam String phone, String email, @RequestParam String account,
            @RequestParam String pwd1, @RequestParam String pwd2)
            throws IOException
    {
        AppType appType = AppType.valueOf(type);
        if(appType != null)
        {
            if(pwd1.equals(pwd2))
            {
                try
                {
                    try
                    {
                        appManager.create(name, desc, appType, AppStatus.NEW, master, pid, phone, email, account, pwd1);
                        switch(appType)
                        {
                            case CLOUD:
                                response.setStatus(HttpServletResponse.SC_OK);
                                break;
                            case STANDALONE:
                                response.setStatus(HttpServletResponse.SC_CREATED);
                                break;
                            case OFFLINE:
                                response.setStatus(HttpServletResponse.SC_ACCEPTED);
                                break;
                        }
                        return "注册成功！";
                    } catch(ZNSH_IllegalArgumentException e)
                    {
                        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "注册应用失败！");
                    }
                } catch(ZNSH_ServiceException e)
                {
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "注册应用失败！");
                }
            } else
            {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "两次输入的密码不一致！");
            }
        } else
        {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "应用类型不正确！");
        }
        return null;
    }

    @ResponseBody
    @RequestMapping(path = "signin", method = RequestMethod.POST)
    public String signin(HttpServletRequest request, HttpServletResponse response, @RequestParam String key, @RequestParam String password)
            throws IOException
    {
        App app = null;
        try
        {
            app = appManager.find(key);
        } catch(ZNSH_IllegalArgumentException e)
        {
            e.printStackTrace();
        } catch(ZNSH_ServiceException e)
        {
            e.printStackTrace();
        }
        if(app != null)
        {
            boolean success = false;
            String appPassword = app.getPassword();
            if(!appPassword.equals(password))
            {
                String md5 = SecureUtil.getMD5_32(password);
                if(md5 != null && md5.length() == 32 && appPassword.equals(md5))
                {
                    success = true;
                }
            } else
            {
                success = true;
            }
            if(success)
            {
                response.setHeader("TOKEN", appManager.getConfig(app.appId(), AppConfigName.TOKEN).getValue());
                return "登录成功！";
            }
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "登录失败，系统异常！");
        } else
        {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "登录失败，账号不存在！");
        }
        return null;
    }

    @ResponseBody
    @RequestMapping(path = "signout", method = RequestMethod.POST)
    public String signout(HttpServletRequest request, HttpServletResponse response)
    {
        return "您已成功退出！";
    }

    @ResponseBody
    @RequestMapping(path = "password", method = RequestMethod.POST)
    public String password(HttpServletRequest request, HttpServletResponse response, @RequestParam String key, @RequestParam String code,
            @RequestParam String pwd1, @RequestParam String pwd2)
            throws IOException
    {
        App app = null;
        try
        {
            app = appManager.find(key);
        } catch(ZNSH_IllegalArgumentException e)
        {
            e.printStackTrace();
        } catch(ZNSH_ServiceException e)
        {
            e.printStackTrace();
        }
        if(app != null)
        {
            String appId = app.appId();
            switch(verifyManager.checkAppVerifyCode(appId, code))
            {
                case VALID:
                    if(pwd1.equals(pwd2))
                    {
                        try
                        {
                            appManager.updatePassword(appId, pwd1);
                        } catch(ZNSH_IllegalArgumentException e)
                        {
                            e.printStackTrace();
                        } catch(ZNSH_ServiceException e)
                        {
                            e.printStackTrace();
                        }
                        return "密码修改成功！";
                    }
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "两次输入的密码不一致！");
                    break;
                case INVALID:
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "验证码不正确！");
                    break;
                case EXPIRED:
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "验证码已过期！");
                    break;
            }
        } else
        {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "账户不存在！");
        }
        return null;
    }

    @ResponseBody
    @RequestMapping(path = "verify", method = RequestMethod.POST)
    public String verify(HttpServletRequest request, HttpServletResponse response, @RequestParam String key, @RequestParam Integer by)
            throws IOException
    {
        App app = null;
        try
        {
            app = appManager.find(key);
        } catch(ZNSH_IllegalArgumentException e)
        {
            e.printStackTrace();
        } catch(ZNSH_ServiceException e)
        {
            e.printStackTrace();
        }
        if(app != null)
        {
            if(by == 0)
            {
                String phone = app.getPhone();
                if(phone != null)
                {
                    if(verifyManager.verifyBySMS(phone))
                    {
                        return "已发送验证短信！";
                    }
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "验证短信发送失败！");
                } else
                {
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "应用异常！未找到注册手机！");
                }
            } else
            {
                String email = app.getEmail();
                if(email != null)
                {
                    if(verifyManager.verifyByEmail(email))
                    {
                        return "已发送验证邮件！";
                    }
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "验证邮件发送失败！");
                } else
                {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "该应用未登记邮箱！");
                }
            }
        } else
        {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "账户不存在！");
        }
        return null;
    }
}