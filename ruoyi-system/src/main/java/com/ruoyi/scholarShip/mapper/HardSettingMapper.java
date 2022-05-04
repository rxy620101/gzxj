package com.ruoyi.scholarShip.mapper;

import java.util.List;
import com.ruoyi.scholarShip.domain.HardSetting;

/**
 * 助学金参数设置Mapper接口
 * 
 * @author raoxy
 * @date 2022-04-23
 */
public interface HardSettingMapper 
{
    /**
     * 查询助学金参数设置
     * 
     * @param settingId 助学金参数设置主键
     * @return 助学金参数设置
     */
    public HardSetting selectHardSettingBySetting(Long settingId);

    /**
     * 查询助学金参数设置列表
     * 
     * @param hardSetting 助学金参数设置
     * @return 助学金参数设置集合
     */
    public List<HardSetting> selectHardSettingList(HardSetting hardSetting);

    /**
     * 新增助学金参数设置
     * 
     * @param hardSetting 助学金参数设置
     * @return 结果
     */
    public int insertHardSetting(HardSetting hardSetting);

    /**
     * 新增助学金参数设置
     *
     * @param hardSettings 助学金参数设置
     * @return 结果
     */
    public int batchAdd(List<HardSetting> hardSettings);

    /**
     * 修改助学金参数设置
     * 
     * @param hardSetting 助学金参数设置
     * @return 结果
     */
    public int updateHardSetting(HardSetting hardSetting);

    /**
     * 删除助学金参数设置
     * 
     * @param settingId 助学金参数设置主键
     * @return 结果
     */
    public int deleteHardSettingBySetting(Long settingId);

    /**
     * 批量删除助学金参数设置
     * 
     * @param settings 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteHardSettingBySettings(Long[] settings);

//根据辅导员工号查询
    public HardSetting getByInstrucotId(Long instructorId);
}
