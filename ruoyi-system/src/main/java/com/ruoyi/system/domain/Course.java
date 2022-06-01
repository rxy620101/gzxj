package com.ruoyi.system.domain;
import com.ruoyi.common.core.domain.entity.SysColleageMajor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 课程对象 course
 * 
 * @author raoxy
 * @date 2022-04-05
 */
public class Course extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 课程Id */
    private Long coseId;

    //专业名称
    @Excel(name="专业名称")
    private String majorName;
    /** 课程名称 */
    @Excel(name = "课程名称")
    private String coseName;

    /** 开设学年 */
    @Excel(name = "开设学年")
    private String startYear;

    /** 开设学期（1=第一学期，2=第二学期） */
    @Excel(name = "开设学期",prompt = "如：1/2")
    private String validTerm;

    @Excel(name="开设年级")
    private String grade;
    
    @Excel(name="课程学分")
    private double credit;
    @Excel(name="课程性质",dictType = "course_type")
    private String courseType;

    //删除标志（0存在，2代表删除）
    private int delFlag;

    private String status;
    /** 专业ids */
    private Long[] majorIds;

    /** 专业对象 */
    private List<SysColleageMajor> majors;

    //分数  ---该课程学生所获的分数
    private Double score;

    //学分  ---该课程学生所获的学分
    private Double haveCredit;

    public Double getHaveCredit() {
        return haveCredit;
    }

    public void setHaveCredit(Double haveCredit) {
        this.haveCredit = haveCredit;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public void setCoseId(Long coseId)
    {
        this.coseId = coseId;
    }
    public Long getCoseId()
    {
        return coseId;
    }
    public void setCoseName(String coseName) 
    {
        this.coseName = coseName;
    }
   @NotBlank(message = "课程名称不能为空")
    public String getCoseName()
    {
        return coseName;
    }
    public void setStartYear(String startYear) 
    {
        this.startYear = startYear;
    }
    @NotBlank(message = "开设学年不能为空")
    public String getStartYear() 
    {
        return startYear;
    }
    public void setValidTerm(String validTerm) 
    {
        this.validTerm = validTerm;
    }
    @NotBlank(message = "开设学期不能为空")
    public String getValidTerm() 
    {
        return validTerm;
    }
    @NotBlank(message = "开设年级不能为空")
    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getMajorName() {
        return majorName;
    }

    public void setMajorName(String majorName) {
        this.majorName = majorName;
    }
    @NotNull(message = "学分不能为空")
    public double getCredit() {
        return credit;
    }

    public void setCredit(double credit) {
        this.credit = credit;
    }
    @NotBlank(message = "课程性质不能为空")
    public String getCourseType() {
        return courseType;
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType;
    }

    public int getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(int delFlag) {
        this.delFlag = delFlag;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    public Long[] getMajorIds() {
        return majorIds;
    }

    public void setMajorIds(Long[] majorIds) {
        this.majorIds = majorIds;
    }

    public List<SysColleageMajor> getMajors() {
        return majors;
    }

    public void setMajors(List<SysColleageMajor> majors) {
        this.majors = majors;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("coseId", getCoseId())
            .append("coseName", getCoseName())
            .append("startYear", getStartYear())
            .append("validTerm", getValidTerm())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
