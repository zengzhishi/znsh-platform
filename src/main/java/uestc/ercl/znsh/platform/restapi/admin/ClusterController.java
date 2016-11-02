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
import uestc.ercl.znsh.common.entity.Cluster;
import uestc.ercl.znsh.common.exception.ZNSH_IllegalFieldValueException;
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
@RequestMapping(path = "api/admin")
public class ClusterController extends BaseController
{
    private final ClusterManager clusterManager;

    @Autowired
    public ClusterController(ClusterManagerImpl clusterManager)
    {
        Assert.notNull(clusterManager, "ClusterService注入失败！不能为空！");
        this.clusterManager = clusterManager;
    }

    @ResponseBody
    @RequestMapping(path = "cluster", method = RequestMethod.POST)
    public String create(HttpServletRequest request, HttpServletResponse response, @RequestParam String name, String desc, String url)
            throws IOException
    {
        try
        {
            clusterManager.create(name, desc, url);
            return "添加集群成功！";
        } catch(ZNSH_IllegalFieldValueException e)
        {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "添加集群失败！原因：" + e.getMessage());
        } catch(ZNSH_ServiceException e)
        {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "添加集群失败！原因：" + e.getMessage());
        }
        return null;
    }

    @ResponseBody
    @RequestMapping(path = "cluster", method = RequestMethod.GET)
    public Cluster retrieve(HttpServletRequest request, HttpServletResponse response, int pk)
            throws IOException
    {
        try
        {
            return clusterManager.retrieve(pk);
        } catch(ZNSH_IllegalFieldValueException e)
        {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "查询集群信息失败！原因：" + e.getMessage());
        } catch(ZNSH_ServiceException e)
        {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "查询集群信息失败！原因：" + e.getMessage());
        }
        return null;
    }

    @ResponseBody
    @RequestMapping(path = "clusters", method = RequestMethod.GET)
    public List<Cluster> retrieve(HttpServletRequest request, HttpServletResponse response, String pk, String name, String desc, String url, int from,
            int count)
            throws IOException
    {
        try
        {
            return clusterManager.retrieve(pk, name, desc, url, from, count);
        } catch(ZNSH_IllegalFieldValueException e)
        {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "查询集群列表失败！原因：" + e.getMessage());
        } catch(ZNSH_ServiceException e)
        {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "查询集群列表失败！原因：" + e.getMessage());
        }
        return null;
    }

    @ResponseBody
    @RequestMapping(path = "cluster", method = RequestMethod.PUT)
    public String update(HttpServletRequest request, HttpServletResponse response, @RequestParam int pk, String name, String desc, String url)
            throws IOException
    {
        try
        {
            clusterManager.update(pk, name, desc, url);
            return "修改集群信息成功！";
        } catch(ZNSH_IllegalFieldValueException e)
        {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "修改集群信息失败！原因：" + e.getMessage());
        } catch(ZNSH_ServiceException e)
        {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "修改集群信息失败！原因：" + e.getMessage());
        }
        return null;
    }

    @ResponseBody
    @RequestMapping(path = "cluster", method = RequestMethod.DELETE)
    public String delete(HttpServletRequest request, HttpServletResponse response, @RequestParam String pks)
            throws IOException
    {
        String[] array = pks.split("\\|");
        if(array.length > 0)
        {
            try
            {
                int[] clusterPks = new int[array.length];
                for(int i = 0; i < array.length; i++)
                {
                    clusterPks[i] = Integer.parseInt(array[i]);
                }
                clusterManager.delete(clusterPks);
                return "删除集群成功！";
            } catch(NumberFormatException | ZNSH_IllegalFieldValueException e)
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