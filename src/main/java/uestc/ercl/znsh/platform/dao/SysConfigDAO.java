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

    public boolean insertOrUpdate(Config config)
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