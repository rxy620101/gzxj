package com.ruoyi.scholarShip.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.scholarShip.domain.InstrorInfo;
import com.ruoyi.scholarShip.service.IInstrorInfoService;
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
import com.ruoyi.scholarShip.domain.StuInfo;
import com.ruoyi.scholarShip.service.IStuInfoService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 学生基础信息Controller
 *
 * @author raoxy
 * @date 2022-04-11
 */
@RestController
@RequestMapping("/scholarShip/stuInfo")
public class StuInfoController extends BaseController {
    @Autowired
    private IStuInfoService stuInfoService;
    @Autowired
    private IInstrorInfoService instrorInfoService;

    /**
     * 查询学生基础信息列表
     */
    @PreAuthorize("@ss.hasPermi('stu:info:list')")
    @GetMapping("/list")
    public TableDataInfo list(StuInfo stuInfo) {
        Long[] majorIds = null;
        String grade=null;
        if (!getUsername().equals("admin")) {
            //数据过滤（辅导员只能查看该带领的学生）
            InstrorInfo instrorInfo = instrorInfoService.selectInstrorInfoById(Long.valueOf(getUsername()));
            if (instrorInfo != null) {
                majorIds = (Long[]) ConvertUtils.convert(instrorInfo.getGuideMajorIds().split(","), Long.class);
                grade=instrorInfo.getGuideGrade();
            }
        }
        startPage();
        List<StuInfo> list = stuInfoService.selectStuInfoList(stuInfo, majorIds,grade);
        return getDataTable(list);
    }

    /**
     * 导出学生基础信息列表
     */
    @PreAuthorize("@ss.hasPermi('stu:info:export')")
    @Log(title = "学生基础信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StuInfo stuInfo) {
        InstrorInfo instrorInfo = instrorInfoService.selectInstrorInfoById(getUserId());
        Long[] majorIds = null;
        String grade=null;
        if (instrorInfo != null) {
            majorIds = (Long[]) ConvertUtils.convert(instrorInfo.getGuideMajorIds().split(","), Long.class);
            grade=instrorInfo.getGuideGrade();
        }
        List<StuInfo> list = stuInfoService.selectStuInfoList(stuInfo, majorIds,grade);
        ExcelUtil<StuInfo> util = new ExcelUtil<StuInfo>(StuInfo.class);
        util.exportExcel(response, list, "学生基础信息数据");
    }

    /**
     * 获取学生基础信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('stu:info:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(stuInfoService.selectStuInfoById(id));
    }
    @GetMapping(value = "/selBySno/{sno}")
    public AjaxResult getInfoBySno(@PathVariable("sno") String sno) {
        return AjaxResult.success(stuInfoService.selectStuInfoBySno(sno));
    }

    /**
     * 新增学生基础信息
     */
    @PreAuthorize("@ss.hasPermi('stu:info:add')")
    @Log(title = "学生基础信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody StuInfo stuInfo) {
        return toAjax(stuInfoService.insertStuInfo(stuInfo));
    }

    /**
     * 修改学生基础信息
     */
//    @PreAuthorize("@ss.hasPermi('stu:info:edit')")
    @Log(title = "学生基础信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody StuInfo stuInfo) {
        return toAjax(stuInfoService.updateStuInfo(stuInfo));
    }

    /**
     * 删除学生基础信息
     */
    @PreAuthorize("@ss.hasPermi('stu:info:remove')")
    @Log(title = "学生基础信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(stuInfoService.deleteStuInfoByIds(ids));
    }

    /*
       根据majorId查询年级列表
     */
    @GetMapping("/selectGrade/{majorId}")
    public AjaxResult selectGrade(@PathVariable Long majorId) {
        return AjaxResult.success(stuInfoService.selectGrade(majorId));
    }

    /*
      根据grade查询班级列表
     */
    @GetMapping("/selectClass/{grade}")
    public AjaxResult selectClass(@PathVariable String grade) {
        return AjaxResult.success(stuInfoService.selectClass(grade));
    }

    /*
   检验学号的唯一性
   */
    @GetMapping("/checkSno/{sno}")
    public AjaxResult checkSnoUnque(@PathVariable String sno) {
        int n = stuInfoService.checkSnoUnique(sno);
        if (n > 0) {
            return AjaxResult.success(n);
        }
        return AjaxResult.success();
    }

    /*
    数据导入
     */
    @Log(title = "学生基础信息", businessType = BusinessType.IMPORT)
    @PreAuthorize("@ss.hasPermi('stu:info:import')")
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<StuInfo> util = new ExcelUtil<StuInfo>(StuInfo.class);
        List<StuInfo> stuList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = stuInfoService.importStuData(stuList, updateSupport, operName);
        return AjaxResult.success(message);
    }

    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        ExcelUtil<StuInfo> util = new ExcelUtil<StuInfo>(StuInfo.class);
        util.importTemplateExcel(response, "学生基础信息数据");
    }


}
