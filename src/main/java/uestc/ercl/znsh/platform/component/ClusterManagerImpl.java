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
package uestc.ercl.znsh.platform.component;

import cn.sel.jutil.annotation.note.NonNull;
import cn.sel.jutil.annotation.note.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import uestc.ercl.znsh.common.entity.Cluster;
import uestc.ercl.znsh.common.exception.ZNSH_DataAccessException;
import uestc.ercl.znsh.common.exception.ZNSH_IllegalArgumentException;
import uestc.ercl.znsh.common.exception.ZNSH_ServiceException;
import uestc.ercl.znsh.platform.component.def.AppManager;
import uestc.ercl.znsh.platform.component.def.ClusterManager;
import uestc.ercl.znsh.platform.dao.ClusterDAO;
import uestc.ercl.znsh.platform.util.ArgValidator;

import java.util.List;
import java.util.Set;

@Component
public class ClusterManagerImpl extends _SysLoggerHolder implements ClusterManager
{
    private static final Logger LOGGER = LoggerFactory.getLogger(AppManager.class);
    private final ClusterDAO clusterDAO;

    public ClusterManagerImpl(ClusterDAO clusterDAO)
    {
        Assert.notNull(clusterDAO, "ClusterDAO 注入失败！不能为空！");
        this.clusterDAO = clusterDAO;
    }

    @Override
    public void create(@NonNull String name, @Nullable String desc, @Nullable String url)
            throws ZNSH_IllegalArgumentException, ZNSH_ServiceException
    {
        ArgValidator.checkArgNonnull(name, "集群名称");
        try
        {
            Cluster cluster = new Cluster(name, desc, url);
            clusterDAO.insert(cluster);
        } catch(ZNSH_DataAccessException e)
        {
            LOGGER.error("创建集群失败！");
            throw new ZNSH_ServiceException(e);
        }
    }

    @Override
    public Cluster retrieve(int pk)
            throws ZNSH_IllegalArgumentException, ZNSH_ServiceException
    {
        try
        {
            List<Cluster> list = clusterDAO.select(String.valueOf(pk), null, null, null, 0, 0);
            if(list != null)
            {
                if(list.size() == 1)
                {
                    return list.get(0);
                } else
                {
                    throw new ZNSH_ServiceException("查询集群列表失败！持久化数据异常！");
                }
            } else
            {
                throw new ZNSH_ServiceException("查询集群列表失败！指定集群不存在！");
            }
        } catch(ZNSH_DataAccessException e)
        {
            LOGGER.error("查询集群列表失败！数据访问未完成！");
            throw new ZNSH_ServiceException(e);
        }
    }

    @Override
    public List<Cluster> retrieve(@Nullable String pk, @Nullable String name, @Nullable String desc, @Nullable String url, long from, int count)
            throws ZNSH_IllegalArgumentException, ZNSH_ServiceException
    {
        if(from < 0)
        {
            from = 0;
        }
        if(count < 1)
        {
            count = 20;
        }
        try
        {
            return clusterDAO.select(pk, name, desc, url, from, count);
        } catch(ZNSH_DataAccessException e)
        {
            LOGGER.error("查找集群失败！");
            throw new ZNSH_ServiceException(e);
        }
    }

    @Override
    public void update(int pk, @Nullable String name, @Nullable String desc, @Nullable String url)
            throws ZNSH_IllegalArgumentException, ZNSH_ServiceException
    {
        try
        {
            Cluster cluster = new Cluster();
            cluster.update(pk, name, desc, url);
            clusterDAO.update(cluster);
        } catch(ZNSH_DataAccessException e)
        {
            LOGGER.error("更新集群失败！");
            throw new ZNSH_ServiceException(e);
        }
    }

    @Override
    public Set<String> delete(@NonNull int... pks)
            throws ZNSH_IllegalArgumentException, ZNSH_ServiceException
    {
        if(pks == null || pks.length <= 0)
        {
            throw new ZNSH_IllegalArgumentException("未选择集群！");
        } else
        {
            try
            {
                Set<String> fails = clusterDAO.delete(pks);
                return fails != null && !fails.isEmpty() ? fails : null;
            } catch(ZNSH_DataAccessException e)
            {
                LOGGER.error("删除集群失败！");
                throw new ZNSH_ServiceException(e);
            }
        }
    }
}
