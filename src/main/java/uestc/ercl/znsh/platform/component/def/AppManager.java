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

import cn.sel.jutil.annotation.note.NonNull;
import cn.sel.jutil.annotation.note.Nullable;
import uestc.ercl.znsh.common.constant.AppStatus;
import uestc.ercl.znsh.common.constant.AppType;
import uestc.ercl.znsh.common.entity.App;
import uestc.ercl.znsh.common.exception.ZNSH_IllegalArgumentException;
import uestc.ercl.znsh.common.exception.ZNSH_ServiceException;

import java.util.List;
import java.util.Map;

public interface AppManager extends AppConfigManager
{
    /**
     * 创建应用
     *
     * @param name     名称
     * @param desc     描述
     * @param type     类型
     * @param status   状态
     * @param master   责任人姓名
     * @param pid      责任人身份证号
     * @param phone    责任人手机
     * @param email    责任人邮箱
     * @param account  管理账号
     * @param password 登录密码
     *
     * @throws ZNSH_ServiceException
     * @throws ZNSH_IllegalArgumentException
     */
    void create(@NonNull String name, @Nullable String desc, @NonNull AppType type, @NonNull AppStatus status, @NonNull String master,
            @NonNull String pid, @NonNull String phone, @Nullable String email, @NonNull String account, @NonNull String password)
            throws ZNSH_IllegalArgumentException, ZNSH_ServiceException;

    /**
     * 根据应用编号（主键）检索应用信息
     *
     * @param appId 应用编号（主键）
     *
     * @return 应用对象
     */
    App get(@Nullable String appId)
            throws ZNSH_IllegalArgumentException, ZNSH_ServiceException;

    /**
     * 根据关键字查找应用信息
     *
     * @param key 应用编号/管理账号/手机/邮箱
     *
     * @return 应用对象
     */
    App find(@Nullable String key)
            throws ZNSH_IllegalArgumentException, ZNSH_ServiceException;

    /**
     * 查询应用列表
     *
     * @param pk     应用编号（模糊查询，置空时不限）
     * @param name   应用名称（模糊查询，置空时不限）
     * @param type   应用类型
     * @param status 应用状态
     * @param master 责任人姓名（模糊查询，置空时不限）
     * @param pid    责任人身份证号（模糊查询，置空时不限）
     * @param phone  责任人手机（模糊查询，置空时不限）
     * @param email  责任人邮箱（模糊查询，置空时不限）
     * @param from   查询起始序号
     * @param count  查询数量
     *
     * @return 应用对象列表
     */
    List<App> find(@Nullable String pk, @Nullable String name, @Nullable AppType type, @Nullable AppStatus status, @Nullable String master,
            @Nullable String pid, @Nullable String phone, @Nullable String email, long from, int count)
            throws ZNSH_IllegalArgumentException, ZNSH_ServiceException;

    /**
     * 更新应用信息
     *
     * @param pk      主键（应用编号）
     * @param name    名称（置空时不改）
     * @param desc    描述（置空时不改）
     * @param master  责任人姓名（置空时不改）
     * @param pid     责任人身份证号（置空时不改）
     * @param phone   责任人手机（置空时不改）
     * @param email   责任人邮箱（置空时不改）
     * @param account 管理账号（置空时不改）
     *
     * @throws ZNSH_IllegalArgumentException
     * @throws ZNSH_ServiceException
     */
    void update(@NonNull String pk, @Nullable String name, @Nullable String desc, @Nullable String master, @Nullable String pid,
            @Nullable String phone, @Nullable String email, @Nullable String account)
            throws ZNSH_IllegalArgumentException, ZNSH_ServiceException;

    /**
     * 更新应用密码
     *
     * @param pk       主键（应用编号）
     * @param password 密码
     *
     * @throws ZNSH_IllegalArgumentException
     * @throws ZNSH_ServiceException
     */
    void updatePassword(@NonNull String pk, @NonNull String password)
            throws ZNSH_IllegalArgumentException, ZNSH_ServiceException;

    /**
     * 删除应用
     *
     * @param pks 主键（应用编号）序列
     *
     * @return null-成功/else-失败项的pk及原因
     */
    Map<String, String> delete(@NonNull String... pks)
            throws ZNSH_IllegalArgumentException, ZNSH_ServiceException;

    /**
     * @param type
     * @param pks  主键（应用编号）序列
     *
     * @return null-成功/else-失败项的pk及原因
     */
    Map<String, String> setType(@NonNull AppType type, @NonNull String... pks)
            throws ZNSH_IllegalArgumentException, ZNSH_ServiceException;

    /**
     * 审核新应用（如批准，设置应用状态为{@link AppStatus#NORMAL}；否则设为{@link AppStatus#DISABLED}）
     *
     * @param accept 批准与否
     * @param pks    主键（应用编号）序列
     *
     * @return null-成功/else-失败项的pk及原因
     */
    Map<String, String> review(boolean accept, @NonNull String... pks)
            throws ZNSH_IllegalArgumentException, ZNSH_ServiceException;

    /**
     * 注销应用（设置应用状态为{@link AppStatus#DISABLED}，并通知服务集群移除该应用）
     *
     * @param pks 主键（应用编号）序列
     *
     * @return null-成功/else-失败项的pk及原因
     */
    Map<String, String> disable(@NonNull String... pks)
            throws ZNSH_IllegalArgumentException, ZNSH_ServiceException;

    /**
     * 激活应用（设置应用状态为{@link AppStatus#NORMAL}，并通知服务集群恢复对该应用的服务）
     *
     * @param pks 主键（应用编号）序列
     *
     * @return null-成功/else-失败项的pk及原因
     */
    Map<String, String> activate(@NonNull String... pks)
            throws ZNSH_IllegalArgumentException, ZNSH_ServiceException;

    /**
     * 挂起应用（设置应用状态为{@link AppStatus#SUSPEND}，并通知服务集群暂停对该应用的服务）
     *
     * @param pks 主键（应用编号）序列
     *
     * @return null-成功/else-失败项的pk及原因
     */
    Map<String, String> suspend(@NonNull String... pks)
            throws ZNSH_IllegalArgumentException, ZNSH_ServiceException;

    /**
     * 启动应用（设置应用状态为{@link AppStatus#NORMAL}，通知服务集群增加该应用）
     *
     * @param pks 主键（应用编号）序列
     *
     * @return null-成功/else-失败项的pk及原因
     */
    Map<String, String> start(@NonNull String... pks)
            throws ZNSH_IllegalArgumentException, ZNSH_ServiceException;

    /**
     * 停止应用（不改变应用状态，通知服务集群移除该应用）
     *
     * @param pks 主键（应用编号）序列
     *
     * @return null-成功/else-失败项的pk及原因
     */
    Map<String, String> stop(@NonNull String... pks)
            throws ZNSH_IllegalArgumentException, ZNSH_ServiceException;

    /**
     * 分配服务集群（通知原集群移除该应用、新集群添加该应用）
     *
     * @param clusterPk 集群编号
     * @param pks       主键（应用编号）序列
     *
     * @return null-成功/else-失败项的pk及原因
     */
    Map<String, String> setCluster(int clusterPk, String... pks)
            throws ZNSH_IllegalArgumentException, ZNSH_ServiceException;
}