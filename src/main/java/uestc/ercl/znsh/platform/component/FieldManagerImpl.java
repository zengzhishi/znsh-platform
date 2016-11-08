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
import org.springframework.util.Assert;
import uestc.ercl.znsh.common.entity.Field;
import uestc.ercl.znsh.common.exception.ZNSH_DataAccessException;
import uestc.ercl.znsh.common.exception.ZNSH_IllegalArgumentException;
import uestc.ercl.znsh.common.exception.ZNSH_ServiceException;
import uestc.ercl.znsh.platform.component.def.FieldManager;
import uestc.ercl.znsh.platform.dao.FieldDAO;

import java.util.List;
import java.util.Set;

public class FieldManagerImpl extends _SysLoggerHolder implements FieldManager
{
    private static final Logger LOGGER = LoggerFactory.getLogger(SheetManagerImpl.class);
    private final FieldDAO fieldDAO;

    public FieldManagerImpl(FieldDAO fieldDAO)
    {
        Assert.notNull(fieldDAO, "FieldDAO 注入失败！不能为空！");
        this.fieldDAO = fieldDAO;
    }

    @Override
    public void create(long sheetPk, @NonNull String id, @Nullable String name, @Nullable String desc, @NonNull String type, boolean nullable,
            String dict)
            throws ZNSH_IllegalArgumentException, ZNSH_ServiceException
    {
        try
        {
            Field sheet = new Field();
            sheet.setPk(sheetPk);
            sheet.setId(id);
            sheet.setName(name);
            sheet.setDesc(desc);
            sheet.setType(type);
            sheet.setNullable(nullable);
            sheet.setDict(dict);
            fieldDAO.insert(sheet);
        } catch(ZNSH_DataAccessException e)
        {
            LOGGER.error("创建字段失败！");
            throw new ZNSH_ServiceException(e);
        }
    }

    @Override
    public List<Field> retrieve(@NonNull long sheetPk)
            throws ZNSH_IllegalArgumentException, ZNSH_ServiceException
    {
        try
        {
            return fieldDAO.select(sheetPk);
        } catch(ZNSH_DataAccessException e)
        {
            LOGGER.error("查找字段失败！数据访问未完成！");
            throw new ZNSH_ServiceException(e);
        }
    }

    @Override
    public void update(long pk, @Nullable String name, @Nullable String desc, @Nullable String type, boolean nullable, @Nullable String dict)
            throws ZNSH_IllegalArgumentException, ZNSH_ServiceException
    {
        try
        {
            Field field = new Field();
            update(pk, name, desc, type, nullable, dict);
            fieldDAO.update(field);
        } catch(ZNSH_DataAccessException e)
        {
            LOGGER.error("更新字段失败！");
            throw new ZNSH_ServiceException(e);
        }
    }

    @Override
    public Set<String> delete(long... pks)
            throws ZNSH_IllegalArgumentException, ZNSH_ServiceException
    {
        if(pks == null || pks.length <= 0)
        {
            throw new ZNSH_IllegalArgumentException("未选择字段！");
        } else
        {
            try
            {
                Set<String> fails = fieldDAO.delete(pks);
                return fails != null && !fails.isEmpty() ? fails : null;
            } catch(ZNSH_DataAccessException e)
            {
                LOGGER.error("删除字段失败！");
                throw new ZNSH_ServiceException(e);
            }
        }
    }
}