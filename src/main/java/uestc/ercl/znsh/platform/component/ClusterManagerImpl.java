/*
 * Copyright (c) 2016. Embedded Real-Time Computation Lab Of UESTC.
 *
 * 电子科技大学・信息与软件工程学院・嵌入式实时计算研究所
 *
 * http://www.is.uestc.edu.cn
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
import uestc.ercl.znsh.common.exception.ZNSH_IllegalFieldValueException;
import uestc.ercl.znsh.common.exception.ZNSH_ServiceException;
import uestc.ercl.znsh.platform.component.def.AppManager;
import uestc.ercl.znsh.platform.component.def.ClusterManager;
import uestc.ercl.znsh.platform.dao.ClusterDAO;
import uestc.ercl.znsh.platform.util.ArgValidator;

import java.util.List;
import java.util.Set;

@Component
public class ClusterManagerImpl implements ClusterManager
{
    private static final Logger LOGGER = LoggerFactory.getLogger(AppManager.class);
    private final ClusterDAO clusterDAO;

    public ClusterManagerImpl(ClusterDAO clusterDAO)
    {
        Assert.notNull(clusterDAO, "ClusterDAO注入失败！不能为空！");
        this.clusterDAO = clusterDAO;
    }

    @Override
    public void create(@NonNull String name, @Nullable String desc, @Nullable String url)
            throws ZNSH_IllegalFieldValueException, ZNSH_ServiceException
    {
        ArgValidator.checkArgNonnull(name, "集群名称");
    }

    @Override
    public Cluster retrieve(int pk)
            throws ZNSH_IllegalFieldValueException, ZNSH_ServiceException
    {
        return null;
    }

    @Override
    public List<Cluster> retrieve(@Nullable String pk, @Nullable String name, @Nullable String desc, @Nullable String url, int from, int count)
            throws ZNSH_IllegalFieldValueException, ZNSH_ServiceException
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
            throws ZNSH_IllegalFieldValueException, ZNSH_ServiceException
    {
        Cluster cluster = new Cluster();
        cluster.setName(name);
        cluster.setDesc(desc);
        cluster.setUrl(url);
        try
        {
            clusterDAO.update(cluster);
        } catch(ZNSH_DataAccessException e)
        {
            LOGGER.error("更新集群失败！");
            throw new ZNSH_ServiceException(e);
        }
    }

    @Override
    public Set<String> delete(@NonNull int[] pks)
            throws ZNSH_IllegalFieldValueException, ZNSH_ServiceException
    {
        if(pks == null || pks.length <= 0)
        {
            throw new ZNSH_IllegalFieldValueException("未选择集群！");
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
