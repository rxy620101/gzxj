package com.ruoyi.scholarShip.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.scholarShip.domain.AppealInfo;
import com.ruoyi.scholarShip.service.IAppealInfoService;
import org.apache.commons.beanutils.ConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 成绩申诉Controller
 *
 * @author raoxy
 * @date 2022-05-19
 */
@RestController
@RequestMapping("/appeal/info")
public class AppealInfoController extends BaseController {
    @Autowired
    private IAppealInfoService appealInfoService;

    /**
     * 查询成绩申诉列表
     */
    @GetMapping("/list")
    public TableDataInfo list(AppealInfo appealInfo)
    {  Map<String, Object> map = appealInfo.getParams();
        if (appealInfo.getParams().get("majorIds") != null) {
            Long[] majorIds = (Long[]) ConvertUtils.convert(appealInfo.getParams().get("majorIds").toString().split(","), Long.class);
            map.put("majorIds", majorIds);
            appealInfo.setParams(map);
        }
        startPage();
        List<AppealInfo> list = appealInfoService.selectAppealInfoList(appealInfo);
        return getDataTable(list);
    }

    /**
     * 获取成绩申诉详细信息
     */
    @GetMapping(value = "/{appealId}")
    public AjaxResult getInfo(@PathVariable("appealId") Long appealId)
    {
        return AjaxResult.success(appealInfoService.selectAppealInfoByAppealId(appealId));
    }

    /**
     * 新增成绩申诉
     */
    @Log(title = "成绩申诉", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AppealInfo appealInfo)
    {
        return toAjax(appealInfoService.insertAppealInfo(appealInfo));
    }

    /**
     * 修改成绩申诉
     */
    @Log(title = "成绩申诉", businessType = BusinessType.UPDATE)
    @PostMapping("/checkAppealInfo")
    public AjaxResult edit(@RequestBody AppealInfo appealInfo)
    {
        return toAjax(appealInfoService.checkAppealInfo(appealInfo));
    }
    /**
     * 查询是否存在成绩申诉
     *
     * @param appealInfo 成绩申诉
     * @return 结果
     */
    @PostMapping("/isExistAppeal")
    public AjaxResult isExistAppeal(@RequestBody AppealInfo appealInfo)
    {
        return AjaxResult.success(appealInfoService.isExistAppeal(appealInfo));
    }

}
