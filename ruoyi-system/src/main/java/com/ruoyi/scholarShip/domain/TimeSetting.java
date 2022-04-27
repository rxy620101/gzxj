package com.ruoyi.scholarShip.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 申请时间参数对象 time_setting
 * 
 * @author raoxy
 * @date 2022-04-23
 */
public class TimeSetting extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 参数id */
    private Long settingId;

    /** 设置类型(1 奖学金，2助学金） */
    @Excel(name = "设置类型(1 奖学金，2助学金）")
    private String setType;

    /** 设置学年 */
    @Excel(name = "设置学年")
    private String setYear;

    /** 设置学期(奖学金每学期一次，助学金一学年一次） */
    @Excel(name = "设置学期(奖学金每学期一次，助学金一学年一次）")
    private String setTerm;

    /** 开始申请时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "开始申请时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date startTime;

    /** 结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date endTime;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public Long getSettingId() {
        return settingId;
    }

    public void setSettingId(Long settingId) {
        this.settingId = settingId;
    }

    public void setSetType(String setType)
    {
        this.setType = setType;
    }

    public String getSetType() 
    {
        return setType;
    }
    public void setSetYear(String setYear) 
    {
        this.setYear = setYear;
    }

    public String getSetYear() 
    {
        return setYear;
    }
    public void setSetTerm(String setTerm) 
    {
        this.setTerm = setTerm;
    }

    public String getSetTerm() 
    {
        return setTerm;
    }
    public void setStartTime(Date startTime) 
    {
        this.startTime = startTime;
    }

    public Date getStartTime() 
    {
        return startTime;
    }
    public void setEndTime(Date endTime) 
    {
        this.endTime = endTime;
    }

    public Date getEndTime() 
    {
        return endTime;
    }
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }
    public void setDelFlag(String delFlag) 
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag() 
    {
        return delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("setting", getSettingId())
            .append("setType", getSetType())
            .append("setYear", getSetYear())
            .append("setTerm", getSetTerm())
            .append("startTime", getStartTime())
            .append("endTime", getEndTime())
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
