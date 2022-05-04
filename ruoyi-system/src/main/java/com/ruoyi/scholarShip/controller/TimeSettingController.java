package com.ruoyi.scholarShip.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.scholarShip.domain.TimeSetting;
import com.ruoyi.scholarShip.service.ITimeSettingService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 申请时间参数Controller
 * 
 * @author raoxy
 * @date 2022-04-23
 */
@RestController
@RequestMapping("/timeSetting/info")
public class TimeSettingController extends BaseController
{
    @Autowired
    private ITimeSettingService timeSettingService;

    /**
     * 查询申请时间参数列表
     */
    @PreAuthorize("@ss.hasPermi('timeSetting:info:list')")
    @GetMapping("/list")
    public TableDataInfo list(TimeSetting timeSetting)
    {
        startPage();
        List<TimeSetting> list = timeSettingService.selectTimeSettingList(timeSetting);
        return getDataTable(list);
    }

    /**
     * 导出申请时间参数列表
     */
    @PreAuthorize("@ss.hasPermi('timeSetting:info:export')")
    @Log(title = "申请时间参数", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TimeSetting timeSetting)
    {
        List<TimeSetting> list = timeSettingService.selectTimeSettingList(timeSetting);
        ExcelUtil<TimeSetting> util = new ExcelUtil<TimeSetting>(TimeSetting.class);
        util.exportExcel(response, list, "申请时间参数数据");
    }

    /**
     * 获取申请时间参数详细信息
     */
    @PreAuthorize("@ss.hasPermi('timeSetting:info:query')")
    @GetMapping(value = "/{settingId}")
    public AjaxResult getInfo(@PathVariable("settingId") Long settingId)
    {
        return AjaxResult.success(timeSettingService.selectTimeSettingBySetting(settingId));
    }

    /**
     * 新增申请时间参数
     */
    @PreAuthorize("@ss.hasPermi('timeSetting:info:add')")
    @Log(title = "申请时间参数", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TimeSetting timeSetting)
    {     timeSetting.setCreateBy(getUsername());
        return toAjax(timeSettingService.insertTimeSetting(timeSetting));
    }

    /**
     * 修改申请时间参数
     */
    @PreAuthorize("@ss.hasPermi('scholarShip:setting:edit')")
    @Log(title = "申请时间参数", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TimeSetting timeSetting)
    {
        return toAjax(timeSettingService.updateTimeSetting(timeSetting));
    }

    /**
     * 删除申请时间参数
     */
    @PreAuthorize("@ss.hasPermi('timeSetting:info:remove')")
    @Log(title = "申请时间参数", businessType = BusinessType.DELETE)
	@DeleteMapping("/{settingsId}")
    public AjaxResult remove(@PathVariable Long[] settingsId)
    {
        return toAjax(timeSettingService.deleteTimeSettingBySettings(settingsId));
    }

    //获取最新的时间参数设置
    @GetMapping("/getAtLeast/{setType}")
    public AjaxResult getAtLeast(@PathVariable("setType") String setType){
        return AjaxResult.success(timeSettingService.getAtLeast(setType));
    }

}
