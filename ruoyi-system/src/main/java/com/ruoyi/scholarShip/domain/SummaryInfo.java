package com.ruoyi.scholarShip.domain;

import com.ruoyi.common.annotation.Excel;
import lombok.Data;

/**统计类**/

@Data
public class SummaryInfo {
    private static final long serialVersionUID = 1L;

    @Excel(name="学院名称",type = Excel.Type.EXPORT ,width = 60)
    private String collegeName;

    public String getCollegeName() {
        return collegeName;
    }

    @Excel(name="等级名称",type = Excel.Type.EXPORT,width = 40)
    private String rankName;

    @Excel(name="金额（元）",type = Excel.Type.EXPORT,width = 20)
    private Integer money;

    @Excel(name="申请人数",type = Excel.Type.EXPORT,isStatistics = true,width = 20)
    private Integer allPeople;

    @Excel(name="应发金额",type = Excel.Type.EXPORT ,isStatistics = true,width = 20)
    private Integer allMoney;


}
