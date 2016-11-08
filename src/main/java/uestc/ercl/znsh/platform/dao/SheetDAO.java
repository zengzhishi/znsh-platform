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
import uestc.ercl.znsh.common.entity.Sheet;
import uestc.ercl.znsh.common.exception.ZNSH_DataAccessException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * TODO 曾
 */
@Repository
public class SheetDAO extends SuperDAO
{
    public SheetDAO(JdbcTemplate jdbcTemplate)
    {
        super(jdbcTemplate);
    }

    /**
     * 插入一个报表
     *
     * @param sheet 报表对象
     *
     * @return 操作是否成功
     *
     * @throws ZNSH_DataAccessException
     */
    public boolean insert(Sheet sheet)
            throws ZNSH_DataAccessException
    {
        if(sheet != null)
        {
            //TODO 完善SQL，并去掉以下模拟功能代码
            sheet.setPk(DB.sheetMap.size() + 1);
            DB.sheetMap.put(sheet.getPk(), sheet);
            return true;
        }
        return false;
    }

    /**
     * 更新报表信息
     *
     * @param sheet 报表对象
     *
     * @return 操作是否成功
     *
     * @throws ZNSH_DataAccessException
     */
    public boolean update(Sheet sheet)
            throws ZNSH_DataAccessException
    {
        if(sheet != null)
        {
            //TODO 完善SQL，并去掉以下模拟功能代码
            DB.sheetMap.put(sheet.getPk(), sheet);
            return true;
        }
        return false;
    }

    /**
     * 删除一组报表
     *
     * @param pks 主键数组
     *
     * @return 删除失败的报表的主键
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
                DB.sheetMap.remove(pk);
            }
            return null;
        }
        throw new ZNSH_DataAccessException();
    }

    /**
     * 查询报表列表
     *
     * @param name  名称（模糊匹配，null表示不限）
     * @param desc  描述（模糊匹配，null表示不限）
     * @param url   地址（模糊匹配，null表示不限）
     * @param from  查询起始位置
     * @param count 查询数量
     *
     * @return
     *
     * @throws ZNSH_DataAccessException
     */
    public List<Sheet> select(String name, String desc, String url, long from, int count)
            throws ZNSH_DataAccessException
    {
        //TODO 完善SQL，并去掉以下模拟功能代码
        return DB.sheetMap.values().stream().collect(Collectors.toList());
    }
}