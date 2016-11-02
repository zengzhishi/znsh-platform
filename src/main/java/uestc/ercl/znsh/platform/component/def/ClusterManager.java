/*
 * Copyright (c) 2016. Embedded Real-Time Computation Lab Of UESTC.
 *
 * 电子科技大学・信息与软件工程学院・嵌入式实时计算研究所
 *
 * http://www.is.uestc.edu.cn
 */
package uestc.ercl.znsh.platform.component.def;

import cn.sel.jutil.annotation.note.NonNull;
import cn.sel.jutil.annotation.note.Nullable;
import uestc.ercl.znsh.common.entity.Cluster;
import uestc.ercl.znsh.common.exception.ZNSH_IllegalFieldValueException;
import uestc.ercl.znsh.common.exception.ZNSH_ServiceException;

import java.util.List;
import java.util.Set;

public interface ClusterManager
{
    void create(@NonNull String name, @Nullable String desc, @Nullable String url)
            throws ZNSH_IllegalFieldValueException, ZNSH_ServiceException;

    Cluster retrieve(int pk)
            throws ZNSH_IllegalFieldValueException, ZNSH_ServiceException;

    List<Cluster> retrieve(@Nullable String pk, @Nullable String name, @Nullable String desc, @Nullable String url, int from, int count)
            throws ZNSH_IllegalFieldValueException, ZNSH_ServiceException;

    void update(int pk, @Nullable String name, @Nullable String desc, @Nullable String url)
            throws ZNSH_IllegalFieldValueException, ZNSH_ServiceException;

    Set<String> delete(@NonNull int[] pks)
            throws ZNSH_IllegalFieldValueException, ZNSH_ServiceException;
}