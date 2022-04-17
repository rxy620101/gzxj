package com.ruoyi.scholarShip.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.system.domain.Course;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

/**
 * 成绩信息对象 grade_info
 * 
 * @author raoxy
 * @date 2022-04-16
 */
public class GradeInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 表主键 */
    private Long gradeId;

    /** 学号 */
    @Excel(name = "学号")
    private String sno;

    /** 学生姓名 */
    @Excel(name = "学生姓名")
    private String stuName;

    /** 课程id */
    @Excel(name = "课程id")
    private Long coseId;

    /** 分数 */
    @Excel(name = "分数")
    private Long score;

    /** 课程所获学分 */
    @Excel(name = "课程所获学分")
    private Long credit;

    /** 删除标志（0代表存在 2代表删除） */
    private Long delFlag;

    /** 课程信息 */
    private Course course;

    public void setGradeId(Long gradeId) 
    {
        this.gradeId = gradeId;
    }

    public Long getGradeId() 
    {
        return gradeId;
    }
    public void setSno(String sno) 
    {
        this.sno = sno;
    }

    public String getSno() 
    {
        return sno;
    }
    public void setStuName(String stuName) 
    {
        this.stuName = stuName;
    }

    public String getStuName() 
    {
        return stuName;
    }
    public void setCoseId(Long coseId) 
    {
        this.coseId = coseId;
    }

    public Long getCoseId() 
    {
        return coseId;
    }
    public void setScore(Long score) 
    {
        this.score = score;
    }

    public Long getScore() 
    {
        return score;
    }
    public void setCredit(Long credit) 
    {
        this.credit = credit;
    }

    public Long getCredit() 
    {
        return credit;
    }
    public void setDelFlag(Long delFlag) 
    {
        this.delFlag = delFlag;
    }

    public Long getDelFlag() 
    {
        return delFlag;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("gradeId", getGradeId())
            .append("sno", getSno())
            .append("stuName", getStuName())
            .append("coseId", getCoseId())
            .append("score", getScore())
            .append("credit", getCredit())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .append("course", getCourse())
            .toString();
    }
}
