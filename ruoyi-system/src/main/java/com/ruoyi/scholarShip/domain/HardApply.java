package com.ruoyi.scholarShip.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 助学金申请对象 hard_apply
 * 
 * @author raoxy
 * @date 2022-04-29
 */
@Data
public class HardApply extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 申请编号 */
    private Long applyId;

    /** 学号 */
    @Excel(name = "学号")
    private String sno;

    /** 姓名 */
    @Excel(name = "姓名")
    private String stuName;

    /** 申请学年 */
    @Excel(name = "申请学年")
    private String applyYear;

    /** 家庭总人口 */
    @Excel(name = "家庭总人口")
    private Long familyNum;

    /** 家庭年平均收入 */
    @Excel(name = "家庭年平均收入")
    private BigDecimal avgIncome;

    /** 家庭情况 */
    @Excel(name = "家庭情况")
    private String hardCondition;

    /** 证明材料图片 */
    @Excel(name = "证明材料图片")
    private String img;

    /** 进度（0 待审核 1通过 2 失败） */
    @Excel(name = "进度", readConverterExp = "0=,待=审核,1=通过,2=,失=败")
    private String process;

    /** 申请时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "申请时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date applyTime;

    /** 辅导员姓名 */
    @Excel(name = "辅导员姓名")
    private String instructorName;

    /** 评语 */
    @Excel(name = "评语")
    private String comment;

    /** 贫困等级（一等、二等） */
    @Excel(name = "贫困等级", readConverterExp = "一=等、二等")
    private String hardRank;

    /** 助学金金额 */
    @Excel(name = "助学金金额")
    private BigDecimal money;

    //等级名称
    private String rankName;
    //学院名称
    private String collegeName;
    //专业名称
    private String majorName;
    //年级
    private String grade;
    //审核时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date checkTime;
    //是否公示
    private String isPublic;
    //操作类型   0学生申请   1学生申请记录查询 2 待审核 3 已审核 4 公示
    private String operation;

    public void setApplyId(Long applyId) 
    {
        this.applyId = applyId;
    }

    public Long getApplyId() 
    {
        return applyId;
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
    public void setApplyYear(String applyYear) 
    {
        this.applyYear = applyYear;
    }

    public String getApplyYear() 
    {
        return applyYear;
    }
    public void setFamilyNum(Long familyNum) 
    {
        this.familyNum = familyNum;
    }

    public Long getFamilyNum() 
    {
        return familyNum;
    }
    public void setAvgIncome(BigDecimal avgIncome) 
    {
        this.avgIncome = avgIncome;
    }

    public BigDecimal getAvgIncome() 
    {
        return avgIncome;
    }
    public void setHardCondition(String hardCondition) 
    {
        this.hardCondition = hardCondition;
    }

    public String getHardCondition() 
    {
        return hardCondition;
    }
    public void setImg(String img) 
    {
        this.img = img;
    }

    public String getImg() 
    {
        return img;
    }
    public void setProcess(String process) 
    {
        this.process = process;
    }

    public String getProcess() 
    {
        return process;
    }
    public void setApplyTime(Date applyTime) 
    {
        this.applyTime = applyTime;
    }

    public Date getApplyTime() 
    {
        return applyTime;
    }
    public void setInstructorName(String instructorName) 
    {
        this.instructorName = instructorName;
    }

    public String getInstructorName() 
    {
        return instructorName;
    }
    public void setComment(String comment) 
    {
        this.comment = comment;
    }

    public String getComment() 
    {
        return comment;
    }
    public void setHardRank(String hardRank) 
    {
        this.hardRank = hardRank;
    }

    public String getHardRank() 
    {
        return hardRank;
    }
    public void setMoney(BigDecimal money) 
    {
        this.money = money;
    }

    public BigDecimal getMoney() 
    {
        return money;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("applyId", getApplyId())
            .append("sno", getSno())
            .append("stuName", getStuName())
            .append("applyYear", getApplyYear())
            .append("familyNum", getFamilyNum())
            .append("avgIncome", getAvgIncome())
            .append("hardCondition", getHardCondition())
            .append("img", getImg())
            .append("process", getProcess())
            .append("applyTime", getApplyTime())
            .append("instructorName", getInstructorName())
            .append("comment", getComment())
            .append("hardRank", getHardRank())
            .append("money", getMoney())
            .toString();
    }
}
