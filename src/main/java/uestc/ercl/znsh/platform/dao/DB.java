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
package uestc.ercl.znsh.platform.dao;

import uestc.ercl.znsh.common.entity.App;
import uestc.ercl.znsh.common.entity.Cluster;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 模拟数据库（用于在DAO未完成前，使用模拟数据测试流程完整性）
 *
 * @apiNote 增、删、改的逻辑正常；查询时返回所有数据，无法支持模糊查询。
 */
class DB
{
    static Map<Integer, Cluster> clusterMap = new LinkedHashMap<>();
    static Map<String, App> appMap = new LinkedHashMap<>();
}