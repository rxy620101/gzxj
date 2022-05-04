package com.ruoyi.scholarShip.service;

import java.util.List;
import com.ruoyi.scholarShip.domain.AwardSetting;

/**
 * 奖学金参数Service接口
 * 
 * @author raoxy
 * @date 2022-04-23
 */
public interface IAwardSettingService 
{
    /**
     * 查询奖学金参数
     * 
     * @param setting 奖学金参数主键
     * @return 奖学金参数
     */
    public AwardSetting selectAwardSettingBySetting(Long setting);

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
     * 批量删除奖学金参数
     * 
     * @param settings 需要删除的奖学金参数主键集合
     * @return 结果
     */
    public int deleteAwardSettingBySettings(Long[] settings);

    /**
     * 删除奖学金参数信息
     * 
     * @param setting 奖学金参数主键
     * @return 结果
     */
    public int deleteAwardSettingBySetting(Long setting);

    //根据名字检查是否存在该类别
    public int checkExitByName(String awardName);

    //根据parentId查询奖项类别
    public List<AwardSetting> selectByParentId(Long parentId);

    //查询具体奖项
    public List<AwardSetting> selectAllDetail(AwardSetting awardSetting);

}
