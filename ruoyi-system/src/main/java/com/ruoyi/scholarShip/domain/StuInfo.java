package com.ruoyi.scholarShip.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * 学生基础信息对象 stu_info
 * 
 * @author raoxy
 * @date 2022-04-11
 */
public class StuInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 表主键 */
    private Long id;

    /** 学号 */
    @Excel(name = "学号")
    private String sno;

    /** 学生姓名 */
    @Excel(name = "学生姓名")
    private String stuName;

    /** 性别（0男 1女 2未知） */
    @Excel(name = "性别", readConverterExp = "0=男,1=女,2=未知")
    private String sex;

    /** 身份证号码 */
    @Excel(name = "身份证号码")
    private String certNum;

    /** 院系id */
    private Long collegeId;

    /** 专业id */
    private Long majorId;

    /** 院系名称*/
    @Excel(name = "院系名称")
    private String collegeName;

    /** 专业名称*/
    @Excel(name = "专业名称")
    private String  majorName;


    /** 年级 */
    @Excel(name = "年级")
    private String grade;

    /** 班级 */
    @Excel(name = "班级")
    private String stuClass;

    /** 入学时间 */
    @JsonFormat(pattern="yyyy-MM-dd")
    @Excel(name = "入学时间", width = 30, dateFormat = "yyyy/MM/dd",prompt = "日期格式为yyyy/MM/dd ")
    private Date registerTime;


    /** 联系电话 */
    private String tel;

    /** 家庭地址 */
    private String addr;

    /** 家庭联系人 */
    private String guidName;

    /** 联系人电话 */
    private String guidmanTel;

    /** 卡号 */
    private String cardNum;

    /** 状态 0表示正常，1表示注销 */
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;


    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setSno(String sno) 
    {
        this.sno = sno;
    }
    @NotBlank(message = "学号不能为空")
    public String getSno() 
    {
        return sno;
    }
    public void setStuName(String stuName) 
    {
        this.stuName = stuName;
    }
    @NotBlank(message = "姓名不能为空")
    public String getStuName() 
    {
        return stuName;
    }
    public void setSex(String sex) 
    {
        this.sex = sex;
    }

    public String getSex() 
    {
        return sex;
    }
    public void setCertNum(String certNum) 
    {
        this.certNum = certNum;
    }
    @NotBlank(message = "身份证号不能为空")
    public String getCertNum() 
    {
        return certNum;
    }
    public void setCollegeId(Long collegeId) 
    {
        this.collegeId = collegeId;
    }

    public Long getCollegeId() 
    {
        return collegeId;
    }
    public void setMajorId(Long majorId) 
    {
        this.majorId = majorId;
    }
    public Long getMajorId() 
    {
        return majorId;
    }
    public void setGrade(String grade) 
    {
        this.grade = grade;
    }
    @NotBlank(message = "年级不能为空")
    public String getGrade() 
    {
        return grade;
    }

    public void setStuClass(String stuClass) {
        this.stuClass = stuClass;
    }

    @NotBlank(message = "班级不能为空")
    public String getStuClass()
    {
        return stuClass;
    }
    public void setRegisterTime(Date registerTime) 
    {
        this.registerTime = registerTime;
    }
    public Date getRegisterTime() 
    {
        return registerTime;
    }
    public void setTel(String tel) 
    {
        this.tel = tel;
    }
    public String getTel() 
    {
        return tel;
    }
    public void setAddr(String addr) 
    {
        this.addr = addr;
    }

    public String getAddr() 
    {
        return addr;
    }
    public void setGuidName(String guidName) 
    {
        this.guidName = guidName;
    }

    public String getGuidName() 
    {
        return guidName;
    }
    public void setGuidmanTel(String guidmanTel) 
    {
        this.guidmanTel = guidmanTel;
    }

    public String getGuidmanTel() 
    {
        return guidmanTel;
    }
    public void setCardNum(String cardNum) 
    {
        this.cardNum = cardNum;
    }

    public String getCardNum() 
    {
        return cardNum;
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
    @NotBlank(message = "学院名称不能为空")
    public String  getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String  collegeName) {
        this.collegeName = collegeName;
    }

    public void setMajorName(String  majorName) {
        this.majorName = majorName;
    }
    @NotBlank(message = "专业名称不能为空")
    public String getMajorName() {
        return majorName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("sno", getSno())
            .append("stuName", getStuName())
            .append("sex", getSex())
            .append("certNum", getCertNum())
            .append("collegeId", getCollegeId())
            .append("majorId", getMajorId())
            .append("grade", getGrade())
            .append("stuClass", getStuClass())
            .append("registerTime", getRegisterTime())
            .append("tel", getTel())
            .append("addr", getAddr())
            .append("guidName", getGuidName())
            .append("guidmanTel", getGuidmanTel())
            .append("cardNum", getCardNum())
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
