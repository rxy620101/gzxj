package com.ruoyi.scholarShip.service;

import java.util.List;
import com.ruoyi.scholarShip.domain.TimeSetting;

/**
 * 申请时间参数Service接口
 * 
 * @author raoxy
 * @date 2022-04-23
 */
public interface ITimeSettingService 
{
    /**
     * 查询申请时间参数
     * 
     * @param setting 申请时间参数主键
     * @return 申请时间参数
     */
    public TimeSetting selectTimeSettingBySetting(Long setting);

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
     * 批量删除申请时间参数
     * 
     * @param settings 需要删除的申请时间参数主键集合
     * @return 结果
     */
    public int deleteTimeSettingBySettings(Long[] settings);

    /**
     * 删除申请时间参数信息
     * 
     * @param setting 申请时间参数主键
     * @return 结果
     */
    public int deleteTimeSettingBySetting(Long setting);
}
