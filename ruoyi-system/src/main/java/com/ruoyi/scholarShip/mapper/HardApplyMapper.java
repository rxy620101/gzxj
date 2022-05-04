package com.ruoyi.scholarShip.mapper;

import java.util.List;
import java.util.Map;

import com.ruoyi.scholarShip.domain.AwardApply;
import com.ruoyi.scholarShip.domain.HardApply;

/**
 * 助学金申请Mapper接口
 * 
 * @author raoxy
 * @date 2022-04-29
 */
public interface HardApplyMapper 
{
    /**
     * 查询助学金申请
     * 
     * @param applyId 助学金申请主键
     * @return 助学金申请
     */
    public HardApply selectHardApplyByApplyId(Long applyId);

    /**
     * 查询助学金申请列表
     * 
     * @param hardApply 助学金申请
     * @return 助学金申请集合
     */
    public List<HardApply> selectHardApplyList(HardApply hardApply);

    /**
     * 新增助学金申请
     * 
     * @param hardApply 助学金申请
     * @return 结果
     */
    public int insertHardApply(HardApply hardApply);

    /**
     * 修改助学金申请
     * 
     * @param hardApply 助学金申请
     * @return 结果
     */
    public int updateHardApply(HardApply hardApply);

    /**
     * 删除助学金申请
     * 
     * @param applyId 助学金申请主键
     * @return 结果
     */
    public int deleteHardApplyByApplyId(Long applyId);

    /**
     * 批量删除助学金申请
     * 
     * @param applyIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteHardApplyByApplyIds(Long[] applyIds);

    //审核
    public int checkApply(HardApply hardApply);

    //发布
    public int publishApply(HardApply hardApply);

    //统计申请人数
    public int selectTotalPeople(Map<String, Object> map);
}
