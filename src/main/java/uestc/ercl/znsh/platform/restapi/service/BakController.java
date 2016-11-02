/*
 * Copyright (c) 2016. Embedded Real-Time Computation Lab Of UESTC.
 *
 * 电子科技大学・信息与软件工程学院・嵌入式实时计算研究所
 *
 * http://www.is.uestc.edu.cn
 */
package uestc.ercl.znsh.platform.restapi.service;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import uestc.ercl.znsh.platform.restapi.BaseController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 数据备份接口
 *
 * @apiNote 仅用于服务集群上传备份数据
 */
@Controller
@RequestMapping(path = "api/service/bak")
public class BakController extends BaseController
{
    @ResponseBody
    @RequestMapping(path = "data")
    public void result(MultipartHttpServletRequest request, HttpServletResponse response)
            throws IOException
    {
    }
}