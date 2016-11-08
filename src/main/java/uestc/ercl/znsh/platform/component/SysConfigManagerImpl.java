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
import cn.sel.jutil.lang.JText;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import uestc.ercl.znsh.common.entity.Config;
import uestc.ercl.znsh.common.exception.ZNSH_IllegalArgumentException;
import uestc.ercl.znsh.platform.component.def.SysConfigManager;
import uestc.ercl.znsh.platform.constant.SysConfigName;
import uestc.ercl.znsh.platform.dao.SysConfigDAO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class SysConfigManagerImpl extends _SysLoggerHolder implements SysConfigManager
{
    private final SysConfigDAO sysConfigDAO;
    private final Map<String, Config> cache = new HashMap<>();

    @Autowired
    public SysConfigManagerImpl(SysConfigDAO sysConfigDAO)
    {
        Assert.notNull(sysConfigDAO, "SysConfigDAO 注入失败！不能为空！");
        this.sysConfigDAO = sysConfigDAO;
    }

    @Override
    public boolean set(@NonNull String name, String value)
            throws ZNSH_IllegalArgumentException
    {
        if(JText.isNormal(name))
        {
            switch(name)
            {
                case SysConfigName.TOKEN:
                    return setToken(value);
                case SysConfigName.PAGE_SIZE:
                    return setPageSize(value);
                case SysConfigName.VCODE_EXPIRES:
                    return setVCodeExpires(value);
            }
        }
        throw new ZNSH_IllegalArgumentException();
    }

    @Override
    public boolean setToken(String value)
            throws ZNSH_IllegalArgumentException
    {
        if(JText.isNormal(value))
        {
            Config config = new Config(SysConfigName.TOKEN, value);
            cache.put(config.getName(), config);
            return sysConfigDAO.insertOrUpdate(config);
        }
        throw new ZNSH_IllegalArgumentException("");
    }

    @Override
    public boolean setPageSize(Object value)
            throws ZNSH_IllegalArgumentException
    {
        int checkedValue;
        if(value != null)
        {
            try
            {
                checkedValue = Integer.parseInt(String.valueOf(value));
            } catch(NumberFormatException e)
            {
                throw new ZNSH_IllegalArgumentException("", e);
            }
        } else
        {
            checkedValue = 20;
        }
        Config config = new Config(SysConfigName.PAGE_SIZE, String.valueOf(checkedValue));
        cache.put(config.getName(), config);
        return sysConfigDAO.insertOrUpdate(config);
    }

    @Override
    public boolean setVCodeExpires(Object value)
            throws ZNSH_IllegalArgumentException
    {
        int checkedValue;
        if(value != null)
        {
            try
            {
                checkedValue = Integer.parseInt(String.valueOf(value));
            } catch(NumberFormatException e)
            {
                throw new ZNSH_IllegalArgumentException("", e);
            }
        } else
        {
            checkedValue = 60 * 3;
        }
        Config config = new Config(SysConfigName.VCODE_EXPIRES, String.valueOf(checkedValue));
        cache.put(config.getName(), config);
        return sysConfigDAO.insertOrUpdate(config);
    }

    @Override
    public String getToken()
    {
        Config config = get(SysConfigName.TOKEN);
        return config != null ? config.getValue() : null;
    }

    @Override
    public int getPageSize()
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
    public int getVCodeExpires()
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

    @Override
    public Config get(@NonNull String name)
    {
        if(JText.isNormal(name))
        {
            if(cache.containsKey(name))
            {
                return cache.get(name);
            }
            return sysConfigDAO.select(name);
        }
        return null;
    }

    @Override
    public List<Config> list()
    {
        return cache.values().stream().collect(Collectors.toList());
    }
}