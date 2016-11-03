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
package uestc.ercl.znsh.platform_test;

import org.junit.Test;
import uestc.ercl.znsh.common.exception.ZNSH_IllegalArgumentException;
import uestc.ercl.znsh.common.exception.ZNSH_InternalException;
import uestc.ercl.znsh.platform.restapi.service.ResourceController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sel on 16-9-6.
 */
public class TestRes
{
    @Test
    public void test()
            throws Exception
    {
        try
        {
            Map<String, Object> map = new HashMap<>();
            map.put("app", ResourceController.SimulatedResource.app());
            map.put("sheets", ResourceController.SimulatedResource.sheets());
            map.put("jobs", ResourceController.SimulatedResource.jobs());
            map.put("warnings", ResourceController.SimulatedResource.warnings());
            System.out.print(map);
        } catch(ZNSH_IllegalArgumentException | ZNSH_InternalException e)
        {
            e.printStackTrace();
        }
    }
}