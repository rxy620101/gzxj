package com.ruoyi.scholarShip.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 辅导员基础信息对象 instror_info
 *
 * @author raoxy
 * @date 2022-04-11
 */
public class InstrorInfo extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 表主键
     */
    private Long id;

    /**
     * 辅导员工号
     */
    @Excel(name = "辅导员工号", prompt = "长度为10")
    private String instructorId;

    /*
    姓名
     */
    @Excel(name = "辅导员姓名")
    private String instructorName;

    @Excel(name = "性别", readConverterExp = "0=男,1=女")
    private String sex;
    /**
     * 学院id
     */
    private Long collegeId;
    /**
     * 联系电话
     */
    @Excel(name = "联系电话")
    private String tel;
    /*
      学院名称
       */
    @Excel(name = "所属学院名称")
    private String collegeName;

    /**
     * 指导年级
     */
    @Excel(name = "指导年级")
    private String guideGrade;
    /*
    指导专业数组
     */
    @Excel(name = "指导专业", prompt = "用逗号(中文状态)进行分隔")
    private String guideMajorNames;

    /**
     * 指导专业id集合（逗号分隔）
     */
    private String guideMajorIds;

    /**
     * 状态 0表示正常，1表示注销
     */
    private String status;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;

    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setInstructorId(String instructorId) {
        this.instructorId = instructorId;
    }

    @NotBlank(message = "职工号不允许为空")
    public String getInstructorId() {
        return instructorId;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public void setCollegeId(Long colleagueId) {
        this.collegeId = colleagueId;
    }

    public Long getCollegeId() {
        return collegeId;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    @Pattern(regexp = "^[1][3,4,5,6,7,8,9][0-9]{9}$", message = "手机号格式有误")
    public String getTel() {
        return tel;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public String getGuideGrade() {
        return guideGrade;
    }

    public void setGuideGrade(String guideGrade) {
        this.guideGrade = guideGrade;
    }

    public String getGuideMajorIds() {
        return guideMajorIds;
    }

    public void setGuideMajorIds(String guideMajorIds) {
        this.guideMajorIds = guideMajorIds;
    }

    public String  getGuideMajorNames() {
        return guideMajorNames;
    }

    public void setGuideMajorNames(String guideMajorNames) {
        this.guideMajorNames = guideMajorNames;
    }

    public String getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("instructorName", getInstructorName())
                .append("instructorId", getInstructorId())
                .append("colleagueId", getCollegeId())
                .append("tel", getTel())
                .append("status", getStatus())
                .append("delFlag", getDelFlag())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
