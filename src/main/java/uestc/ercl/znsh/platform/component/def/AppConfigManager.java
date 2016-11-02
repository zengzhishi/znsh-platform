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

public interface AppConfigManager
{
    /**
     * 设置配置参数
     *
     * @param appId  应用编号（主键）
     * @param config 配置内容
     *
     * @return 操作是否成功
     */
    boolean setConfig(@NonNull String appId, Config config);

    /**
     * 获取配置参数
     *
     * @param appId      应用编号（主键）
     * @param configName 配置项名称
     *
     * @return 配置项
     */
    Config getConfig(@NonNull String appId, String configName);

    /**
     * 获取配置参数列表
     *
     * @param appId 应用编号（主键）
     *
     * @return 配置列表
     */
    List<Config> getConfigList(@NonNull String appId);
}