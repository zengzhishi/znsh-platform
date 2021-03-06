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
package uestc.ercl.znsh.platform.component.def;

import cn.sel.jutil.annotation.note.NonNull;
import cn.sel.jutil.annotation.note.Nullable;
import uestc.ercl.znsh.common.entity.Sheet;
import uestc.ercl.znsh.common.exception.ZNSH_IllegalArgumentException;
import uestc.ercl.znsh.common.exception.ZNSH_ServiceException;

import java.util.List;
import java.util.Set;

public interface SheetManager
{
    void create(@NonNull String appId, @NonNull String id, @NonNull String name, @Nullable String desc)
            throws ZNSH_IllegalArgumentException, ZNSH_ServiceException;

    List<Sheet> retrieve(@Nullable String id, @Nullable String name, @Nullable String desc, long from, int count)
            throws ZNSH_IllegalArgumentException, ZNSH_ServiceException;

    void update(long pk, @Nullable String name, @Nullable String desc)
            throws ZNSH_IllegalArgumentException, ZNSH_ServiceException;

    Set<String> delete(long... pks)
            throws ZNSH_IllegalArgumentException, ZNSH_ServiceException;
}