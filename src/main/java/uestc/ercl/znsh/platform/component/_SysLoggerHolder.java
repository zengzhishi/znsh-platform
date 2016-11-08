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
package uestc.ercl.znsh.platform.component;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import uestc.ercl.znsh.common.logging.LogSource;
import uestc.ercl.znsh.common.logging.SysLogManager;

import javax.sql.DataSource;

public abstract class _SysLoggerHolder
{
    protected SysLogManager sysLogger;

    public _SysLoggerHolder()
    {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("spring/servlet-context.xml");
        DataSource dataSource = (DataSource)ctx.getBean("dataSource");
        sysLogger = new SysLogManagerImpl(LogSource.SYS, dataSource);
    }
}