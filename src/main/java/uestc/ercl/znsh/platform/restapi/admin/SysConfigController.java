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
package uestc.ercl.znsh.platform.restapi.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import uestc.ercl.znsh.common.entity.Config;
import uestc.ercl.znsh.common.exception.ZNSH_IllegalArgumentException;
import uestc.ercl.znsh.platform.component.SysConfigManagerImpl;
import uestc.ercl.znsh.platform.component.def.SysConfigManager;
import uestc.ercl.znsh.platform.restapi.BaseController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 应用管理接口
 */
@Controller
@RequestMapping(path = "api/admin/config")
public class SysConfigController extends BaseController
{
    private final SysConfigManager sysConfigManager;

    @Autowired
    public SysConfigController(SysConfigManagerImpl sysConfigManager)
    {
        Assert.notNull(sysConfigManager, "SysConfigManager 注入失败！不能为空！");
        this.sysConfigManager = sysConfigManager;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET)
    public List<Config> retrieve(HttpServletRequest request, HttpServletResponse response)
            throws IOException
    {
        return sysConfigManager.list();
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.PUT, produces = "text/plain;charset=UTF-8")
    public String update(HttpServletRequest request, HttpServletResponse response, @RequestParam String name, String value)
            throws IOException
    {
        try
        {
            sysConfigManager.set(name, value);
            return "修改参数信息成功！";
        } catch(ZNSH_IllegalArgumentException e)
        {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "修改参数信息失败！原因：" + e.getMessage());
        }
        return null;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.DELETE, produces = "text/plain;charset=UTF-8")
    public String delete(HttpServletRequest request, HttpServletResponse response, @RequestParam String names)
            throws IOException
    {
        String[] array = names.split("\\|");
        if(array.length > 0)
        {
            try
            {
                for(String name : array)
                {
                    sysConfigManager.set(name, null);
                }
                return "删除参数成功！";
            } catch(ZNSH_IllegalArgumentException e)
            {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "删除参数失败！原因：" + e.getMessage());
            }
        } else
        {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "删除参数失败！原因：请选择至少一个参数！");
        }
        return null;
    }
}