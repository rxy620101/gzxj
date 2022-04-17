package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 专业-课程关联对象 major_course
 * 
 * @author raoxy
 * @date 2022-04-05
 */
public class MajorCourse extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 专业id */
    private Long collegeId;

    /** 课程Id */
    @Excel(name = "课程Id")
    private Long courseId;

    public void setCollegeId(Long collegeId) 
    {
        this.collegeId = collegeId;
    }

    public Long getCollegeId() 
    {
        return collegeId;
    }
    public void setCourseId(Long courseId) 
    {
        this.courseId = courseId;
    }

    public Long getCourseId() 
    {
        return courseId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("collegeId", getCollegeId())
            .append("courseId", getCourseId())
            .toString();
    }
}
