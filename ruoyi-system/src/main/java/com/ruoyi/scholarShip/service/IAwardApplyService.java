package com.ruoyi.scholarShip.service;

import java.util.List;
import java.util.Map;

import com.ruoyi.scholarShip.domain.AwardApply;

/**
 * 奖学金申请Service接口
 * 
 * @author raoxy
 * @date 2022-04-25
 */
public interface IAwardApplyService 
{
    /**
     * 查询奖学金申请
     * 
     * @param applyId 奖学金申请主键
     * @return 奖学金申请
     */
    public AwardApply selectAwardApplyByApplyId(Long applyId);

    /**
     * 查询奖学金申请列表
     * 
     * @param awardApply 奖学金申请
     * @return 奖学金申请集合
     */
    public List<AwardApply> selectAwardApplyList(AwardApply awardApply);

    /**
     * 新增奖学金申请
     * 
     * @param awardApply 奖学金申请
     * @return 结果
     */
    public int insertAwardApply(AwardApply awardApply);

    /**
     * 修改奖学金申请
     * 
     * @param awardApply 奖学金申请
     * @return 结果
     */
    public int updateAwardApply(AwardApply awardApply);

    /**
     * 批量删除奖学金申请
     * 
     * @param applyIds 需要删除的奖学金申请主键集合
     * @return 结果
     */
    public int deleteAwardApplyByApplyIds(Long[] applyIds);

    /**
     * 删除奖学金申请信息
     * 
     * @param applyId 奖学金申请主键
     * @return 结果
     */
    public int deleteAwardApplyByApplyId(Long applyId);
    //审核
    public int checkApply(AwardApply awardApply);

    //发布
    public int publishApply(AwardApply awardApply);
    //统计申请人数
    public int selectTotalPeople(Map<String, Object> map);

    //查询是否重复申请
    public int checkIsApply(AwardApply awardApply);

}
