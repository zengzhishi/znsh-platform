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
import uestc.ercl.znsh.common.constant.AppType;
import uestc.ercl.znsh.common.constant.DataType;
import uestc.ercl.znsh.common.constant.JobType;
import uestc.ercl.znsh.common.data.JsonUtil;
import uestc.ercl.znsh.common.entity.*;
import uestc.ercl.znsh.common.exception.ZNSH_IllegalFieldValueException;
import uestc.ercl.znsh.common.exception.ZNSH_InternalException;
import uestc.ercl.znsh.common.security.Authenticator;
import uestc.ercl.znsh.common.security.TokenRole;
import uestc.ercl.znsh.platform.restapi.BaseController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * 工作资源加载接口
 *
 * @apiNote 仅供服务集群调用
 */
@Controller
@RequestMapping(path = "api/service")
public class ResourceController extends BaseController
{
    @ResponseBody
    @RequestMapping(path = "res")
    public List res(String signs, HttpServletResponse response)
            throws IOException
    {
        Map<String, String> signList = null;
        if(signs != null)
        {
            signList = JsonUtil.getObject(signs, JsonUtil.MAP_STRING_STRING);
            if(signList == null)
            {
                response.sendError(400, "非法参数！");
            }
        }
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        App app = null;
        try
        {
            app = SimulatedResource.app();
            map.put("app", app);
            map.put("token", SimulatedResource.token());
            map.put("taskLimit", SimulatedResource.limit());
            map.put("sheets", SimulatedResource.sheets());
            map.put("jobs", SimulatedResource.jobs());
            map.put("warnings", SimulatedResource.warnings());
        } catch(ZNSH_IllegalFieldValueException e)
        {
            response.sendError(400, "非法参数！" + e);
        } catch(ZNSH_InternalException e)
        {
            response.sendError(500, "内部错误！" + e);
        } catch(Exception e)
        {
            response.sendError(500, "未知错误！" + e);
        }
        String appMd5 = "testapp";
        if(app != null)
        {
            if(signList == null || !appMd5.equals(signList.get(app.appId())))
            {
                list.add(map);
            }
        }
        System.out.println("模拟数据：" + list);
        return list;
    }

    /**
     * TODO 模拟数据
     */
    public static class SimulatedResource
    {
        public static App app()
                throws ZNSH_IllegalFieldValueException
        {
            App app = new App();
            app.setPk("TestApp");
            app.setName("选课审核");
            app.setMaster("Sel");
            app.setEmail("philshang@163.com");
            app.setPhone("18628960293");
            app.setPid("1234567890");
            app.setAppType(AppType.CLOUD);
            return app;
        }

        public static String token()
        {
            return Authenticator.createToken("TestApp", TokenRole.TERMINAL, "TEST_TOKEN").token();
        }

        public static int limit()
        {
            return 1048576;
        }

        public static List<Warning> warnings()
        {
            List<Warning> warnings = new ArrayList<>();
            warnings.add(new Warning("TestApp", 0, "级别0", false));
            warnings.add(new Warning("TestApp", 1, "级别1", true));
            warnings.add(new Warning("TestApp", 2, "级别2", true));
            warnings.add(new Warning("TestApp", 3, "级别3", true));
            warnings.add(new Warning("TestApp", 4, "级别4", true));
            return warnings;
        }

        public static List<Job> jobs()
                throws IOException, ZNSH_IllegalFieldValueException, ZNSH_InternalException
        {
            ArrayList<Job> jobs = new ArrayList<>();
            Job job = new Job();
            job.setPk(1);
            job.setId("CourseRegister");
            job.setName("选课审核");
            job.setDesc("对选课请求进行检查");
            job.setRules(getRules());
            job.setJobType(JobType.SERIAL);
            jobs.add(job);
            return jobs;
        }

