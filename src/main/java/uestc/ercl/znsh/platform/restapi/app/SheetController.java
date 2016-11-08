/*
 * Copyright (c) 2016. Embedded Real-Time Computation Lab Of UESTC.
 *
 * 版权所有：电子科技大学・与软件工程学院・嵌入式实时计算研究所（简称ERCL）
 * http://www.is.uestc.edu.cn
 *
 * 未经许可，任何其他组织或个人不得将此程序——
 * 1、用于商业用途。
 * 2、修改或再发布。
 */
package uestc.ercl.znsh.platform.restapi.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import uestc.ercl.znsh.common.entity.Sheet;
import uestc.ercl.znsh.common.exception.ZNSH_IllegalArgumentException;
import uestc.ercl.znsh.common.exception.ZNSH_ServiceException;
import uestc.ercl.znsh.platform.component.SheetManagerImpl;
import uestc.ercl.znsh.platform.component.def.SheetManager;
import uestc.ercl.znsh.platform.restapi.BaseController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping(path = "api/sheet")
public class SheetController extends BaseController
{
    private SheetManager sheetManager;

    @Autowired
    public SheetController(SheetManagerImpl sheetManager)
    {
        Assert.notNull(sheetManager, "SheetManager 注入失败！不能为空！");
        this.sheetManager = sheetManager;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
    public String create(HttpServletRequest request, HttpServletResponse response, @RequestParam String appId, @RequestParam String id,
            @RequestParam String name, String desc)
            throws IOException
    {
        try
        {
            sheetManager.create(appId, id, name, desc);
            return "添加审核报表成功！";
        } catch(ZNSH_IllegalArgumentException e)
        {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "添加审核报表失败！原因：" + e.getMessage());
        } catch(ZNSH_ServiceException e)
        {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "添加审核报表失败！原因：" + e.getMessage());
        }
        return null;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET)
    public List<Sheet> retrieve(HttpServletRequest request, HttpServletResponse response, String id, String name, String desc,
            @RequestParam(defaultValue = "0") Long from, @RequestParam(defaultValue = "20") Integer count)
            throws IOException
    {
        try
        {
            return sheetManager.retrieve(id, name, desc, from, count);
        } catch(ZNSH_IllegalArgumentException e)
        {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "查询审核报表列表失败！原因：" + e.getMessage());
        } catch(ZNSH_ServiceException e)
        {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "查询审核报表列表失败！原因：" + e.getMessage());
        }
        return null;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.PUT, produces = "text/plain;charset=UTF-8")
    public String update(HttpServletRequest request, HttpServletResponse response, @RequestParam Integer pk, String id, String name, String desc)
            throws IOException
    {
        try
        {
            sheetManager.update(pk, name, desc);
            return "修改审核报表成功！";
        } catch(ZNSH_IllegalArgumentException e)
        {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "修改审核报表失败！原因：" + e.getMessage());
        } catch(ZNSH_ServiceException e)
        {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "修改审核报表失败！原因：" + e.getMessage());
        }
        return null;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.DELETE, produces = "text/plain;charset=UTF-8")
    public String delete(HttpServletRequest request, HttpServletResponse response, @RequestParam String pks)
            throws IOException
    {
        String[] array = pks.split("\\|");
        if(array.length > 0)
        {
            try
            {
                long[] clusterPks = new long[array.length];
                for(Integer i = 0; i < array.length; i++)
                {
                    clusterPks[i] = Long.parseLong(array[i]);
                }
                sheetManager.delete(clusterPks);
                return "删除审核报表成功！";
            } catch(NumberFormatException | ZNSH_IllegalArgumentException e)
            {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "删除审核报表失败！原因：" + e.getMessage());
            } catch(ZNSH_ServiceException e)
            {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "删除审核报表失败！原因：" + e.getMessage());
            }
        } else
        {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "删除审核报表失败！原因：请选择至少一个审核报表！");
        }
        return null;
    }
}