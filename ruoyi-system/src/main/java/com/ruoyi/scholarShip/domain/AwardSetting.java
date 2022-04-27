package com.ruoyi.scholarShip.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 奖学金参数对象 award_setting
 *
 * @author raoxy
 * @date 2022-04-23
 */
public class AwardSetting extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 参数id
     */
    private Long settingId;

    /**
     * 大类id
     */
    @Excel(name = "大类id")
    private Long parentId;

    /**
     * 祖级列表
     */
    @Excel(name = "祖级列表")
    private String ancestors;

    /**
     * 奖项类别或名称
     */
    @Excel(name = "奖项类别或名称")
    private String awardNames;

    /**
     * 设置的学期
     */
    @Excel(name = "设置的学期")
    private String setTerm;

    /**
     * 设置的学年
     */
    @Excel(name = "设置的学年")
    private String setYear;

    /**
     * 奖项的占比(%)
     */
    private String ratio;

    /**
     * 显示顺序
     */
    private Integer orderNum;

    /**
     * 状态（0正常 1停用）
     */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;

    //名额
    private String num;

    //金额
    private int money;

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public Long getSettingId() {
        return settingId;
    }

    public void setSettingId(Long settingId) {
        this.settingId = settingId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setAncestors(String ancestors) {
        this.ancestors = ancestors;
    }

    public String getAncestors() {
        return ancestors;
    }

    public void setAwardNames(String awardNames) {
        this.awardNames = awardNames;
    }

    public String getAwardNames() {
        return awardNames;
    }

    public String getSetTerm() {
        return setTerm;
    }

    public void setSetTerm(String setTerm) {
        this.setTerm = setTerm;
    }

    public void setSetYear(String setYear) {
        this.setYear = setYear;
    }

    public String getSetYear() {
        return setYear;
    }

    public void setRatio(String ratio) {
        this.ratio = ratio;
    }

    public String getRatio() {
        return ratio;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public Integer getOrderNum() {
        return orderNum;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("setting", getSettingId())
                .append("parentId", getParentId())
                .append("ancestors", getAncestors())
                .append("awardNames", getAwardNames())
                .append("setterm", getSetTerm())
                .append("setYear", getSetYear())
                .append("ratio", getRatio())
                .append("num", getNum())
                .append("orderNum", getOrderNum())
                .append("status", getStatus())
                .append("delFlag", getDelFlag())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
