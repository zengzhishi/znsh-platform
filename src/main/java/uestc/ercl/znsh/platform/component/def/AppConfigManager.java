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