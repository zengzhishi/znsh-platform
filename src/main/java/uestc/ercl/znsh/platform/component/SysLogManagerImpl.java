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

import cn.sel.jutil.annotation.note.NonNull;
import cn.sel.jutil.annotation.note.Nullable;
import cn.sel.jutil.calendar.DateTime;
import cn.sel.jutil.lang.JText;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import uestc.ercl.znsh.common.data.DataUtil;
import uestc.ercl.znsh.common.exception.ZNSH_DataAccessException;
import uestc.ercl.znsh.common.logging.LogLevel;
import uestc.ercl.znsh.common.logging.LogSource;
import uestc.ercl.znsh.common.logging.LogType;
import uestc.ercl.znsh.common.logging.SysLogManager;

import javax.sql.DataSource;
import java.sql.ResultSetMetaData;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 面向Oracle数据库的日志实现
 * TODO SQL待改，当前是PG的
 */
public class SysLogManagerImpl extends SysLogManager
{
    private static final Logger LOGGER = LoggerFactory.getLogger(SysLogManagerImpl.class);
    private static final RowMapper<Map<String, Object>> LOG_MAPPER = (rs, rowNum)->
    {
        Map<String, Object> result = new HashMap<>();
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();
        if(columnCount > 0)
        {
            for(int i = 1; i <= columnCount; i++)
            {
                String columnName = metaData.getColumnName(i);
                switch(columnName.toUpperCase())
                {
                    case "SOURCE":
                        result.put(columnName, LogSource.valueOf(rs.getInt(i)));
                        break;
                    case "TYPE":
                        result.put(columnName, LogType.valueOf(rs.getInt(i)));
                        break;
                    case "LEVEL":
                        result.put(columnName, LogLevel.valueOf(rs.getInt(i)));
                        break;
                    default:
                        result.put(columnName, rs.getObject(i));
                        break;
                }
            }
        }
        return result;
    };
    JdbcDaoSupport jdbcDAO = new JdbcDaoSupport()
    {
    };

    public SysLogManagerImpl(@NonNull LogSource logSource, @NonNull DataSource dataSource)
    {
        super(logSource);
        if(dataSource == null)
        {
            throw new IllegalArgumentException("数据源不能为null！");
        }
        jdbcDAO.setDataSource(dataSource);
        try
        {
            jdbcDAO.getDataSource().getConnection();
        } catch(Exception e)
        {
            throw new IllegalArgumentException("数据源无效！" + e.getMessage());
        }
    }

    @Override
    @Nullable
    public List<Map<String, Object>> find(@Nullable String title, @Nullable String content, @Nullable LogSource source, @Nullable LogType type,
            @Nullable LogLevel level, @Nullable Date timeStart, @Nullable Date timeEnd, long from, int count)
            throws ZNSH_DataAccessException
    {
        String sql = "SELECT * FROM common.\"" + TABLE_NAME + "\" WHERE 1=1";
        if(JText.isNormal(title))
        {
            sql += String.format(" AND title LIKE '%%%s%%'", title);
        }
        if(JText.isNormal(content))
        {
            sql += String.format(" AND content LIKE '%%%s%%'", content);
        }
        if(source != null)
        {
            sql += String.format(" AND source = %d", source.value());
        }
        if(type != null)
        {
            sql += String.format(" AND type = %d", type.value());
        }
        if(level != null)
        {
            sql += String.format(" AND level = %d", level.value());
        }
        if(timeStart != null)
        {
            sql += String.format(" AND datetime >= '%s'", DateTime.toString(timeStart));
        }
        if(timeEnd != null)
        {
            sql += String.format(" AND datetime <= '%s'", DateTime.toString(timeEnd));
        }
        sql += String.format(" LIMIT %d OFFSET %d", from + count, from);
        try
        {
            return jdbcDAO.getJdbcTemplate().query(sql, LOG_MAPPER);
        } catch(Exception e)
        {
            LOGGER.error("查询系统日志出错，详见以上堆栈跟踪。", e);
            throw new ZNSH_DataAccessException("查询系统日志失败！");
        }
    }

