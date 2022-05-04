package com.ruoyi.scholarShip.service;

import java.util.List;
import java.util.Map;

import com.ruoyi.scholarShip.domain.HardApply;

/**
 * 助学金申请Service接口
 * 
 * @author raoxy
 * @date 2022-04-29
 */
public interface IHardApplyService 
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
     * 批量删除助学金申请
     * 
     * @param applyIds 需要删除的助学金申请主键集合
     * @return 结果
     */
    public int deleteHardApplyByApplyIds(Long[] applyIds);

    /**
     * 删除助学金申请信息
     * 
     * @param applyId 助学金申请主键
     * @return 结果
     */
    public int deleteHardApplyByApplyId(Long applyId);

    //审核
    public int checkApply(HardApply hardApply);

    //发布
    public int publishApply(HardApply hardApply);

    //统计学院的每个等级的人数
    public int selectTotalPeople(Map<String,Object> map);
}
