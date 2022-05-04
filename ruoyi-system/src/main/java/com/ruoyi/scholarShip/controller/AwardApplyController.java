package com.ruoyi.scholarShip.controller;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.ConvertUtils;
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
import com.ruoyi.scholarShip.domain.AwardApply;
import com.ruoyi.scholarShip.service.IAwardApplyService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 奖学金申请Controller
 *
 * @author raoxy
 * @date 2022-04-25
 */
@RestController
@RequestMapping("/award/info")
public class AwardApplyController extends BaseController {
    @Autowired
    private IAwardApplyService awardApplyService;

    /**
     * 查询奖学金申请列表
     */
    @PreAuthorize("@ss.hasPermi('award:info:list')")
    @GetMapping("/list")
    public TableDataInfo list(AwardApply awardApply) {
        Map<String, Object> map = awardApply.getParams();
        if (awardApply.getParams().get("majorIds") != null) {
            Long[] majorIds = (Long[]) ConvertUtils.convert(awardApply.getParams().get("majorIds").toString().split(","), Long.class);
            map.put("majorIds", majorIds);
            awardApply.setParams(map);
        }
        startPage();
        List<AwardApply> list = awardApplyService.selectAwardApplyList(awardApply);
        return getDataTable(list);
    }

    /**
     * 导出奖学金申请列表
     */
    @PreAuthorize("@ss.hasPermi('award:info:export')")
    @Log(title = "奖学金申请", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AwardApply awardApply) {
        List<AwardApply> list = awardApplyService.selectAwardApplyList(awardApply);
        ExcelUtil<AwardApply> util = new ExcelUtil<AwardApply>(AwardApply.class);
        util.exportExcel(response, list, "奖学金申请数据");
    }

    /**
     * 获取奖学金申请详细信息
     */
    @PreAuthorize("@ss.hasPermi('award:info:query')")
    @GetMapping(value = "/{applyId}")
    public AjaxResult getInfo(@PathVariable("applyId") Long applyId) {
        return AjaxResult.success(awardApplyService.selectAwardApplyByApplyId(applyId));
    }

    /**
     * 新增奖学金申请
     */
    @PreAuthorize("@ss.hasPermi('award:info:add')")
    @Log(title = "奖学金申请", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AwardApply awardApply) {
        return toAjax(awardApplyService.insertAwardApply(awardApply));
    }

    /**
     * 修改奖学金申请
     */
    @PreAuthorize("@ss.hasPermi('award:info:edit')")
    @Log(title = "奖学金申请", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AwardApply awardApply) {
        return toAjax(awardApplyService.updateAwardApply(awardApply));
    }

    /**
     * 删除奖学金申请
     */
    @PreAuthorize("@ss.hasPermi('award:info:remove')")
    @Log(title = "奖学金申请", businessType = BusinessType.DELETE)
    @DeleteMapping("/{applyIds}")
    public AjaxResult remove(@PathVariable Long[] applyIds) {
        return toAjax(awardApplyService.deleteAwardApplyByApplyIds(applyIds));
    }

    /**
     * 审核
     */
    @PreAuthorize("@ss.hasPermi('award:info:check')")
    @Log(title = "奖学金申请", businessType = BusinessType.DELETE)
    @PostMapping("/checkApply")
    public AjaxResult checkApply(@RequestBody AwardApply awardApply) {
        return toAjax(awardApplyService.checkApply(awardApply));
    }

    /**
     * 发布
     */
    @PostMapping("/publishApply")
    public AjaxResult publishApply(@RequestBody AwardApply awardApply) {
        Map<String, Object> map = awardApply.getParams();
        if (awardApply.getParams().get("majorIds") != null) {
            Long[] majorIds = (Long[]) ConvertUtils.convert(awardApply.getParams().get("majorIds").toString().split(","), Long.class);
            map.put("majorIds", majorIds);
            awardApply.setParams(map);
        }
        return toAjax(awardApplyService.publishApply(awardApply));
    }
}
