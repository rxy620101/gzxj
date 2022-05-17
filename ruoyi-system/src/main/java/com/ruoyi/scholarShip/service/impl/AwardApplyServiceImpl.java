package com.ruoyi.scholarShip.service.impl;

import java.util.List;
import java.util.Map;

import com.ruoyi.scholarShip.domain.HardApply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.scholarShip.mapper.AwardApplyMapper;
import com.ruoyi.scholarShip.domain.AwardApply;
import com.ruoyi.scholarShip.service.IAwardApplyService;

/**
 * 奖学金申请Service业务层处理
 * 
 * @author raoxy
 * @date 2022-04-25
 */
@Service
public class AwardApplyServiceImpl implements IAwardApplyService 
{
    @Autowired
    private AwardApplyMapper awardApplyMapper;

    /**
     * 查询奖学金申请
     * 
     * @param applyId 奖学金申请主键
     * @return 奖学金申请
     */
    @Override
    public AwardApply selectAwardApplyByApplyId(Long applyId)
    {
        return awardApplyMapper.selectAwardApplyByApplyId(applyId);
    }

    /**
     * 查询奖学金申请列表
     * 
     * @param awardApply 奖学金申请
     * @return 奖学金申请
     */
    @Override
    public List<AwardApply> selectAwardApplyList(AwardApply awardApply)
    {
        return awardApplyMapper.selectAwardApplyList(awardApply);
    }

    /**
     * 新增奖学金申请
     * 
     * @param awardApply 奖学金申请
     * @return 结果
     */
    @Override
    public int insertAwardApply(AwardApply awardApply)
    {
        return awardApplyMapper.insertAwardApply(awardApply);
    }

    /**
     * 修改奖学金申请
     * 
     * @param awardApply 奖学金申请
     * @return 结果
     */
    @Override
    public int updateAwardApply(AwardApply awardApply)
    {
        return awardApplyMapper.updateAwardApply(awardApply);
    }

    /**
     * 批量删除奖学金申请
     * 
     * @param applyIds 需要删除的奖学金申请主键
     * @return 结果
     */
    @Override
    public int deleteAwardApplyByApplyIds(Long[] applyIds)
    {
        return awardApplyMapper.deleteAwardApplyByApplyIds(applyIds);
    }

    /**
     * 删除奖学金申请信息
     * 
     * @param applyId 奖学金申请主键
     * @return 结果
     */
    @Override
    public int deleteAwardApplyByApplyId(Long applyId)
    {
        return awardApplyMapper.deleteAwardApplyByApplyId(applyId);
    }
    //审核
    @Override
    public int checkApply(AwardApply awardApply){
        return  awardApplyMapper.checkApply(awardApply);
    }

    //发布
    @Override
    public int publishApply(AwardApply awardApply){
        return awardApplyMapper.publishApply(awardApply);
    }
    //统计申请人数
    @Override
    public int selectTotalPeople(Map<String, Object> map){
        return  awardApplyMapper.selectTotalPeople(map);
    }

    //查询是否重复申请
    @Override
    public int checkIsApply(AwardApply awardApply){
        return  awardApplyMapper.checkIsApply(awardApply);
    }
}
