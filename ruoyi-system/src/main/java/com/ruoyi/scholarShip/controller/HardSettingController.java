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
import com.ruoyi.scholarShip.domain.HardSetting;
import com.ruoyi.scholarShip.service.IHardSettingService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 助学金参数设置Controller
 * 
 * @author raoxy
 * @date 2022-04-23
 */
@RestController
@RequestMapping("/hardSetting/info")
public class HardSettingController extends BaseController
{
    @Autowired
    private IHardSettingService hardSettingService;

    /**
     * 查询助学金参数设置列表
     */
    @PreAuthorize("@ss.hasPermi('hardSetting:info:list')")
    @GetMapping("/list")
    public TableDataInfo list(HardSetting hardSetting)
    {
        startPage();
        List<HardSetting> list = hardSettingService.selectHardSettingList(hardSetting);
        return getDataTable(list);
    }

    /**
     * 导出助学金参数设置列表
     */
    @PreAuthorize("@ss.hasPermi('hardSetting:info:export')")
    @Log(title = "助学金参数设置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, HardSetting hardSetting)
    {
        List<HardSetting> list = hardSettingService.selectHardSettingList(hardSetting);
        ExcelUtil<HardSetting> util = new ExcelUtil<HardSetting>(HardSetting.class);
        util.exportExcel(response, list, "助学金参数设置数据");
    }

    /**
     * 获取助学金参数设置详细信息
     */
    @PreAuthorize("@ss.hasPermi('hardSetting:info:query')")
    @GetMapping(value = "/{settingId}")
    public AjaxResult getInfo(@PathVariable("settingId") Long settingId)
    {
        return AjaxResult.success(hardSettingService.selectHardSettingBySetting(settingId));
    }

    /**
     * 新增助学金参数设置
     */
    @PreAuthorize("@ss.hasPermi('hardSetting:info:add')")
    @Log(title = "助学金参数设置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody HardSetting hardSetting)
    {
        return toAjax(hardSettingService.insertHardSetting(hardSetting));
    }

    /**
     * 修改助学金参数设置
     */
    @PreAuthorize("@ss.hasPermi('hardSetting:info:edit')")
    @Log(title = "助学金参数设置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody HardSetting hardSetting)
    {
        return toAjax(hardSettingService.updateHardSetting(hardSetting));
    }

    /**
     * 删除助学金参数设置
     */
    @PreAuthorize("@ss.hasPermi('hardSetting:info:remove')")
    @Log(title = "助学金参数设置", businessType = BusinessType.DELETE)
	@DeleteMapping("/{settings}")
    public AjaxResult remove(@PathVariable Long[] settings)
    {
        return toAjax(hardSettingService.deleteHardSettingBySettings(settings));
    }
}
