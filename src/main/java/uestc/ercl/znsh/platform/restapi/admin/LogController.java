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
package uestc.ercl.znsh.platform.restapi.admin;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import uestc.ercl.znsh.common.logging.LogLevel;
import uestc.ercl.znsh.common.logging.LogSource;
import uestc.ercl.znsh.common.logging.LogType;
import uestc.ercl.znsh.common.logging.SysLogManager;
import uestc.ercl.znsh.platform.component.SysLogManagerImpl;
import uestc.ercl.znsh.platform.restapi.BaseController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 应用管理接口
 */
@Controller
@RequestMapping(path = "api/admin/log")
public class LogController extends BaseController
{
    private final SysLogManager sysLogManager;

    public LogController()
    {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("spring/servlet-context.xml");
        DataSource dataSource = (DataSource)ctx.getBean("dataSource");
        sysLogManager = new SysLogManagerImpl(LogSource.SYS, dataSource);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET)
    public List<Map<String, Object>> retrieve(HttpServletRequest request, HttpServletResponse response, String title, String content, String source,
            String type, String level, Long timeStart, Long timeEnd, @RequestParam(defaultValue = "0") Long from,
            @RequestParam(defaultValue = "20") Integer count)
            throws IOException
    {
        LogSource logSource = LogSource.valueOf(source);
        LogType logType = LogType.valueOf(type);
        LogLevel logLevel = LogLevel.valueOf(level);
        Date start = timeStart != null ? new Date(timeStart) : null;
        Date end = timeEnd != null ? new Date(timeEnd) : null;
        return sysLogManager.find(title, content, logSource, logType, logLevel, start, end, from, count);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.DELETE, produces = "text/plain;charset=UTF-8")
    public String delete(HttpServletRequest request, HttpServletResponse response)
            throws IOException
    {
        return "未实现";
    }
}