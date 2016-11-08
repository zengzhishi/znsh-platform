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
import uestc.ercl.znsh.common.entity.Cluster;
import uestc.ercl.znsh.common.exception.ZNSH_IllegalArgumentException;
import uestc.ercl.znsh.common.exception.ZNSH_ServiceException;
import uestc.ercl.znsh.platform.component.ClusterManagerImpl;
import uestc.ercl.znsh.platform.component.def.ClusterManager;
import uestc.ercl.znsh.platform.restapi.BaseController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 应用管理接口
 */
@Controller
@RequestMapping(path = "api/admin/cluster")
public class ClusterController extends BaseController
{
    private final ClusterManager clusterManager;

    @Autowired
    public ClusterController(ClusterManagerImpl clusterManager)
    {
        Assert.notNull(clusterManager, "ClusterService 注入失败！不能为空！");
        this.clusterManager = clusterManager;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
    public String create(HttpServletRequest request, HttpServletResponse response, @RequestParam String name, String desc, String url)
            throws IOException
    {
        try
        {
            clusterManager.create(name, desc, url);
            return "添加集群成功！";
        } catch(ZNSH_IllegalArgumentException e)
        {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "添加集群失败！原因：" + e.getMessage());
        } catch(ZNSH_ServiceException e)
        {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "添加集群失败！原因：" + e.getMessage());
        }
        return null;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET)
    public List<Cluster> retrieve(HttpServletRequest request, HttpServletResponse response, String pk, String name, String desc, String url,
            @RequestParam(defaultValue = "0") Long from, @RequestParam(defaultValue = "20") Integer count)
            throws IOException
    {
        try
        {
            return clusterManager.retrieve(pk, name, desc, url, from, count);
        } catch(ZNSH_IllegalArgumentException e)
        {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "查询集群列表失败！原因：" + e.getMessage());
        } catch(ZNSH_ServiceException e)
        {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "查询集群列表失败！原因：" + e.getMessage());
        }
        return null;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.PUT, produces = "text/plain;charset=UTF-8")
    public String update(HttpServletRequest request, HttpServletResponse response, @RequestParam Integer pk, String name, String desc, String url)
            throws IOException
    {
        try
        {
            clusterManager.update(pk, name, desc, url);
            return "修改集群信息成功！";
        } catch(ZNSH_IllegalArgumentException e)
        {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "修改集群信息失败！原因：" + e.getMessage());
        } catch(ZNSH_ServiceException e)
        {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "修改集群信息失败！原因：" + e.getMessage());
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
                int[] clusterPks = new int[array.length];
                for(Integer i = 0; i < array.length; i++)
                {
                    clusterPks[i] = Integer.parseInt(array[i]);
                }
                clusterManager.delete(clusterPks);
                return "删除集群成功！";
            } catch(NumberFormatException | ZNSH_IllegalArgumentException e)
            {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "删除集群失败！原因：" + e.getMessage());
            } catch(ZNSH_ServiceException e)
            {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "删除集群失败！原因：" + e.getMessage());
            }
        } else
        {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "删除集群失败！原因：请选择至少一个集群！");
        }
        return null;
    }
}