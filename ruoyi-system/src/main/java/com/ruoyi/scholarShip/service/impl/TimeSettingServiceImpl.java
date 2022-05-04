package com.ruoyi.scholarShip.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.scholarShip.mapper.TimeSettingMapper;
import com.ruoyi.scholarShip.domain.TimeSetting;
import com.ruoyi.scholarShip.service.ITimeSettingService;

/**
 * 申请时间参数Service业务层处理
 * 
 * @author raoxy
 * @date 2022-04-23
 */
@Service
public class TimeSettingServiceImpl implements ITimeSettingService 
{
    @Autowired
    private TimeSettingMapper timeSettingMapper;

    /**
     * 查询申请时间参数
     * 
     * @param setting 申请时间参数主键
     * @return 申请时间参数
     */
    @Override
    public TimeSetting selectTimeSettingBySetting(Long setting)
    {
        return timeSettingMapper.selectTimeSettingBySetting(setting);
    }

    /**
     * 查询申请时间参数列表
     * 
     * @param timeSetting 申请时间参数
     * @return 申请时间参数
     */
    @Override
    public List<TimeSetting> selectTimeSettingList(TimeSetting timeSetting)
    {
        return timeSettingMapper.selectTimeSettingList(timeSetting);
    }

    /**
     * 新增申请时间参数
     * 
     * @param timeSetting 申请时间参数
     * @return 结果
     */
    @Override
    public int insertTimeSetting(TimeSetting timeSetting)
    {
        timeSetting.setCreateTime(DateUtils.getNowDate());
        return timeSettingMapper.insertTimeSetting(timeSetting);
    }

    /**
     * 修改申请时间参数
     * 
     * @param timeSetting 申请时间参数
     * @return 结果
     */
    @Override
    public int updateTimeSetting(TimeSetting timeSetting)
    {
        timeSetting.setUpdateTime(DateUtils.getNowDate());
        return timeSettingMapper.updateTimeSetting(timeSetting);
    }

    /**
     * 批量删除申请时间参数
     * 
     * @param settings 需要删除的申请时间参数主键
     * @return 结果
     */
    @Override
    public int deleteTimeSettingBySettings(Long[] settings)
    {
        return timeSettingMapper.deleteTimeSettingBySettings(settings);
    }

    /**
     * 删除申请时间参数信息
     * 
     * @param setting 申请时间参数主键
     * @return 结果
     */
    @Override
    public int deleteTimeSettingBySetting(Long setting)
    {
        return timeSettingMapper.deleteTimeSettingBySetting(setting);
    }

    //获取最新的时间
    @Override
    public TimeSetting getAtLeast(String setType){
        return  timeSettingMapper.getAtLeast(setType);
    }
}
