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
package uestc.ercl.znsh.platform.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import uestc.ercl.znsh.common.entity.Cluster;
import uestc.ercl.znsh.common.exception.ZNSH_DataAccessException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * TODO 曾
 */
@Repository
public class ClusterDAO extends SuperDAO
{
    public ClusterDAO(JdbcTemplate jdbcTemplate)
    {
        super(jdbcTemplate);
    }

    /**
     * 插入一个集群
     *
     * @param cluster 集群对象
     *
     * @return 操作是否成功
     *
     * @throws ZNSH_DataAccessException
     */
    public boolean insert(Cluster cluster)
            throws ZNSH_DataAccessException
    {
        if(cluster != null)
        {
            //SQL
            cluster.setPk(DB.clusterMap.size() + 1);
            DB.clusterMap.put(cluster.getPk(), cluster);
            return true;
        }
        return false;
    }

    /**
     * 更新集群信息
     *
     * @param cluster 集群对象
     *
     * @return 操作是否成功
     *
     * @throws ZNSH_DataAccessException
     */
    public boolean update(Cluster cluster)
            throws ZNSH_DataAccessException
    {
        if(cluster != null)
        {
            //SQL
            DB.clusterMap.put(cluster.getPk(), cluster);
            return true;
        }
        return false;
    }

    /**
     * 删除一组集群
     *
     * @param pks 主键数组
     *
     * @return 删除失败的集群的主键
     *
     * @throws ZNSH_DataAccessException
     */
    public Set<String> delete(int[] pks)
            throws ZNSH_DataAccessException
    {
        if(pks != null && pks.length > 0)
        {
            //SQL
            for(int pk : pks)
            {
                DB.clusterMap.remove(pk);
            }
            return null;
        }
        throw new ZNSH_DataAccessException();
    }

    /**
     * 查询集群列表
     *
     * @param pk    主键文本（模糊匹配，null表示不限）
     * @param name  名称（模糊匹配，null表示不限）
     * @param desc  描述（模糊匹配，null表示不限）
     * @param url   地址（模糊匹配，null表示不限）
     * @param from  查询起始位置
     * @param count 查询数量
     *
     * @return
     *
     * @throws ZNSH_DataAccessException
     */
    public List<Cluster> select(String pk, String name, String desc, String url, int from, int count)
            throws ZNSH_DataAccessException
    {
        //SQL
        return DB.clusterMap.values().stream().collect(Collectors.toList());
    }

    /**
     * 检索一个集群
     *
     * @param pk 主键
     *
     * @return 集群对象
     *
     * @throws ZNSH_DataAccessException
     */
    public Cluster get(int pk)
            throws ZNSH_DataAccessException
    {
        if(pk > 0)
        {
            //SQL
            return DB.clusterMap.get(pk);
        }
        return null;
    }
}