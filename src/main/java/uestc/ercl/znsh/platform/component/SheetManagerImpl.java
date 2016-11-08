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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import uestc.ercl.znsh.common.entity.Sheet;
import uestc.ercl.znsh.common.exception.ZNSH_DataAccessException;
import uestc.ercl.znsh.common.exception.ZNSH_IllegalArgumentException;
import uestc.ercl.znsh.common.exception.ZNSH_ServiceException;
import uestc.ercl.znsh.common.logging.LogSource;
import uestc.ercl.znsh.platform.component.def.SheetManager;
import uestc.ercl.znsh.platform.dao.SheetDAO;

import java.util.List;
import java.util.Set;

@Component
public class SheetManagerImpl extends _SysLoggerHolder implements SheetManager
{
    private static final Logger LOGGER = LoggerFactory.getLogger(SheetManagerImpl.class);
    private final SheetDAO sheetDAO;

    public SheetManagerImpl(SheetDAO sheetDAO)
    {
        Assert.notNull(sheetDAO, "SheetDAO 注入失败！不能为空！");
        this.sheetDAO = sheetDAO;
        sysLogger = new SysLogManagerImpl(LogSource.SYS, sheetDAO.getJdbcTemplate().getDataSource());
    }

    @Override
    public void create(@NonNull String appId, @NonNull String id, @NonNull String name, @Nullable String desc)
            throws ZNSH_IllegalArgumentException, ZNSH_ServiceException
    {
        try
        {
            Sheet sheet = new Sheet(appId, id, name, desc);
            sheetDAO.insert(sheet);
        } catch(ZNSH_DataAccessException e)
        {
            LOGGER.error("创建报表失败！");
            throw new ZNSH_ServiceException(e);
        }
    }

    @Override
    public List<Sheet> retrieve(@Nullable String id, @Nullable String name, @Nullable String desc, long from, int count)
            throws ZNSH_IllegalArgumentException, ZNSH_ServiceException
    {
        if(from < 0)
        {
            from = 0;
        }
        if(count < 1)
        {
            count = 20;
        }
        try
        {
            return sheetDAO.select(id, name, desc, from, count);
        } catch(ZNSH_DataAccessException e)
        {
            LOGGER.error("查找报表失败！数据访问未完成！");
            throw new ZNSH_ServiceException(e);
        }
    }

    @Override
    public void update(long pk, @Nullable String name, @Nullable String desc)
            throws ZNSH_IllegalArgumentException, ZNSH_ServiceException
    {
        try
        {
            Sheet sheet = new Sheet();
            sheet.update(pk, name, desc);
            sheetDAO.update(sheet);
        } catch(ZNSH_DataAccessException e)
        {
            LOGGER.error("更新报表失败！");
            throw new ZNSH_ServiceException(e);
        }
    }

    @Override
    public Set<String> delete(long... pks)
            throws ZNSH_IllegalArgumentException, ZNSH_ServiceException
    {
        if(pks == null || pks.length <= 0)
        {
            throw new ZNSH_IllegalArgumentException("未选择报表！");
        } else
        {
            try
            {
                Set<String> fails = sheetDAO.delete(pks);
                return fails != null && !fails.isEmpty() ? fails : null;
            } catch(ZNSH_DataAccessException e)
            {
                LOGGER.error("删除报表失败！");
                throw new ZNSH_ServiceException(e);
            }
        }
    }
}