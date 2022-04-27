package com.ruoyi.scholarShip.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 助学金参数设置对象 hard_setting
 * 
 * @author raoxy
 * @date 2022-04-23
 */
public class HardSetting extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 参数id */
    private Long settingId;

    /** 设置学年 */
    @Excel(name = "设置学年")
    private String setYear;

    /** 学院id */
    @Excel(name = "学院id")
    private Long collegeId;

    /** 该学院对应的总名额 */
    @Excel(name = "该学院对应的总名额")
    private Integer allNum;

    /** 辅导员工号 */
    @Excel(name = "辅导员工号")
    private Long instructorId;

    /** 辅导员手下的名额 */
    @Excel(name = "辅导员手下的名额")
    private Integer num;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;
    //学院名称
    private String collegeName;
    //辅导员姓名
    private String instructorName;
    //年级
    private  String grade;

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public String getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }

    public Long getSettingId() {
        return settingId;
    }

    public void setSettingId(Long settingId) {
        this.settingId = settingId;
    }
    public void setSetYear(String setYear)
    {
        this.setYear = setYear;
    }

    public String getSetYear() 
    {
        return setYear;
    }
    public void setCollegeId(Long collegeId) 
    {
        this.collegeId = collegeId;
    }

    public Long getCollegeId() 
    {
        return collegeId;
    }
    public void setAllNum(Integer allNum)
    {
        this.allNum = allNum;
    }

    public Integer getAllNum()
    {
        return allNum;
    }
    public void setInstructorId(Long instructorId) 
    {
        this.instructorId = instructorId;
    }

    public Long getInstructorId() 
    {
        return instructorId;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
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
            .append("setYear", getSetYear())
            .append("collegeId", getCollegeId())
            .append("allNum", getAllNum())
            .append("instructorId", getInstructorId())
            .append("num", getNum())
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
