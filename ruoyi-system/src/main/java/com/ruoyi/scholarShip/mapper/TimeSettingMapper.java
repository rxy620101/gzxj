package com.ruoyi.scholarShip.mapper;

import java.util.List;
import com.ruoyi.scholarShip.domain.TimeSetting;

/**
 * 申请时间参数Mapper接口
 * 
 * @author raoxy
 * @date 2022-04-23
 */
public interface TimeSettingMapper 
{
    /**
     * 查询申请时间参数
     * 
     * @param settingId 申请时间参数主键
     * @return 申请时间参数
     */
    public TimeSetting selectTimeSettingBySetting(Long settingId);

    /**
     * 查询申请时间参数列表
     * 
     * @param timeSetting 申请时间参数
     * @return 申请时间参数集合
     */
    public List<TimeSetting> selectTimeSettingList(TimeSetting timeSetting);

    /**
     * 新增申请时间参数
     * 
     * @param timeSetting 申请时间参数
     * @return 结果
     */
    public int insertTimeSetting(TimeSetting timeSetting);

    /**
     * 修改申请时间参数
     * 
     * @param timeSetting 申请时间参数
     * @return 结果
     */
    public int updateTimeSetting(TimeSetting timeSetting);

    /**
     * 删除申请时间参数
     * 
     * @param settingId 申请时间参数主键
     * @return 结果
     */
    public int deleteTimeSettingBySetting(Long settingId);

    /**
     * 批量删除申请时间参数
     * 
     * @param settings 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTimeSettingBySettings(Long[] settings);

    //获取最新的时间
    public TimeSetting getAtLeast(String setType);
}
