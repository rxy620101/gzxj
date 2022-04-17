package com.ruoyi.common.core.domain.entity;

import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * 院系-专业对象 college_major
 *
 * @author raoxy
 * @date 2022-04-04
 */
public class SysColleageMajor extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 部门id */
    private Long collegeId;

    /** 父d */
    private Long parentId;

    /** 祖级列表 */
    private String ancestors;

    /** 名称 */
    private String name;

    /** 显示顺序 */
    private Integer orderNum;

    /** 部门状态（0正常 1停用） */
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    /** 父部门名称 */
    private String parentName;

    /** 子部门 */
    private List<SysColleageMajor> children = new ArrayList<SysColleageMajor>();

    public void setcollegeId(Long collegeId)
    {
        this.collegeId = collegeId;
    }

    public Long getcollegeId()
    {
        return collegeId;
    }
    public void setParentId(Long parentId)
    {
        this.parentId = parentId;
    }

    public Long getParentId()
    {
        return parentId;
    }
    public void setAncestors(String ancestors)
    {
        this.ancestors = ancestors;
    }

    public String getAncestors()
    {
        return ancestors;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    @NotBlank(message = "名称不能为空")
    @Size(min = 1, max = 30, message = "名称长度不能超过30个字符")
    public String getName()
    {
        return name;
    }
    public void setOrderNum(Integer orderNum)
    {
        this.orderNum = orderNum;
    }
    @NotNull(message = "显示顺序不能为空")
    public Integer getOrderNum()
    {
        return orderNum;
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
    public String getParentName()
    {
        return parentName;
    }

    public void setParentName(String parentName)
    {
        this.parentName = parentName;
    }

    public List<SysColleageMajor> getChildren()
    {
        return children;
    }

    public void setChildren(List<SysColleageMajor> children)
    {
        this.children = children;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("collegeId", getcollegeId())
                .append("parentId", getParentId())
                .append("ancestors", getAncestors())
                .append("name", getName())
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
