/*
 * Copyright (c) 2016. Embedded Real-Time Computation Lab Of UESTC.
 *
 * 电子科技大学・信息与软件工程学院・嵌入式实时计算研究所
 *
 * http://www.is.uestc.edu.cn
 */
package uestc.ercl.znsh.platform.restapi.admin;

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
import uestc.ercl.znsh.common.exception.ZNSH_IllegalFieldValueException;
import uestc.ercl.znsh.common.exception.ZNSH_ServiceException;
import uestc.ercl.znsh.platform.component.AppManagerImpl;
import uestc.ercl.znsh.platform.component.def.AppManager;
import uestc.ercl.znsh.platform.restapi.BaseController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 应用管理接口
 */
@Controller
@RequestMapping(path = "api/admin/app")
public class AppController extends BaseController
{
    private final AppManager appManager;

    @Autowired
    public AppController(AppManagerImpl appManager)
    {
        Assert.notNull(appManager, "AppService注入失败！不能为空！");
        this.appManager = appManager;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST)
    public String create(HttpServletRequest request, HttpServletResponse response, @RequestParam String name, String desc, @RequestParam int type,
            @RequestParam String master, @RequestParam String pid, @RequestParam String phone, String email, @RequestParam String account,
            @RequestParam String password)
            throws IOException
    {
        AppType appType = AppType.valueOf(type);
        if(appType != null)
        {
            try
            {
                appManager.create(name, desc, appType, AppStatus.NEW, master, pid, phone, email, account, password);
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
                return "添加应用成功！";
            } catch(ZNSH_IllegalFieldValueException e)
            {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "添加应用失败！原因：" + e.getMessage());
            } catch(ZNSH_ServiceException e)
            {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "添加应用失败！原因：" + e.getMessage());
            }
        } else
        {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "添加应用失败！应用类型不正确！");
        }
        return null;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET)
    public List<App> retrieve(HttpServletRequest request, HttpServletResponse response, String pk, String name, AppType type, AppStatus status,
            String master, String pid, String phone, String email, int from, int count)
            throws IOException
    {
        try
        {
            return appManager.find(pk, name, type, status, master, pid, phone, email, from, count);
        } catch(ZNSH_IllegalFieldValueException e)
        {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "查询应用列表失败！原因：" + e.getMessage());
        } catch(ZNSH_ServiceException e)
        {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "查询应用列表失败！原因：" + e.getMessage());
        }
        return null;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.PUT)
    public String update(HttpServletRequest request, HttpServletResponse response, @RequestParam String pk, String name, String desc, String master,
            String pid, String phone, String email, String account)
            throws IOException
    {
        try
        {
            appManager.update(pk, name, desc, master, pid, phone, email, account);
            return "修改应用信息成功！";
        } catch(ZNSH_IllegalFieldValueException e)
        {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "修改应用信息失败！原因：" + e.getMessage());
        } catch(ZNSH_ServiceException e)
        {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "修改应用信息失败！原因：" + e.getMessage());
        }
        return null;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.DELETE)
    public String delete(HttpServletRequest request, HttpServletResponse response, @RequestParam String pks)
            throws IOException
    {
        String[] appIds = pks.split("\\|");
        if(appIds.length > 0)
        {
            try
            {
                appManager.delete(appIds);
                return "删除应用成功！";
            } catch(ZNSH_IllegalFieldValueException e)
            {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "删除应用失败！原因：" + e.getMessage());
            } catch(ZNSH_ServiceException e)
            {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "删除应用失败！原因：" + e.getMessage());
            }
        } else
        {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "删除应用失败！原因：请选择至少一个应用！");
        }
        return null;
    }

    @ResponseBody
    @RequestMapping(path = "disable", method = RequestMethod.POST)
    public String disable(HttpServletRequest request, HttpServletResponse response, @RequestParam String pks)
            throws IOException
    {
        String[] appIds = pks.split("\\|");
        if(appIds.length > 0)
        {
            try
            {
                appManager.disable(appIds);
                return "注销应用成功！";
            } catch(ZNSH_IllegalFieldValueException e)
            {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "注销应用失败！原因：" + e.getMessage());
            } catch(ZNSH_ServiceException e)
            {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "注销应用失败！原因：" + e.getMessage());
            }
        } else
        {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "注销应用失败！原因：请选择至少一个应用！");
        }
        return null;
    }

    @ResponseBody
    @RequestMapping(path = "activate", method = RequestMethod.POST)
    public String activate(HttpServletRequest request, HttpServletResponse response, @RequestParam String pks)
            throws IOException
    {
        String[] appIds = pks.split("\\|");
        if(appIds.length > 0)
        {
            try
            {
                appManager.activate(appIds);
                return "激活应用成功！";
            } catch(ZNSH_IllegalFieldValueException e)
            {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "激活应用失败！原因：" + e.getMessage());
            } catch(ZNSH_ServiceException e)
            {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "激活应用失败！原因：" + e.getMessage());
            }
        } else
        {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "激活应用失败！原因：请选择至少一个应用！");
        }
        return null;
    }

    @ResponseBody
    @RequestMapping(path = "suspend", method = RequestMethod.POST)
    public String suspend(HttpServletRequest request, HttpServletResponse response, @RequestParam String pks)
            throws IOException
    {
        String[] appIds = pks.split("\\|");
        if(appIds.length > 0)
        {
            try
            {
                appManager.suspend(appIds);
                return "挂起应用成功！";
            } catch(ZNSH_IllegalFieldValueException e)
            {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "挂起应用失败！原因：" + e.getMessage());
            } catch(ZNSH_ServiceException e)
            {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "挂起应用失败！原因：" + e.getMessage());
            }
        } else
        {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "挂起应用失败！原因：请选择至少一个应用！");
        }
        return null;
    }

    @ResponseBody
    @RequestMapping(path = "review", method = RequestMethod.POST)
    public String review(HttpServletRequest request, HttpServletResponse response, @RequestParam String pks, @RequestParam Boolean accept)
            throws IOException
    {
        String[] appIds = pks.split("\\|");
        if(appIds.length > 0)
        {
            try
            {
                appManager.review(accept, appIds);
                return "审批应用成功！";
            } catch(ZNSH_IllegalFieldValueException e)
            {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "审批应用失败！原因：" + e.getMessage());
            } catch(ZNSH_ServiceException e)
            {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "审批应用失败！原因：" + e.getMessage());
            }
        } else
        {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "审批应用失败！原因：请选择至少一个应用！");
        }
        return null;
    }

    @ResponseBody
    @RequestMapping(path = "cluster", method = RequestMethod.POST)
    public String cluster(HttpServletRequest request, HttpServletResponse response, @RequestParam String pks, @RequestParam Integer clusterPk)
            throws IOException
    {
        String[] appIds = pks.split("\\|");
        if(appIds.length > 0)
        {
            try
            {
                appManager.setCluster(clusterPk, appIds);
                return "分配集群成功！";
            } catch(ZNSH_IllegalFieldValueException e)
            {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "分配集群失败！原因：" + e.getMessage());
            } catch(ZNSH_ServiceException e)
            {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "分配集群失败！原因：" + e.getMessage());
            }
        } else
        {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "分配集群失败！原因：请选择至少一个应用！");
        }
        return null;
    }
}