        public static HashMap<Integer, Rule> getRules()
                throws IOException, ZNSH_IllegalFieldValueException, ZNSH_InternalException
        {
            Properties properties = new Properties();
            properties.load(ResourceController.class.getClassLoader().getResource("rule.properties").openStream());
            HashMap<Integer, Rule> result = new HashMap<>();
            //
            Rule rule_course = new Rule();
            rule_course.setName("RULE_C");
            rule_course.setDesc("检查选课人数是否已达到上限。");
            rule_course.setResult("课程已满！");
            rule_course.setAdvice("请选择其它课程！");
            rule_course.setWarnLevel(1);
            rule_course.setCondition(properties.getProperty("cond_c"));
            rule_course.setReturns(properties.getProperty("rets_c"));
            //
            Rule rule_student = new Rule();
            rule_student.setName("RULE_S");
            rule_student.setDesc("检查选课学生是否为一年级新生。");
            rule_student.setResult("只有新生才可以选课！");
            rule_student.setWarnLevel(2);
            rule_student.setCondition(properties.getProperty("cond_s"));
            rule_student.setReturns(properties.getProperty("rets_s"));
            //
            result.put(1, rule_student);
            result.put(2, rule_course);
            return result;
        }

        public static List<Sheet> sheets()
                throws ZNSH_IllegalFieldValueException
        {
            //-----------------------------------------------------
            Field f_id = new Field();
            f_id.setPk(3645);
            f_id.setId("id");
            f_id.setName("编号");
            f_id.setDesc("课程编号");
            f_id.setDataType(DataType.NUMBER);
            f_id.setNullable(false);
            //
            Field f_name = new Field();
            f_name.setPk(5441);
            f_name.setId("name");
            f_name.setName("名称");
            f_name.setDesc("课程名称");
            f_name.setDataType(DataType.TEXT);
            f_name.setNullable(false);
            //
            Field f_max = new Field();
            f_max.setPk(6546);
            f_max.setId("maxLearners");
            f_max.setName("最大人数");
            f_max.setDesc("该课程最多选课人数");
            f_max.setDataType(DataType.NUMBER);
            f_max.setNullable(false);
            //
            Field f_cur = new Field();
            f_cur.setPk(365653);
            f_cur.setId("curLearners");
            f_cur.setName("当前人数");
            f_cur.setDesc("该课程当前选课人数");
            f_cur.setDataType(DataType.NUMBER);
            f_cur.setNullable(false);
            //
            Sheet t_cource = new Sheet();
            t_cource.setPk(321505);
            t_cource.setId("Course");
            t_cource.setName("课程信息表");
            t_cource.setDesc("可选课程的信息");
            t_cource.addField(f_id);
            t_cource.addField(f_name);
            t_cource.addField(f_max);
            t_cource.addField(f_cur);
            //-----------------------------------------------------
            Field f_cid = new Field();
            f_cid.setPk(352656);
            f_cid.setId("courseId");
            f_cid.setName("课程编号");
            f_cid.setDesc("选课申请中的课程编号");
            f_cid.setDataType(DataType.NUMBER);
            f_cid.setNullable(false);
            //
            Field f_sname = new Field();
            f_sname.setPk(98765);
            f_sname.setId("stuName");
            f_sname.setName("学生姓名");
            f_sname.setDesc("选课申请中的学生姓名");
            f_sname.setDataType(DataType.TEXT);
            f_sname.setNullable(false);
            //
            Field f_grade = new Field();
            f_grade.setPk(857498);
            f_grade.setId("stuGrade");
            f_grade.setName("学生年级");
            f_grade.setDesc("选课申请中的学生所在的年级");
            f_grade.setDataType(DataType.NUMBER);
            f_grade.setNullable(false);
            //
            Field f_class = new Field();
            f_class.setPk(6123);
            f_class.setId("stuClass");
            f_class.setName("学生班号");
            f_class.setDesc("选课申请中的学生所在的班");
            f_class.setDataType(DataType.NUMBER);
            f_class.setNullable(false);
            //
            Sheet t_registry = new Sheet();
            t_registry.setPk(52456);
            t_registry.setId("CourseRegistry");
            t_registry.setName("选课申请表");
            t_registry.setDesc("学生提交的选课信息");
            t_registry.addField(f_cid);
            t_registry.addField(f_sname);
            t_registry.addField(f_grade);
            t_registry.addField(f_class);
            //
            List<Sheet> sheets = new ArrayList<>();
            sheets.add(t_cource);
            sheets.add(t_registry);
            return sheets;
        }
    }
}