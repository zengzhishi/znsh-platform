/*
 * Copyright (c) 2016. Embedded Real-Time Computation Lab Of UESTC.
 *
 * 电子科技大学・信息与软件工程学院・嵌入式实时计算研究所
 *
 * http://www.is.uestc.edu.cn
 */
package uestc.ercl.znsh.platform_test;

import com.zaxxer.hikari.HikariDataSource;
import oracle.jdbc.pool.OracleDataSource;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import uestc.ercl.znsh.platform.dao.TestDAO;

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
        properties.load(getClass().getClassLoader().getResourceAsStream("znsh.conf"));
        OracleDataSource oracleDataSource = new OracleDataSource();
        oracleDataSource.setDriverType(properties.getProperty("oracle.driverType").trim());
        oracleDataSource.setServerName(properties.getProperty("oracle.serverName").trim());
        oracleDataSource.setPortNumber(Integer.parseInt(properties.getProperty("oracle.portNumber").trim()));
        oracleDataSource.setDatabaseName(properties.getProperty("oracle.databaseName").trim());
        oracleDataSource.setUser(properties.getProperty("oracle.user").trim());
        oracleDataSource.setPassword(properties.getProperty("oracle.password").trim());
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