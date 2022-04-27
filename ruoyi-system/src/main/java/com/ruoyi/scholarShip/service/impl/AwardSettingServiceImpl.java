package com.ruoyi.scholarShip.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.scholarShip.mapper.AwardSettingMapper;
import com.ruoyi.scholarShip.domain.AwardSetting;
import com.ruoyi.scholarShip.service.IAwardSettingService;

/**
 * 奖学金参数Service业务层处理
 * 
 * @author raoxy
 * @date 2022-04-23
 */
@Service
public class AwardSettingServiceImpl implements IAwardSettingService 
{
    @Autowired
    private AwardSettingMapper awardSettingMapper;

    /**
     * 查询奖学金参数
     * 
     * @param setting 奖学金参数主键
     * @return 奖学金参数
     */
    @Override
    public AwardSetting selectAwardSettingBySetting(Long setting)
    {
        return awardSettingMapper.selectAwardSettingBySetting(setting);
    }

    /**
     * 查询奖学金参数列表
     * 
     * @param awardSetting 奖学金参数
     * @return 奖学金参数
     */
    @Override
    public List<AwardSetting> selectAwardSettingList(AwardSetting awardSetting)
    {
        return awardSettingMapper.selectAwardSettingList(awardSetting);
    }

    /**
     * 新增奖学金参数
     * 
     * @param awardSetting 奖学金参数
     * @return 结果
     */
    @Override
    public int insertAwardSetting(AwardSetting awardSetting)
    {
        awardSetting.setCreateTime(DateUtils.getNowDate());
        return awardSettingMapper.insertAwardSetting(awardSetting);
    }

    /**
     * 修改奖学金参数
     * 
     * @param awardSetting 奖学金参数
     * @return 结果
     */
    @Override
    public int updateAwardSetting(AwardSetting awardSetting)
    {
        awardSetting.setUpdateTime(DateUtils.getNowDate());
        return awardSettingMapper.updateAwardSetting(awardSetting);
    }

    /**
     * 批量删除奖学金参数
     * 
     * @param settings 需要删除的奖学金参数主键
     * @return 结果
     */
    @Override
    public int deleteAwardSettingBySettings(Long[] settings)
    {
        return awardSettingMapper.deleteAwardSettingBySettings(settings);
    }

    /**
     * 删除奖学金参数信息
     * 
     * @param setting 奖学金参数主键
     * @return 结果
     */
    @Override
    public int deleteAwardSettingBySetting(Long setting)
    {
        return awardSettingMapper.deleteAwardSettingBySetting(setting);
    }
    //根据名字检查是否存在该类别
    @Override
    public int checkExitByName(String awardName){
        return awardSettingMapper.checkExitByName(awardName);
    }

    //根据parentId查询奖项类别
    @Override
    public List<AwardSetting> selectByParentId(Long parentId){
        return awardSettingMapper.selectByParentId(parentId);
    }

}
