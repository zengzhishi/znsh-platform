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
package uestc.ercl.znsh.platform_test;

import com.zaxxer.hikari.HikariDataSource;
import oracle.jdbc.pool.OracleDataSource;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import uestc.ercl.znsh.platform.dao.TestDAO;

import java.io.FileInputStream;
import java.util.Properties;

/**
 * Created by sel on 16-9-6.
 */
public class TestDB
{
    @Test
    public void test()
            throws Exception
    {
        Properties properties = new Properties();
        String userHome = System.getProperty("user.home");
        properties.load(new FileInputStream(userHome + "/.znsh/znsh.conf"));
        OracleDataSource oracleDataSource = new OracleDataSource();
        oracleDataSource.setDriverType(properties.getProperty("ORACLE_DRIVERTYPE").trim());
        oracleDataSource.setServerName(properties.getProperty("ORACLE_SERVERNAME").trim());
        oracleDataSource.setPortNumber(Integer.parseInt(properties.getProperty("ORACLE_PORTNUMBER").trim()));
        oracleDataSource.setDatabaseName(properties.getProperty("ORACLE_DATABASENAME").trim());
        oracleDataSource.setUser(properties.getProperty("ORACLE_USER").trim());
        oracleDataSource.setPassword(properties.getProperty("ORACLE_PASSWORD").trim());
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDataSource(oracleDataSource);
        JdbcTemplate jdbcTemplate = new JdbcTemplate(oracleDataSource);
        jdbcTemplate.getDataSource().getConnection();
        new TestDAO(jdbcTemplate).test().forEach(array->
        {
            for(String s : array)
            {
                System.out.print(s);
                System.out.print('\t');
            }
            System.out.println();
        });
    }
}