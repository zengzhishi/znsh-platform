/*
 * Copyright (c) 2016. Embedded Real-Time Computation Lab Of UESTC.
 *
 * 电子科技大学・信息与软件工程学院・嵌入式实时计算研究所
 *
 * http://www.is.uestc.edu.cn
 */
package uestc.ercl.znsh.platform.constants;

/**
 * 动态验证结果
 *
 * @apiNote 分别为：正确，错误，过期，不存在，系统错
 */
public enum VerifyCodeStatus
{
    VALID,
    INVALID,
    EXPIRED,
    NONE,
    ERROR
}