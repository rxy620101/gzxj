package com.ruoyi.scholarShip.controller;

import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.utils.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
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
import com.ruoyi.scholarShip.domain.AwardSetting;
import com.ruoyi.scholarShip.service.IAwardSettingService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 奖学金参数Controller
 * 
 * @author raoxy
 * @date 2022-04-23
 */
@RestController
@RequestMapping("/awardSetting/info")
public class AwardSettingController extends BaseController
{
    @Autowired
    private IAwardSettingService awardSettingService;

    /**
     * 查询奖学金参数列表
     */
    @PreAuthorize("@ss.hasPermi('awardSetting:info:list')")
    @GetMapping("/list")
    public TableDataInfo list(AwardSetting awardSetting)
    {
        startPage();
        List<AwardSetting> list = awardSettingService.selectAwardSettingList(awardSetting);
        return getDataTable(list);
    }

    /**
     * 导出奖学金参数列表
     */
    @PreAuthorize("@ss.hasPermi('awardSetting:info:export')")
    @Log(title = "奖学金参数", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AwardSetting awardSetting)
    {
        List<AwardSetting> list = awardSettingService.selectAwardSettingList(awardSetting);
        ExcelUtil<AwardSetting> util = new ExcelUtil<AwardSetting>(AwardSetting.class);
        util.exportExcel(response, list, "奖学金参数数据");
    }

    /**
     * 获取奖学金参数详细信息
     */
    @PreAuthorize("@ss.hasPermi('awardSetting:info:query')")
    @GetMapping(value = "/{settingId}")
    public AjaxResult getInfo(@PathVariable("settingId") Long settingId)
    {
        return AjaxResult.success(awardSettingService.selectAwardSettingBySetting(settingId));
    }

    /**
     * 新增奖学金参数
     */
    @PreAuthorize("@ss.hasPermi('awardSetting:info:add')")
    @Log(title = "奖学金参数", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AwardSetting awardSetting)
    {   awardSetting.setCreateBy(getUsername());
        return toAjax(awardSettingService.insertAwardSetting(awardSetting));
    }

    /**
     * 修改奖学金参数
     */
    @PreAuthorize("@ss.hasPermi('awardSetting:info:edit')")
    @Log(title = "奖学金参数", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AwardSetting awardSetting)
    {   awardSetting.setUpdateBy(getUsername());
        return toAjax(awardSettingService.updateAwardSetting(awardSetting));
    }

    /**
     * 删除奖学金参数
     */
    @PreAuthorize("@ss.hasPermi('awardSetting:info:remove')")
    @Log(title = "奖学金参数", businessType = BusinessType.DELETE)
	@DeleteMapping("/{settings}")
    public AjaxResult remove(@PathVariable Long[] settings)
    {
        return toAjax(awardSettingService.deleteAwardSettingBySettings(settings));
    }

    //根据名字检查是否存在该类别
    @PostMapping("/checkByName")
    public AjaxResult checkExitByName( @RequestBody String awardName){
        //返回的row小于0时会出现error
        int n= awardSettingService.checkExitByName(awardName);
        AjaxResult ajaxResult=AjaxResult.success();
        ajaxResult.put("rows",n);
        return ajaxResult;
    }

    //根据parentId查询奖项类别
    @GetMapping("/selByParentId/{parentId}")
    public AjaxResult selectByParentId(@PathVariable("parentId") Long parentId){
        return AjaxResult.success(awardSettingService.selectByParentId(parentId));

    }
    /**
     * 查询部门列表（排除节点）
     */
    @PreAuthorize("@ss.hasPermi('award:info:list')")
    @GetMapping("/list/exclude/{settingId}")
    public AjaxResult excludeChild(@PathVariable(value = "settingId", required = false) Long settingId)
    {
        List<AwardSetting> depts = awardSettingService.selectAwardSettingList(new AwardSetting());
        Iterator<AwardSetting> it = depts.iterator();
        while (it.hasNext())
        {
            AwardSetting d = (AwardSetting) it.next();
            if (d.getSettingId().intValue() == settingId
                    || ArrayUtils.contains(StringUtils.split(d.getAncestors(), ","), settingId + ""))
            {
                it.remove();
            }
        }
        return AjaxResult.success(depts);
    }

}
