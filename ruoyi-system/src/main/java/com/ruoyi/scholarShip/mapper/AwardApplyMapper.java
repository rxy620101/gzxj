package com.ruoyi.scholarShip.mapper;

import java.util.List;
import com.ruoyi.scholarShip.domain.AwardApply;

/**
 * 奖学金申请Mapper接口
 * 
 * @author raoxy
 * @date 2022-04-25
 */
public interface AwardApplyMapper 
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
     * 删除奖学金申请
     * 
     * @param applyId 奖学金申请主键
     * @return 结果
     */
    public int deleteAwardApplyByApplyId(Long applyId);

    /**
     * 批量删除奖学金申请
     * 
     * @param applyIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAwardApplyByApplyIds(Long[] applyIds);
}
