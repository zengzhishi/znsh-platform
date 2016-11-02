/*
 * Copyright (c) 2016. Embedded Real-Time Computation Lab Of UESTC.
 *
 * 电子科技大学・信息与软件工程学院・嵌入式实时计算研究所
 *
 * http://www.is.uestc.edu.cn
 */
package uestc.ercl.znsh.platform.component.def;

import uestc.ercl.znsh.common.entity.Rule;
import uestc.ercl.znsh.common.exception.ZNSH_IllegalDataException;
import uestc.ercl.znsh.common.exception.ZNSH_IllegalFieldValueException;
import uestc.ercl.znsh.common.exception.ZNSH_InternalException;

public interface RuleManager
{
    Rule setRule(int flag, String json, String appId, int creator)
            throws ZNSH_IllegalFieldValueException, ZNSH_InternalException, ZNSH_IllegalDataException;
}