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