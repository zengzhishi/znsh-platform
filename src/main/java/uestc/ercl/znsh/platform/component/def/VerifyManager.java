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
package uestc.ercl.znsh.platform.component.def;

import uestc.ercl.znsh.platform.constants.VerifyCodeStatus;

/**
 * Created by sel on 16-10-26.
 */
public interface VerifyManager
{
    /**
     * 生成动态验证码
     *
     * @return 6位随机字符串
     */
    String generateVerifyCode();

    /**
     * 使用手机短信验证
     *
     * @param number 手机号码
     *
     * @return 验证请求发送成功与否
     */
    boolean verifyBySMS(String number);

    /**
     * 使用电子邮件验证
     *
     * @param address 邮箱地址
     *
     * @return 验证请求发送成功与否
     */
    boolean verifyByEmail(String address);

    /**
     * 检查系统管理员动态验证码
     *
     * @param adminPk 管理员编号
     * @param code    用户提交的验证码
     *
     * @return {@link VerifyCodeStatus}
     */
    VerifyCodeStatus checkAdminVerifyCode(int adminPk, String code);

    /**
     * 检查应用管理员动态验证码
     *
     * @param appId 应用编号
     * @param code  用户提交的验证码
     *
     * @return {@link VerifyCodeStatus}
     */
    VerifyCodeStatus checkAppVerifyCode(String appId, String code);
}
