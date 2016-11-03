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
package uestc.ercl.znsh.platform.util;

import uestc.ercl.znsh.common.constant.Regular;
import uestc.ercl.znsh.common.exception.ZNSH_IllegalArgumentException;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * 参数检查工具
 */
public class ArgValidator
{
    public static void checkArgNonnull(String arg, String title)
            throws ZNSH_IllegalArgumentException
    {
        if(arg == null)
        {
            throw new ZNSH_IllegalArgumentException(String.format("参数'%s'不能为null！", title));
        }
    }

    public static void checkArgReg(String arg, String title, String pattern)
            throws ZNSH_IllegalArgumentException
    {
        checkArgNonnull(arg, title);
        if(!Regular.match(arg, pattern))
        {
            throw new ZNSH_IllegalArgumentException(String.format("参数'%s'格式不合法！", title));
        }
    }

    public static void checkArgRegIfExist(String arg, String title, String pattern)
            throws ZNSH_IllegalArgumentException
    {
        if(arg != null)
        {
            checkArgReg(arg, title, pattern);
        }
    }

    public static void checkArgReg(String arg, String title, Pattern pattern)
            throws ZNSH_IllegalArgumentException
    {
        checkArgNonnull(arg, title);
        if(!Regular.match(arg, pattern))
        {
            throw new ZNSH_IllegalArgumentException(String.format("参数'%s'格式不合法！", title));
        }
    }

    public static void checkArgRegIfExist(String arg, String title, Pattern pattern)
            throws ZNSH_IllegalArgumentException
    {
        if(arg != null)
        {
            checkArgReg(arg, title, pattern);
        }
    }

    public static void checkArgLengthRange(String arg, String title, int min, int max)
            throws ZNSH_IllegalArgumentException
    {
        checkArgNonnull(arg, title);
        int length = arg.length();
        if(length < min || length > max)
        {
            throw new ZNSH_IllegalArgumentException(String.format("参数'%s'长度不正确！应在%d,%d之间，实为%d。", title, min, max, length));
        }
    }

    public static void checkArgLengthRangeIfExist(String arg, String title, int min, int max)
            throws ZNSH_IllegalArgumentException
    {
        if(arg != null)
        {
            checkArgLengthRange(arg, title, min, max);
        }
    }

    public static void checkArgLength(String arg, String title, int length)
            throws ZNSH_IllegalArgumentException
    {
        checkArgNonnull(arg, title);
        int len = arg.length();
        if(len != length)
        {
            throw new ZNSH_IllegalArgumentException(String.format("参数'%s'长度不正确！应为%d，实为%d。", title, length, len));
        }
    }

    public static void checkArgLengthIfExist(String arg, String title, int length)
            throws ZNSH_IllegalArgumentException
    {
        if(arg != null)
        {
            checkArgLength(arg, title, length);
        }
    }

    public static void checkArgNonEmpty(String arg, String title)
            throws ZNSH_IllegalArgumentException
    {
        checkArgNonnull(arg, title);
        if(arg.isEmpty())
        {
            throw new ZNSH_IllegalArgumentException(String.format("参数'%s'不能为空！", title));
        }
    }

    public static void checkArgNonEmptyIfExist(String arg, String title)
            throws ZNSH_IllegalArgumentException
    {
        if(arg != null)
        {
            checkArgNonEmpty(arg, title);
        }
    }

    public static void checkArgInclude(String arg, String title, String[] includes)
            throws ZNSH_IllegalArgumentException
    {
        checkArgNonnull(arg, title);
        Set<String> set = new HashSet<>();
        for(String ex : includes)
        {
            if(!arg.contains(ex))
            {
                set.add(ex);
            }
        }
        if(!set.isEmpty())
        {
            throw new ZNSH_IllegalArgumentException(String.format("参数'%s'必须包含参数'%s'！", title, set));
        }
    }

    public static void checkArgExclude(String arg, String title, String[] excludes)
            throws ZNSH_IllegalArgumentException
    {
        checkArgNonnull(arg, title);
        Set<String> set = new HashSet<>();
        for(String ex : excludes)
        {
            if(arg.contains(ex))
            {
                set.add(ex);
            }
        }
        if(!set.isEmpty())
        {
            throw new ZNSH_IllegalArgumentException(String.format("参数'%s'不能包含参数'%s'！", title, set));
        }
    }
}