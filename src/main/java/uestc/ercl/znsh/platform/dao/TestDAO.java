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
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * TODO 曾
 */
@Repository
public class TestDAO extends SuperDAO
{
    public static final RowMapper<String[]> rowMapper = (rs, rowNum)->
    {
        String[] result = new String[3];
        result[0] = rs.getString("USERID");
        result[1] = rs.getString("USERNAME");
        result[2] = rs.getString("PASSWORD");
        return result;
    };

    public TestDAO(JdbcTemplate jdbcTemplate)
    {
        super(jdbcTemplate);
    }

    public List<String[]> test()
    {
        return jdbcTemplate.query("SELECT USERID,USERNAME,PASSWORD FROM USERINFO", rowMapper);
    }
}