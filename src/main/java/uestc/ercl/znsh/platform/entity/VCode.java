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
package uestc.ercl.znsh.platform.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 动态验证码
 */
@Entity
@Table(name = VCode.TABLE_NAME)
public class VCode
{
    public static final String TABLE_NAME = "T_VCODE";
    /**
     * 主键（应用编号或管理员编号的文本形式）
     */
    @Id
    @Column(name = "pk", length = 20)
    private String pk;
    /**
     * 验证码
     */
    @Column(name = "code", nullable = false, length = 6)
    private String code;
    /**
     * 创建时间
     */
    @Column(name = "create_time", nullable = false)
    private long createTime;

    public VCode()
    {
    }

    public VCode(String pk, String code, long createTime)
    {
        this.pk = pk;
        this.code = code;
        this.createTime = createTime;
    }

    @Override
    public String toString()
    {
        return "VCode{" + "pk='" + pk + '\'' + ", code='" + code + '\'' + ", createTime=" + createTime + '}';
    }

    public String getPk()
    {
        return pk;
    }

    public void setPk(String pk)
    {
        this.pk = pk;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public long getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(long createTime)
    {
        this.createTime = createTime;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime.getTime();
    }
}