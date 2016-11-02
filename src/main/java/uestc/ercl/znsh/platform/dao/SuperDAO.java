/*
 * Copyright (c) 2016. Embedded Real-Time Computation Lab Of UESTC.
 *
 * 电子科技大学・信息与软件工程学院・嵌入式实时计算研究所
 *
 * http://www.is.uestc.edu.cn
 */
package uestc.ercl.znsh.platform.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

/**
 * Created by sel on 16-11-2.
 */
@Repository
public abstract class SuperDAO
{
    protected JdbcTemplate jdbcTemplate;

    @Autowired
    public SuperDAO(JdbcTemplate jdbcTemplate)
    {
        Assert.notNull(jdbcTemplate, "JdbcTemplate注入失败！不能为空！");
        this.jdbcTemplate = jdbcTemplate;
    }

    public JdbcTemplate getJdbcTemplate()
    {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate)
    {
        Assert.notNull(jdbcTemplate, "JdbcTemplate设置失败！不能为空！");
        this.jdbcTemplate = jdbcTemplate;
    }
}