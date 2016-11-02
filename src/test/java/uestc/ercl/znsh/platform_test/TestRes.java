/*
 * Copyright (c) 2016. Embedded Real-Time Computation Lab Of UESTC.
 *
 * 电子科技大学・信息与软件工程学院・嵌入式实时计算研究所
 *
 * http://www.is.uestc.edu.cn
 */
package uestc.ercl.znsh.platform_test;

import org.junit.Test;
import uestc.ercl.znsh.common.exception.ZNSH_IllegalFieldValueException;
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
        } catch(ZNSH_IllegalFieldValueException | ZNSH_InternalException e)
        {
            e.printStackTrace();
        }
    }
}