package com.ruoyi.scholarShip.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.scholarShip.mapper.HardSettingMapper;
import com.ruoyi.scholarShip.domain.HardSetting;
import com.ruoyi.scholarShip.service.IHardSettingService;

/**
 * 助学金参数设置Service业务层处理
 * 
 * @author raoxy
 * @date 2022-04-23
 */
@Service
public class HardSettingServiceImpl implements IHardSettingService 
{
    @Autowired
    private HardSettingMapper hardSettingMapper;

    /**
     * 查询助学金参数设置
     * 
     * @param setting 助学金参数设置主键
     * @return 助学金参数设置
     */
    @Override
    public HardSetting selectHardSettingBySetting(Long setting)
    {
        return hardSettingMapper.selectHardSettingBySetting(setting);
    }

    /**
     * 查询助学金参数设置列表
     * 
     * @param hardSetting 助学金参数设置
     * @return 助学金参数设置
     */
    @Override
    public List<HardSetting> selectHardSettingList(HardSetting hardSetting)
    {
        return hardSettingMapper.selectHardSettingList(hardSetting);
    }

    /**
     * 新增助学金参数设置
     * 
     * @param hardSetting 助学金参数设置
     * @return 结果
     */
    @Override
    public int insertHardSetting(HardSetting hardSetting)
    {
        hardSetting.setCreateTime(DateUtils.getNowDate());
        return hardSettingMapper.insertHardSetting(hardSetting);
    }
    /**
     * 新增助学金参数设置
     *
     * @param hardSettings 助学金参数设置
     * @return 结果
     */
    @Override
    public int batchAdd(List<HardSetting> hardSettings){
        return hardSettingMapper.batchAdd(hardSettings);
    }

    /**
     * 修改助学金参数设置
     * 
     * @param hardSetting 助学金参数设置
     * @return 结果
     */
    @Override
    public int updateHardSetting(HardSetting hardSetting)
    {
        hardSetting.setUpdateTime(DateUtils.getNowDate());
        return hardSettingMapper.updateHardSetting(hardSetting);
    }

    /**
     * 批量删除助学金参数设置
     * 
     * @param settings 需要删除的助学金参数设置主键
     * @return 结果
     */
    @Override
    public int deleteHardSettingBySettings(Long[] settings)
    {
        return hardSettingMapper.deleteHardSettingBySettings(settings);
    }

    /**
     * 删除助学金参数设置信息
     * 
     * @param setting 助学金参数设置主键
     * @return 结果
     */
    @Override
    public int deleteHardSettingBySetting(Long setting)
    {
        return hardSettingMapper.deleteHardSettingBySetting(setting);
    }
}
