package com.ruoyi.scholarShip.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

@Data
public class HardRank extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 等级id */
    private Long rangkId;

    /** 等级名称 */
    private String rankName;

    /** 金额 */
    private BigDecimal money;

    public void setRangkId(Long rangkId)
    {
        this.rangkId = rangkId;
    }

    public Long getRangkId()
    {
        return rangkId;
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
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("rangkId", getRangkId())
                .append("rankName", getRankName())
                .append("money", getMoney())
                .append("createTime", getCreateTime())
                .append("updateTime", getUpdateTime())
                .toString();
    }


}
