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
import uestc.ercl.znsh.common.entity.Config;
import uestc.ercl.znsh.common.exception.ZNSH_IllegalArgumentException;

import java.util.List;

/**
 * 系统配置服务，实现类应当实现缓存。
 */
public interface SysConfigManager
{
    /**
     * 设置系统通信令牌
     *
     * @param name  参数名
     * @param value 参数值
     *
     * @return 操作是否成功
     *
     * @throws ZNSH_IllegalArgumentException
     */
    boolean set(@NonNull String name, String value)
            throws ZNSH_IllegalArgumentException;

    /**
     * 设置系统通信令牌
     *
     * @param value 参数值
     *
     * @return 操作是否成功
     */
    boolean setToken(String value)
            throws ZNSH_IllegalArgumentException;

    /**
     * 设置系统管理界面数据列表页大小
     *
     * @param value 参数值
     *
     * @return 操作是否成功
     */
    boolean setPageSize(Object value)
            throws ZNSH_IllegalArgumentException;

    /**
     * 设置系统动态验证码有效时长
     *
     * @param value 参数值
     *
     * @return 操作是否成功
     */
    boolean setVCodeExpires(Object value)
            throws ZNSH_IllegalArgumentException;

    /**
     * 查询系统通信令牌
     *
     * @return TOKEN密文
     */
    String getToken();

    /**
     * 查询系统管理界面数据列表页大小
     *
     * @return 页大小（默认为20）
     */
    int getPageSize();

    /**
     * 查询系统动态验证码有效时长
     *
     * @return 秒数
     */
    int getVCodeExpires();

    /**
     * 获取配置参数
     *
     * @param name 参数名
     *
     * @return 配置项
     */
    Config get(@NonNull String name);

    /**
     * 查询所有配置列表（不分页）
     *
     * @return 配置列表
     */
    List<Config> list();
}