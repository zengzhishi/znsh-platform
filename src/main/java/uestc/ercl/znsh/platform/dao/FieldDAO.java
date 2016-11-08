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
import org.springframework.stereotype.Repository;
import uestc.ercl.znsh.common.entity.Field;
import uestc.ercl.znsh.common.exception.ZNSH_DataAccessException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * TODO 曾
 */
@Repository
public class FieldDAO extends SuperDAO
{
    public FieldDAO(JdbcTemplate jdbcTemplate)
    {
        super(jdbcTemplate);
    }

    /**
     * 插入一个字段
     *
     * @param sheet 字段对象
     *
     * @return 操作是否成功
     *
     * @throws ZNSH_DataAccessException
     */
    public boolean insert(Field sheet)
            throws ZNSH_DataAccessException
    {
        if(sheet != null)
        {
            //TODO 完善SQL，并去掉以下模拟功能代码
            sheet.setPk(DB.fieldMap.size() + 1);
            DB.fieldMap.put(sheet.getPk(), sheet);
            return true;
        }
        return false;
    }

    /**
     * 更新字段信息
     *
     * @param sheet 字段对象
     *
     * @return 操作是否成功
     *
     * @throws ZNSH_DataAccessException
     */
    public boolean update(Field sheet)
            throws ZNSH_DataAccessException
    {
        if(sheet != null)
        {
            //TODO 完善SQL，并去掉以下模拟功能代码
            DB.fieldMap.put(sheet.getPk(), sheet);
            return true;
        }
        return false;
    }

    /**
     * 删除一组字段
     *
     * @param pks 主键数组
     *
     * @return 删除失败的字段的主键
     *
     * @throws ZNSH_DataAccessException
     */
    public Set<String> delete(long[] pks)
            throws ZNSH_DataAccessException
    {
        if(pks != null && pks.length > 0)
        {
            //TODO 完善SQL，并去掉以下模拟功能代码
            for(long pk : pks)
            {
                DB.fieldMap.remove(pk);
            }
            return null;
        }
        throw new ZNSH_DataAccessException();
    }

    /**
     * 查询字段列表
     *
     * @param sheetPk 所属报表主键
     *
     * @return
     *
     * @throws ZNSH_DataAccessException
     */
    public List<Field> select(long sheetPk)
            throws ZNSH_DataAccessException
    {
        //TODO 完善SQL，并去掉以下模拟功能代码
        return DB.fieldMap.values().stream().collect(Collectors.toList());
    }
}