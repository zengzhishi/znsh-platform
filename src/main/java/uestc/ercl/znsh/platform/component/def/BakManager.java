/*
 * Copyright (c) 2016. Embedded Real-Time Computation Lab Of UESTC.
 *
 * 电子科技大学・信息与软件工程学院・嵌入式实时计算研究所
 *
 * http://www.is.uestc.edu.cn
 */
package uestc.ercl.znsh.platform.component.def;

import cn.sel.jutil.annotation.note.NonNull;
import uestc.ercl.znsh.common.entity.Sheet;

import java.util.List;
import java.util.Map;

public interface BakManager
{
    /**
     * 查询所有应用的审核报表集合（按应用编号分组）
     *
     * @return String-应用编号，List-审核报表列表
     */
    Map<String, List<Sheet>> getAllAppSheets();

    /**
     * 查询指定应用的审核报表列表
     *
     * @param appId 应用编号
     *
     * @return 审核报表列表
     */
    List<Sheet> getAppSheets(@NonNull String appId);

    /**
     * 查询所有应用最后一个已完成“数据备份”的任务的时间戳
     *
     * @return String-应用编号，Long-时间戳
     */
    Map<String, Long> getLastTask();

    /**
     * 向服务集群发送“数据备份”通知 TODO 未确定
     *
     * @return 操作是否成功
     */
    boolean notifyDataBak();

    /**
     * 保存备份数据 TODO 未确定
     *
     * @return 操作是否成功
     */
    boolean saveData();
}