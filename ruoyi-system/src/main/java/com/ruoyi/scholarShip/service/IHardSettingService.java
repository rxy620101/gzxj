package com.ruoyi.scholarShip.service;

import java.util.List;
import com.ruoyi.scholarShip.domain.HardSetting;

/**
 * 助学金参数设置Service接口
 * 
 * @author raoxy
 * @date 2022-04-23
 */
public interface IHardSettingService 
{
    /**
     * 查询助学金参数设置
     * 
     * @param setting 助学金参数设置主键
     * @return 助学金参数设置
     */
    public HardSetting selectHardSettingBySetting(Long setting);

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
     * 批量删除助学金参数设置
     * 
     * @param settings 需要删除的助学金参数设置主键集合
     * @return 结果
     */
    public int deleteHardSettingBySettings(Long[] settings);

    /**
     * 删除助学金参数设置信息
     * 
     * @param setting 助学金参数设置主键
     * @return 结果
     */
    public int deleteHardSettingBySetting(Long setting);

    //根据辅导员工号查询
    public HardSetting getByInstrucotId(Long instructorId);
}
