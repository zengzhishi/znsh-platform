/*
 * Copyright (c) 2016. Embedded Real-Time Computation Lab Of UESTC.
 *
 * 电子科技大学・信息与软件工程学院・嵌入式实时计算研究所
 *
 * http://www.is.uestc.edu.cn
 */
package uestc.ercl.znsh.platform.restapi.app;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import uestc.ercl.znsh.common.exception.ZNSH_IllegalDataException;
import uestc.ercl.znsh.common.exception.ZNSH_IllegalFieldValueException;
import uestc.ercl.znsh.common.exception.ZNSH_InternalException;
import uestc.ercl.znsh.platform.component.RuleManagerImpl;
import uestc.ercl.znsh.platform.restapi.BaseController;

import java.io.IOException;

@Controller
@RequestMapping(path = "api/rule")
public class RuleController extends BaseController
{
    @ResponseBody
    @RequestMapping(path = "set")
    public String set(int flag, String json)
            throws IOException
    {
        String appId = "";
        int creator = 0;
        RuleManagerImpl ruleMgrImpl = new RuleManagerImpl(null);
        try
        {
            ruleMgrImpl.setRule(flag, json, appId, creator);
        } catch(ZNSH_IllegalFieldValueException e)
        {
            e.printStackTrace();
        } catch(ZNSH_InternalException e)
        {
            e.printStackTrace();
        } catch(ZNSH_IllegalDataException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    @ResponseBody
    @RequestMapping(path = "del")
    public String del(String pkOrId)
            throws IOException
    {
        return null;
    }
}