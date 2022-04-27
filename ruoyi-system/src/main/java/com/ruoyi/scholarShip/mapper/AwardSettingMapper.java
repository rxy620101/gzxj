package com.ruoyi.scholarShip.mapper;

import java.util.List;
import com.ruoyi.scholarShip.domain.AwardSetting;

/**
 * 奖学金参数Mapper接口
 * 
 * @author raoxy
 * @date 2022-04-23
 */
public interface AwardSettingMapper 
{
    /**
     * 查询奖学金参数
     * 
     * @param settingId 奖学金参数主键
     * @return 奖学金参数
     */
    public AwardSetting selectAwardSettingBySetting(Long settingId);

    /**
     * 查询奖学金参数列表
     * 
     * @param awardSetting 奖学金参数
     * @return 奖学金参数集合
     */
    public List<AwardSetting> selectAwardSettingList(AwardSetting awardSetting);

    /**
     * 新增奖学金参数
     * 
     * @param awardSetting 奖学金参数
     * @return 结果
     */
    public int insertAwardSetting(AwardSetting awardSetting);

    /**
     * 修改奖学金参数
     * 
     * @param awardSetting 奖学金参数
     * @return 结果
     */
    public int updateAwardSetting(AwardSetting awardSetting);

    /**
     * 删除奖学金参数
     * 
     * @param settingId 奖学金参数主键
     * @return 结果
     */
    public int deleteAwardSettingBySetting(Long settingId);

    /**
     * 批量删除奖学金参数
     * 
     * @param settings 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAwardSettingBySettings(Long[] settings);

    //根据名字检查是否存在该类别
    public int checkExitByName(String awardNames);

    //根据parentId查询奖项类别
    public List<AwardSetting> selectByParentId(Long parentId);

}
