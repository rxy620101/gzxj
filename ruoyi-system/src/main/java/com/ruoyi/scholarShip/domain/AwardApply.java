package com.ruoyi.scholarShip.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import java.util.Date;

/**
 * 奖学金申请对象 award_apply
 *
 * @author raoxy
 * @date 2022-04-29
 */
@Data
public class AwardApply extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 申请id
     */
    private Long applyId;

    /**
     * 学号
     */
    @Excel(name = "学号")
    private String sno;

    /**
     * 学生姓名
     */
    @Excel(name = "学生姓名")
    private String stuName;

    /**
     * 申请学年
     */
    @Excel(name = "申请学年")
    private String applyYear;

    /**
     * 申请学期
     */
    @Excel(name = "申请学期")
    private String applyTerm;

    /**
     * 申请的奖项大类
     */
    @Excel(name = "申请的奖项大类")
    private String applyType;

    /**
     * 初始等级(奖项金的具体名称)
     */
    @Excel(name = "初始等级(奖项金的具体名称)")
    private String initRank;

    /**
     * 自我评价
     */
    @Excel(name = "自我评价")
    private String selfRemark;

    /**
     * 进度(0 待审核，1审核通过，2审核失败）
     */
    @Excel(name = "进度(0 待审核，1审核通过，2审核失败）")
    private String process;

    /**
     * 辅导员姓名
     */
    @Excel(name = "辅导员姓名")
    private String instructorName;

    /**
     * 是否调整等级（0 否，1是）
     */
    @Excel(name = "是否调整等级", readConverterExp = "0=,否=，1是")
    private String isAdapt;

    /**
     * 调整等级
     */
    @Excel(name = "调整等级")
    private String adaptRank;

    /**
     * 评语
     */
    @Excel(name = "评语")
    private String comment;

    /**
     * 卡号
     */
    @Excel(name = "卡号")
    private String cardNum;
    //奖项类别名称
    private String typeName;
    //奖项等级名称
    private String rankName;
    //初始等级金额
    private int initMoney;
    //调整等级金额
    private int adaptMoney;
    //审核时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date checkTime;
    //是否公示
    private String isPublic;
    //操作类型   0学生申请   1学生申请记录查询 2 待审核 3 已审核 4 公示
    private String operation;
    //学院名称
    private String collegeName;
    //专业名称
    private String majorName;
    //年级
    private String grade;
    //审核结果
    private String checkResult;
    //调整类型
    private String adaptType;
    //调整等级名称
    private String adaptName;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getRankName() {
        return rankName;
    }

    public void setRankName(String rankName) {
        this.rankName = rankName;
    }

    public void setApplyId(Long applyId) {
        this.applyId = applyId;
    }
    public Long getApplyId() {
        return applyId;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public String getSno() {
        return sno;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    public String getStuName() {
        return stuName;
    }

    public void setApplyYear(String applyYear) {
        this.applyYear = applyYear;
    }

    public String getApplyYear() {
        return applyYear;
    }

    public void setApplyTerm(String applyTerm) {
        this.applyTerm = applyTerm;
    }

    public String getApplyTerm() {
        return applyTerm;
    }

    public void setApplyType(String applyType) {
        this.applyType = applyType;
    }

    public String getApplyType() {
        return applyType;
    }

    public void setInitRank(String initRank) {
        this.initRank = initRank;
    }

    public String getInitRank() {
        return initRank;
    }

    public void setSelfRemark(String selfRemark) {
        this.selfRemark = selfRemark;
    }

    public String getSelfRemark() {
        return selfRemark;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getProcess() {
        return process;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public void setIsAdapt(String isAdapt) {
        this.isAdapt = isAdapt;
    }

    public String getIsAdapt() {
        return isAdapt;
    }

    public void setAdaptRank(String adaptRank) {
        this.adaptRank = adaptRank;
    }

    public String getAdaptRank() {
        return adaptRank;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public String getCardNum() {
        return cardNum;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("applyId", getApplyId())
                .append("sno", getSno())
                .append("stuName", getStuName())
                .append("applyYear", getApplyYear())
                .append("applyTerm", getApplyTerm())
                .append("applyType", getApplyType())
                .append("initRank", getInitRank())
                .append("selfRemark", getSelfRemark())
                .append("process", getProcess())
                .append("instructorName", getInstructorName())
                .append("isAdapt", getIsAdapt())
                .append("adaptRank", getAdaptRank())
                .append("comment", getComment())
                .append("createTime", getCreateTime())
                .append("cardNum", getCardNum())
                .toString();
    }
}
