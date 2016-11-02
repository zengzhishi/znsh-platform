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
import uestc.ercl.znsh.platform.entity.VCode;

@Repository
public class VerifyCodeDAO extends SuperDAO
{
    public VerifyCodeDAO(JdbcTemplate jdbcTemplate)
    {
        super(jdbcTemplate);
    }

    public boolean insert(String key, String code)
    {
        return false;
    }

    public VCode select(String key)
    {
        return null;
    }
}