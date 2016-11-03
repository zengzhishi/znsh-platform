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
package uestc.ercl.znsh.platform.component;

import cn.sel.jutil.lang.JText;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;
import uestc.ercl.znsh.common.constant.RuleStatus;
import uestc.ercl.znsh.common.data.JsonUtil;
import uestc.ercl.znsh.common.entity.Rule;
import uestc.ercl.znsh.common.exception.ZNSH_DataAccessException;
import uestc.ercl.znsh.common.exception.ZNSH_IllegalArgumentException;
import uestc.ercl.znsh.common.exception.ZNSH_IllegalDataException;
import uestc.ercl.znsh.common.exception.ZNSH_InternalException;
import uestc.ercl.znsh.platform.component.def.RuleManager;
import uestc.ercl.znsh.platform.dao.RuleDAO;

import java.util.Objects;

/**
 * 规则管理工类（需要借助DAO实现）
 */
@Component
public class RuleManagerImpl implements RuleManager
{
    private final RuleDAO dao;

    public RuleManagerImpl(RuleDAO ruleDAO)
    {
        Objects.requireNonNull(ruleDAO, "规则DAO不能为空！");
        this.dao = ruleDAO;
    }

    /**
     * 创建（更新）规则信息
     *
     * @param flag    0-创建/other-更新
     * @param json    按Ginkgo SRL规范封装的JSON格式的规则信息
     * @param appId   所属应用编号
     * @param creator 创建者（业务人员）编号
     *
     * @return 操作结束后的规则对象
     *
     * @throws ZNSH_IllegalArgumentException
     * @throws ZNSH_InternalException
     * @throws ZNSH_IllegalDataException
     */
    @Override
    public Rule setRule(int flag, String json, String appId, int creator)
            throws ZNSH_IllegalArgumentException, ZNSH_InternalException, ZNSH_IllegalDataException
    {
        if(JText.isNullOrEmpty(json))
        {
            throw new ZNSH_IllegalDataException("规则信息不能为空！");
        }
        JsonNode jsonNode = JsonUtil.getObject(json, JsonNode.class);
        if(jsonNode == null)
        {
            throw new ZNSH_IllegalDataException("规则信息有误！");
        }
        return flag == 0 ? createRule(jsonNode, appId, creator) : updateRule(jsonNode);
    }

    private Rule createRule(JsonNode jsonNode, String appId, int creator)
            throws ZNSH_IllegalArgumentException, ZNSH_InternalException
    {
        Rule rule = new Rule();
        setRuleFields(rule, jsonNode);
        rule.setAppId(appId);
        rule.setRuleStatus(RuleStatus.NEW);
        rule.setCreateTime(System.currentTimeMillis());
        rule.setCreator(creator);
        return rule;
    }

    private Rule updateRule(JsonNode jsonNode)
            throws ZNSH_InternalException, ZNSH_IllegalArgumentException
    {
        String pk = jsonNode.get("pk").asText("");
        Rule rule = dao.select(pk);
        if(rule != null)
        {
            setRuleFields(rule, jsonNode);
            rule.setUpdateTime(System.currentTimeMillis());
        } else
        {
            throw new ZNSH_DataAccessException(String.format("不存在主键为'%s'的规则！", pk));
        }
        return rule;
    }

    private void setRuleFields(Rule rule, JsonNode fields)
            throws ZNSH_IllegalArgumentException, ZNSH_InternalException
    {
        rule.setName(fields.get("name").asText(null));
        rule.setDesc(fields.get("desc").asText(null));
        rule.setWarnLevel(fields.get("warning").asInt(-1));
        rule.setAdvice(fields.get("advice").asText(null));
        rule.setCondition(fields.get("cond").asText(null));
        rule.setResult(fields.get("result").asText(null));
        rule.setReturns(fields.get("returns").asText(null));
    }
}