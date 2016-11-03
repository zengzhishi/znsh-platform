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
import org.springframework.stereotype.Component;
import uestc.ercl.znsh.common.entity.Sheet;
import uestc.ercl.znsh.platform.component.def.BakManager;

import java.util.List;
import java.util.Map;

/**
 * Created by sel on 16-10-28.
 */
@Component
public class BakManagerImpl implements BakManager
{
    @Override
    public Map<String, List<Sheet>> getAllAppSheets()
    {
        return null;
    }

    @Override
    public List<Sheet> getAppSheets(@NonNull String appId)
    {
        return null;
    }

    @Override
    public Map<String, Long> getLastTask()
    {
        return null;
    }

    @Override
    public boolean notifyDataBak()
    {
        return false;
    }

    @Override
    public boolean saveData()
    {
        return false;
    }
}