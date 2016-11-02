/*
 * Copyright (c) 2016. Embedded Real-Time Computation Lab Of UESTC.
 *
 * 电子科技大学・信息与软件工程学院・嵌入式实时计算研究所
 *
 * http://www.is.uestc.edu.cn
 */
package uestc.ercl.znsh.platform.component;

import cn.sel.jutil.annotation.note.NonNull;
import cn.sel.jutil.lang.JText;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import uestc.ercl.znsh.common.entity.Config;
import uestc.ercl.znsh.platform.component.def.SysConfigManager;
import uestc.ercl.znsh.platform.constants.SysConfigName;
import uestc.ercl.znsh.platform.dao.SysConfigDAO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by sel on 16-10-26.
 */
@Component
public class SysConfigManagerImpl implements SysConfigManager
{
    private final SysConfigDAO sysConfigDAO;
    private final Map<String, Config> cache = new HashMap<>();

    @Autowired
    public SysConfigManagerImpl(SysConfigDAO sysConfigDAO)
    {
        Assert.notNull(sysConfigDAO, "SysConfigDAO注入失败！不能为空！");
        this.sysConfigDAO = sysConfigDAO;
    }

    @Override
    public boolean set(@NonNull Config config)
    {
        if(config != null)
        {
            cache.put(config.getName(), config);
        }
        return sysConfigDAO.insert(config);
    }

    @Override
    public Config get(@NonNull String configName)
    {
        if(JText.isNormal(configName))
        {
            if(cache.keySet().contains(configName))
            {
                return cache.get(configName);
            }
            return sysConfigDAO.select(configName);
        }
        return null;
    }

    @Override
    public List<Config> getList()
    {
        return cache.values().stream().collect(Collectors.toList());
    }

    @Override
    public String getToken()
    {
        Config config = get(SysConfigName.TOKEN);
        return config != null ? config.getValue() : null;
    }

    @Override
    public Integer getPageSize()
    {
        Config config = get(SysConfigName.PAGE_SIZE);
        if(config != null)
        {
            try
            {
                return Integer.valueOf(config.getValue());
            } catch(NumberFormatException e)
            {
                e.printStackTrace();
            }
        }
        return 20;
    }

    @Override
    public Integer getVCodeExpires()
    {
        Config config = get(SysConfigName.VCODE_EXPIRES);
        if(config != null)
        {
            try
            {
                return Integer.valueOf(config.getValue());
            } catch(NumberFormatException e)
            {
                e.printStackTrace();
            }
        }
        return 60 * 3;
    }
}