    @Override
    public void delete(@Nullable String title, @Nullable String content, @Nullable LogSource source, @Nullable LogType type, @Nullable LogLevel level,
            @Nullable Date timeStart, @Nullable Date timeEnd)
            throws ZNSH_DataAccessException
    {
        String sql = "DELETE FROM common.\"" + TABLE_NAME + "\" WHERE 1=1";
        if(JText.isNormal(title))
        {
            sql += String.format(" AND title LIKE '%%%s%%'", title);
        }
        if(JText.isNormal(content))
        {
            sql += String.format(" AND content LIKE '%%%s%%'", content);
        }
        if(source != null)
        {
            sql += String.format(" AND source = %d", source.value());
        }
        if(type != null)
        {
            sql += String.format(" AND type = %d", type.value());
        }
        if(level != null)
        {
            sql += String.format(" AND level = %d", level.value());
        }
        if(timeStart != null)
        {
            sql += String.format(" AND datetime >= '%s'", DateTime.toString(timeStart));
        }
        if(timeEnd != null)
        {
            sql += String.format(" AND datetime <= '%s'", DateTime.toString(timeEnd));
        }
        try
        {
            jdbcDAO.getJdbcTemplate().update(sql);
        } catch(Exception e)
        {
            LOGGER.error("删除系统日志出错，详见以上堆栈跟踪。", e);
            throw new ZNSH_DataAccessException("删除系统日志失败！");
        }
    }

    @Override
    public void delete(@NonNull CharSequence... pks)
            throws ZNSH_DataAccessException
    {
        if(pks == null || pks.length < 1)
        {
            LOGGER.error("删除系统日志时，主键序列不能为null或空!");
        } else
        {
            String sql = "DELETE FROM common.\"" + TABLE_NAME + "\" WHERE pk IN (?)";
            try
            {
                int deletedRows = jdbcDAO.getJdbcTemplate().update(sql, DataUtil.joinPKs(pks));
                if(deletedRows != pks.length)
                {
                    throw new ZNSH_DataAccessException("部分日志删除失败！");
                }
            } catch(Exception e)
            {
                LOGGER.error("删除系统日志出错，详见以上堆栈跟踪。", e);
                throw new ZNSH_DataAccessException("删除系统日志失败！");
            }
        }
    }

    @Override
    public void clear()
            throws ZNSH_DataAccessException
    {
        String sql = "DELETE FROM common.\"" + TABLE_NAME + "\" WHERE 1=1";
        try
        {
            jdbcDAO.getJdbcTemplate().update(sql);
        } catch(Exception e)
        {
            LOGGER.error("清空系统日志出错，详见以上堆栈跟踪。", e);
            throw new ZNSH_DataAccessException("清空系统日志失败！");
        }
    }

    @Override
    protected void log(@NonNull String title, @NonNull String content, @Nullable LogSource source, @NonNull LogType type, @NonNull LogLevel level)
    {
        String pk = DataUtil.newPK32();
        System.out.println(String.format("新日志：主键=%s，标题=%s，内容=%s，类型=%s，级别=%s", pk, title, content, type, level));
        if(checkLog(title, content, type, level))
        {
            String sql = "INSERT INTO common.\"" + TABLE_NAME + "\"(pk,title,content,source,type,level) VALUES(?,?,?,?,?,?)";
            try
            {
                int affectedRowCount = jdbcDAO.getJdbcTemplate().update(sql, pk, title, content, source.value(), type.value(), level.value());
                if(affectedRowCount != 1)
                {
                    throw new ZNSH_DataAccessException();
                }
            } catch(Exception e)
            {
                LOGGER.error(String.format("保存系统日志失败！详见以上堆栈跟踪。日志信息：标题=%s|内容=%s|类型=%s。", title, content, LogType.SYS_RUNTIME), e);
            }
        }
    }

    private boolean checkLog(String title, String content, LogType type, LogLevel level)
    {
        boolean ok = true;
        if(JText.isNullOrEmpty(title))
        {
            ok = false;
            LOGGER.error("保存系统日志出错，日志标题不能为null或空!");
        }
        if(JText.isNullOrEmpty(content))
        {
            ok = false;
            LOGGER.error("保存系统日志出错，日志内容不能为null或空!");
        }
        if(type == null)
        {
            ok = false;
            LOGGER.error("保存系统日志出错，日志类型不能为null!");
        }
        if(level == null)
        {
            ok = false;
            LOGGER.error("保存系统日志出错，日志级别不能为null!");
        }
        return ok;
    }
}