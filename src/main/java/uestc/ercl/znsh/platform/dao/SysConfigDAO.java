/*
 * Copyright (c) 2016. Embedded Real-Time Computation Lab Of UESTC.
 *
 * 电子科技大学・信息与软件工程学院・嵌入式实时计算研究所
 *
 * http://www.is.uestc.edu.cn
 */
package uestc.ercl.znsh.platform.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import uestc.ercl.znsh.common.entity.Config;

import java.util.List;

/**
 * Created by sel on 16-10-26.
 */
@Repository
public class SysConfigDAO extends SuperDAO
{
    public SysConfigDAO(JdbcTemplate jdbcTemplate)
    {
        super(jdbcTemplate);
    }

    public boolean insert(Config config)
    {
        return false;
    }

    public Config select(String name)
    {
        return null;
    }

    public List<Config> select()
    {
        return null;
    }
}