/*
 * Copyright (c) 2016. Embedded Real-Time Computation Lab Of UESTC.
 *
 * 电子科技大学・信息与软件工程学院・嵌入式实时计算研究所
 *
 * http://www.is.uestc.edu.cn
 */
package uestc.ercl.znsh.platform.component.def;

import cn.sel.jutil.annotation.note.NonNull;
import uestc.ercl.znsh.common.entity.Config;

import java.util.List;

/**
 * 系统配置服务，实现类应当实现缓存。
 */
public interface SysConfigManager
{
    /**
     * 设置配置参数
     *
     * @param config 配置项
     *
     * @return
     */
    boolean set(@NonNull Config config);

    /**
     * 获取配置参数
     *
     * @param configName 名称
     *
     * @return 配置项
     */
    Config get(@NonNull String configName);

    /**
     * 查询所有配置列表（不分页）
     *
     * @return 配置列表
     */
    List<Config> getList();

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
    Integer getPageSize();

    /**
     * 查询系统动态验证码有效时长
     *
     * @return 秒数
     */
    Integer getVCodeExpires();
}