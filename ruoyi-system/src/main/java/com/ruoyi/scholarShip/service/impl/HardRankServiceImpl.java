package com.ruoyi.scholarShip.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.scholarShip.mapper.HardRankMapper;
import com.ruoyi.scholarShip.domain.HardRank;
import com.ruoyi.scholarShip.service.IHardRankService;

/**
 * 贫困等级Service业务层处理
 * 
 * @author raoxy
 * @date 2022-05-02
 */
@Service
public class HardRankServiceImpl implements IHardRankService 
{
    @Autowired
    private HardRankMapper hardRankMapper;

    /**
     * 查询贫困等级
     * 
     * @param rangkId 贫困等级主键
     * @return 贫困等级
     */
    @Override
    public HardRank selectHardRankByRangkId(Long rangkId)
    {
        return hardRankMapper.selectHardRankByRangkId(rangkId);
    }

    /**
     * 查询贫困等级列表
     * 
     * @param hardRank 贫困等级
     * @return 贫困等级
     */
    @Override
    public List<HardRank> selectHardRankList()
    {
        return hardRankMapper.selectHardRankList();
    }

    /**
     * 新增贫困等级
     * 
     * @param hardRank 贫困等级
     * @return 结果
     */
    @Override
    public int insertHardRank(HardRank hardRank)
    {
        hardRank.setCreateTime(DateUtils.getNowDate());
        return hardRankMapper.insertHardRank(hardRank);
    }

    /**
     * 修改贫困等级
     * 
     * @param hardRank 贫困等级
     * @return 结果
     */
    @Override
    public int updateHardRank(HardRank hardRank)
    {
        hardRank.setUpdateTime(DateUtils.getNowDate());
        return hardRankMapper.updateHardRank(hardRank);
    }

    /**
     * 批量删除贫困等级
     * 
     * @param rangkIds 需要删除的贫困等级主键
     * @return 结果
     */
    @Override
    public int deleteHardRankByRangkIds(Long[] rangkIds)
    {
        return hardRankMapper.deleteHardRankByRangkIds(rangkIds);
    }

    /**
     * 删除贫困等级信息
     * 
     * @param rangkId 贫困等级主键
     * @return 结果
     */
    @Override
    public int deleteHardRankByRangkId(Long rangkId)
    {
        return hardRankMapper.deleteHardRankByRangkId(rangkId);
    }
}
