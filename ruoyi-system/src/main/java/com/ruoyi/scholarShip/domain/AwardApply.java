package com.ruoyi.scholarShip.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 奖学金申请对象 award_apply
 * 
 * @author raoxy
 * @date 2022-04-25
 */
public class AwardApply extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 申请id */
    private Long applyId;

    /** 学号 */
    @Excel(name = "学号")
    private String sno;

    /** 学生姓名 */
    @Excel(name = "学生姓名")
    private String stuName;

    /** 申请学年 */
    @Excel(name = "申请学年")
    private String applyYear;

    /** 申请学期 */
    @Excel(name = "申请学期")
    private String applyTerm;

    /** 申请的奖项大类 */
    @Excel(name = "申请的奖项大类")
    private String applyType;


    /** 初始等级 */
    @Excel(name = "初始等级")
    private String initRank;

    /** 自我评价 */
    @Excel(name = "自我评价")
    private String selfRemark;

    /** 最终等级 */
    private String finalRank;

    /** 学科学分成绩 */
    @Excel(name = "学科学分成绩")
    private BigDecimal allCredit;

    /** 加分项分数总和 */
    @Excel(name = "加分项分数总和")
    private BigDecimal extraPoint;

    /** 最终学分绩点 */
    @Excel(name = "最终学分绩点")
    private BigDecimal finalCredit;

    /** 是否申请（0是，1否） */
    @Excel(name = "是否申请", readConverterExp = "0=是，1否")
    private String isApply;

    /** 进度(0 待审核，1审核通过，2审核失败） */
    @Excel(name = "进度(0 待审核，1审核通过，2审核失败）")
    private String process;

    /** 辅导员姓名 */
    @Excel(name = "辅导员姓名")
    private String instructorName;

    /** 评语 */
    private String comment;
    //奖项类别名称
    private String  awardName;
    //奖项等级名称
    private String  rankName;
   //卡号
    private String cardNum;

    public String getAwardName() {
        return awardName;
    }

    public void setAwardName(String awardName) {
        this.awardName = awardName;
    }

    public String getRankName() {
        return rankName;
    }

    public void setRankName(String rankName) {
        this.rankName = rankName;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

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
    public void setApplyTerm(String applyTerm) 
    {
        this.applyTerm = applyTerm;
    }

    public String getApplyTerm() 
    {
        return applyTerm;
    }
    public void setApplyType(String applyType) 
    {
        this.applyType = applyType;
    }

    public String getApplyType() 
    {
        return applyType;
    }
    public void setInitRank(String initRank) 
    {
        this.initRank = initRank;
    }

    public String getInitRank() 
    {
        return initRank;
    }
    public void setSelfRemark(String selfRemark) 
    {
        this.selfRemark = selfRemark;
    }

    public String getSelfRemark() 
    {
        return selfRemark;
    }
    public void setFinalRank(String finalRank) 
    {
        this.finalRank = finalRank;
    }

    public String getFinalRank() 
    {
        return finalRank;
    }
    public void setAllCredit(BigDecimal allCredit) 
    {
        this.allCredit = allCredit;
    }

    public BigDecimal getAllCredit() 
    {
        return allCredit;
    }
    public void setExtraPoint(BigDecimal extraPoint) 
    {
        this.extraPoint = extraPoint;
    }

    public BigDecimal getExtraPoint() 
    {
        return extraPoint;
    }
    public void setFinalCredit(BigDecimal finalCredit) 
    {
        this.finalCredit = finalCredit;
    }

    public BigDecimal getFinalCredit() 
    {
        return finalCredit;
    }
    public void setIsApply(String isApply) 
    {
        this.isApply = isApply;
    }

    public String getIsApply() 
    {
        return isApply;
    }
    public void setProcess(String process) 
    {
        this.process = process;
    }

    public String getProcess() 
    {
        return process;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("applyId", getApplyId())
            .append("sno", getSno())
            .append("stuName", getStuName())
            .append("applyYear", getApplyYear())
            .append("applyTerm", getApplyTerm())
            .append("applyType", getApplyType())
            .append("initRank", getInitRank())
            .append("selfRemark", getSelfRemark())
            .append("finalRank", getFinalRank())
            .append("allCredit", getAllCredit())
            .append("extraPoint", getExtraPoint())
            .append("finalCredit", getFinalCredit())
            .append("isApply", getIsApply())
            .append("process", getProcess())
            .append("instructorName", getInstructorName())
            .append("comment", getComment())
            .toString();
    }
}
