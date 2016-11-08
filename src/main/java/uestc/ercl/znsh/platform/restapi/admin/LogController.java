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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import uestc.ercl.znsh.common.exception.ZNSH_DataAccessException;
import uestc.ercl.znsh.common.logging.LogLevel;
import uestc.ercl.znsh.common.logging.LogSource;
import uestc.ercl.znsh.common.logging.LogType;
import uestc.ercl.znsh.common.logging.SysLogManager;
import uestc.ercl.znsh.platform.component.SysLogManagerImpl;
import uestc.ercl.znsh.platform.dao.SysConfigDAO;
import uestc.ercl.znsh.platform.restapi.BaseController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    private SysLogManager sysLogManager;
    private final SysConfigDAO sysConfigDAO;

    @Autowired
    public LogController(SysConfigDAO sysConfigDAO)
    {
        Assert.notNull(sysConfigDAO);
        this.sysConfigDAO = sysConfigDAO;
        sysLogManager = new SysLogManagerImpl(LogSource.SYS, sysConfigDAO.getJdbcTemplate().getDataSource());
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET)
    public List<Map<String, Object>> retrieve(HttpServletRequest request, HttpServletResponse response, String title, String content, String source,
            String type, String level, Long timeStart, Long timeEnd, @RequestParam(defaultValue = "0") Long from,
            @RequestParam(defaultValue = "20") Integer count)
            throws IOException
    {
        LogSource logSource = LogSource.parse(source);
        LogType logType = LogType.parse(type);
        LogLevel logLevel = LogLevel.parse(level);
        Date start = timeStart != null ? new Date(timeStart) : null;
        Date end = timeEnd != null ? new Date(timeEnd) : null;
        try
        {
            return sysLogManager.find(title, content, logSource, logType, logLevel, start, end, from, count);
        } catch(ZNSH_DataAccessException e)
        {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "查询日志列表失败！");
        }
        return null;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.DELETE, produces = "text/plain;charset=UTF-8")
    public String delete(HttpServletRequest request, HttpServletResponse response)
            throws IOException
    {
        return "暂未实现";
    }
}