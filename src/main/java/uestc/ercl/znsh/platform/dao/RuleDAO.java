/*
 * Copyright (c) 2016. Embedded Real-Time Computation Lab Of UESTC.
 *
 * 电子科技大学・信息与软件工程学院・嵌入式实时计算研究所
 *
 * http://www.is.uestc.edu.cn
 */
package uestc.ercl.znsh.platform.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import uestc.ercl.znsh.common.constant.RuleStatus;
import uestc.ercl.znsh.common.entity.Rule;

import java.util.List;

/**
 * 审核规则的数据访问组件
 */
@Repository
public class RuleDAO extends SuperDAO
{
    public RuleDAO(JdbcTemplate jdbcTemplate)
    {
        super(jdbcTemplate);
    }

    public boolean insertOrUpdateRule(Rule rule)
    {
        return false;
    }

    public boolean delete(String pkOrId)
    {
        return false;
    }

    public boolean updateStatus(String pkOrId, RuleStatus ruleStatus)
    {
        return false;
    }

    public Rule select(String id)
    {
        return null;
    }

    public List<Rule> select(String name, String desc, int level, int from, int count)
    {
        return null;
    }
}