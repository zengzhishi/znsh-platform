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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import sun.java2d.pipe.AlphaPaintPipe;
import uestc.ercl.znsh.common.constant.AppStatus;
import uestc.ercl.znsh.common.constant.AppType;
import uestc.ercl.znsh.common.entity.App;
import uestc.ercl.znsh.common.exception.ZNSH_DataAccessException;
import uestc.ercl.znsh.common.exception.ZNSH_IllegalArgumentException;

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
            //TODO 完善SQL，并去掉以下模拟功能代码 need test
            DB.appMap.put(app.getPk(), app);
            String sql = "INSERT INTO " + App.TABLE_NAME + "(pk, name, descr, type, status, " +
                    "master, pid, phone, email, account, password, cluster_pk, create_time) " +
                    "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";

            int result = jdbcTemplate.update(sql, app.getPk(), app.getName(), app.getDesc(), app.getType(),
                    app.getStatus(), app.getMaster(), app.getPid(), app.getPhone(), app.getEmail(),
                    app.getAccount(), app.getPassword(), app.getClusterPk(), app.getCreateDateTime());
            if (result != 1) return false;
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
            //TODO 完善SQL，并去掉以下模拟功能代码 need test
            DB.appMap.put(app.getPk(), app);
            StringBuilder sqlBuilder = new StringBuilder("UPDATE " + App.TABLE_NAME + " set ");
            sqlBuilder.append("name=?,descr=?,type=?,status=?,master=?,pid=?,phone=?," +
                    "email=?,account=?,password=?,cluster_pk=?,create_time=? ");
            sqlBuilder.append("WHERE pk=?;");

            int rows = jdbcTemplate.update(sqlBuilder.toString(), app.getName(), app.getDesc(), app.getType(),
                    app.getStatus(), app.getMaster(), app.getPid(), app.getPhone(), app.getEmail(),
                    app.getAccount(), app.getPassword(), app.getClusterPk(), app.getCreateDateTime());
            if (rows != 1) return false;
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
            //TODO 完善SQL，并去掉以下模拟功能代码 need test
            for(String pk : pks)
            {
                String sql = "DELETE FROM " + App.TABLE_NAME + " WHERE pk=?;";
                jdbcTemplate.update(sql, pk);
//                DB.appMap.remove(pk);
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
    public List<App> select(String pk, String name, String master, String pid, String phone, String email, AppType type, AppStatus status, long from,
            int count)
            throws ZNSH_DataAccessException
    {
        List<App> list = new ArrayList<>();
        //TODO 完善SQL，并去掉以下模拟功能代码 need test
        StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM (");
        sqlBuilder.append("SELECT A.*, ROWNUM RN FROM (");
        sqlBuilder.append("SELECT * FROM " + App.TABLE_NAME + " WHERE ");
        sqlBuilder.append("pk LIKE ? and ");
        sqlBuilder.append("name LIKE ? and ");
        sqlBuilder.append("master LIKE ? and ");
        sqlBuilder.append("pid LIKE ? and ");
        sqlBuilder.append("phone LIKE ? and ");
        sqlBuilder.append("email LIKE ? and ");
        sqlBuilder.append("type=? and ");
        sqlBuilder.append("status=?");
        //需要做分页
        sqlBuilder.append(") A WHERE ROWNUM <=?) WHERE RN>=?;");

        return jdbcTemplate.queryForList(sqlBuilder.toString(),
                new Object[]{
                    "%" + pk + "%", "%" + name + "%", "%" + master + "%", "%" + pid + "%",
                        "%" + phone + "%", "%" + email + "%", type, status, from+count, from},
                App.class);

//        return DB.appMap.values().stream().collect(Collectors.toList());
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
            //TODO 完善SQL，并去掉以下模拟功能代码 need test
            String sql = "SELECT * FROM " + App.TABLE_NAME + " WHERE pk=?";
            return (App)jdbcTemplate.queryForObject(sql, new Object[]{pk}, App.class);
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
            //TODO 完善SQL，并去掉以下模拟功能代码 need test?
            String sql = "SELECT * FROM " + App.TABLE_NAME;
            for(App app : jdbcTemplate.queryForList(sql, App.class))
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