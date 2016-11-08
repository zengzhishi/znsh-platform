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
package uestc.ercl.znsh.platform.restapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import uestc.ercl.znsh.common.constant.AppStatus;
import uestc.ercl.znsh.common.constant.AppType;
import uestc.ercl.znsh.common.entity.App;
import uestc.ercl.znsh.common.exception.ZNSH_IllegalArgumentException;
import uestc.ercl.znsh.platform.dao.TestDAO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 测试接口（用于检测程序是否部署成功）
 *
 * @apiNote 仅用于服务集群上传备份数据
 */
@Controller
@RequestMapping(path = "api/test")
public class TestController extends BaseController
{
    private final TestDAO testDAO;

    @Autowired
    public TestController(TestDAO testDAO)
    {
        Assert.notNull(testDAO);
        this.testDAO = testDAO;
    }

    @ResponseBody
    public String hello(HttpServletRequest request, HttpServletResponse response)
            throws IOException
    {
        return "欢迎使用智能审核系统！";
    }

    @ResponseBody
    @RequestMapping(path = "html")
    public String html(HttpServletRequest request, HttpServletResponse response)
            throws IOException
    {
        return "欢迎使用智能审核系统！";
    }

    @ResponseBody
    @RequestMapping(path = "txt")
    public Object txt(HttpServletRequest request, HttpServletResponse response)
            throws IOException
    {
        return "智能审核系统";
    }

    @ResponseBody
    @RequestMapping(path = "text", produces = "text/plain;charset=UTF-8")
    public String text(HttpServletRequest request, HttpServletResponse response)
            throws IOException
    {
        return "智能审核系统";
    }

    @ResponseBody
    @RequestMapping(path = "obj")
    public Object obj(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ZNSH_IllegalArgumentException
    {
        App app = new App();
        app.setPk("pkpkpk");
        app.setName("TestApp");
        app.setDesc("测试应用");
        app.setType(AppType.CLOUD);
        app.setStatus(AppStatus.NORMAL);
        app.setCreateTime(System.currentTimeMillis());
        app.setMaster("master");
        app.setPid("pid");
        app.setPhone("phone");
        app.setEmail("email");
        app.setAccount("account");
        app.setPassword("password");
        return app;
    }

    @ResponseBody
    @RequestMapping(path = "sql", method = RequestMethod.GET)
    public List sql(HttpServletRequest request, HttpServletResponse response, String key)
            throws IOException
    {
        return testDAO.test();
    }
}