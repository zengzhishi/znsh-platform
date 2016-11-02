/*
 * Copyright (c) 2016. Embedded Real-Time Computation Lab Of UESTC.
 *
 * 电子科技大学・信息与软件工程学院・嵌入式实时计算研究所
 *
 * http://www.is.uestc.edu.cn
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