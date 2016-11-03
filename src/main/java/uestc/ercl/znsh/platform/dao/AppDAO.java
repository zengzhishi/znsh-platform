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

import cn.sel.jutil.lang.JText;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import uestc.ercl.znsh.common.constant.AppStatus;
import uestc.ercl.znsh.common.constant.AppType;
import uestc.ercl.znsh.common.entity.App;
import uestc.ercl.znsh.common.exception.ZNSH_DataAccessException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * TODO 曾
 */
@Repository
public class AppDAO extends SuperDAO
{
    public AppDAO(JdbcTemplate jdbcTemplate)
    {
        super(jdbcTemplate);
    }

    /**
     * 插入一个应用
     *
     * @param app 应用对象
     *
     * @return 操作是否成功
     *
     * @throws ZNSH_DataAccessException
     */
    public boolean insert(App app)
            throws ZNSH_DataAccessException
    {
        if(app != null)
        {
            //SQL
            DB.appMap.put(app.getPk(), app);
            return true;
        }
        return false;
    }

    /**
     * 更新应用信息
     *
     * @param app 应用对象
     *
     * @return 操作是否成功
     *
     * @throws ZNSH_DataAccessException
     */
    public boolean update(App app)
            throws ZNSH_DataAccessException
    {
        if(app != null)
        {
            //SQL
            DB.appMap.put(app.getPk(), app);
            return true;
        }
        return false;
    }

    /**
     * 删除一组应用
     *
     * @param pks 主键数组
     *
     * @return 删除失败的应用的主键
     *
     * @throws ZNSH_DataAccessException
     */
    public Set<String> delete(String[] pks)
            throws ZNSH_DataAccessException
    {
        if(pks != null && pks.length > 0)
        {
            //SQL
            for(String pk : pks)
            {
                DB.appMap.remove(pk);
            }
            return null;
        }
        throw new ZNSH_DataAccessException();
    }

    /**
     * 查询应用列表
     *
     * @param pk     主键（模糊匹配，null表示不限）
     * @param name   名称（模糊匹配，null表示不限）
     * @param master 姓名（模糊匹配，null表示不限）
     * @param pid    身份证号（模糊匹配，null表示不限）
     * @param phone  手机号码（模糊匹配，null表示不限）
     * @param email  邮箱地址（模糊匹配，null表示不限）
     * @param type   类型（null表示不限）
     * @param status 状态（null表示不限）
     * @param from   查询起始位置
     * @param count  查询数量
     *
     * @return
     *
     * @throws ZNSH_DataAccessException
     */
    public List<App> select(String pk, String name, String master, String pid, String phone, String email, AppType type, AppStatus status, int from,
            int count)
            throws ZNSH_DataAccessException
    {
        List<App> list = new ArrayList<>();
        //SQL
        return DB.appMap.values().stream().collect(Collectors.toList());
    }

    /**
     * 检索一个应用
     *
     * @param pk 主键
     *
     * @return 应用对象
     *
     * @throws ZNSH_DataAccessException
     */
    public App get(String pk)
            throws ZNSH_DataAccessException
    {
        if(JText.isNormal(pk))
        {
            //SQL
            return DB.appMap.get(pk);
        }
        return null;
    }

    /**
     * 查找一个应用
     *
     * @param key 以下任意：主键、管理账号、手机号码、邮箱地址
     *
     * @return 应用对象
     *
     * @throws ZNSH_DataAccessException
     */
    public App find(String key)
            throws ZNSH_DataAccessException
    {
        if(JText.isNormal(key))
        {
            //SQL
            for(App app : DB.appMap.values())
            {
                if(app.getPk().equals(key) || app.getAccount().equals(key) || app.getPhone().equals(key) || app.getEmail().equals(key))
                {
                    return app;
                }
            }
        }
        return null;
    }
}