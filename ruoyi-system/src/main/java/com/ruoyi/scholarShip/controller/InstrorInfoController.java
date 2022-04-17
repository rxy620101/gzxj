package com.ruoyi.scholarShip.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.system.service.ISysColleageMajorService;
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
import com.ruoyi.scholarShip.domain.InstrorInfo;
import com.ruoyi.scholarShip.service.IInstrorInfoService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 辅导员基础信息Controller
 * 
 * @author raoxy
 * @date 2022-04-11
 */
@RestController
@RequestMapping("/instructor/info")
public class InstrorInfoController extends BaseController
{
    @Autowired
    private IInstrorInfoService instrorInfoService;

    @Autowired
    private ISysColleageMajorService sysColleageMajorService;

    /**
     * 查询辅导员基础信息列表
     */
    @PreAuthorize("@ss.hasPermi('instructor:info:list')")
    @GetMapping("/list")
    public TableDataInfo list(InstrorInfo instrorInfo)
    {
        startPage();
        List<InstrorInfo> list = instrorInfoService.selectInstrorInfoList(instrorInfo);
        return getDataTable(list);
    }

    /**
     * 导出辅导员基础信息列表
     */
    @PreAuthorize("@ss.hasPermi('instructor:info:export')")
    @Log(title = "辅导员基础信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, InstrorInfo instrorInfo)
    {
        List<InstrorInfo> list = instrorInfoService.selectInstrorInfoList(instrorInfo);
        ExcelUtil<InstrorInfo> util = new ExcelUtil<InstrorInfo>(InstrorInfo.class);
        util.exportExcel(response, list, "辅导员基础信息数据");
    }

    /**
     * 获取辅导员基础信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('instructor:info:query')")
    @GetMapping(value ={"/", "/{id}"})
    public AjaxResult getInfo(@PathVariable(value = "id",required = false) Long id)
    {
        if(id==null){
        id=Long.valueOf(getUsername());
        InstrorInfo instrorInfo=instrorInfoService.selectInstrorInfoById((id));
        AjaxResult ajaxResult=AjaxResult.success(instrorInfo);
         //获取该辅导员指导的专业名称
         Long[] majorIds=(Long[]) ConvertUtils.convert(instrorInfo.getGuideMajorIds().split(","),Long[].class);
        String[] majorNames=sysColleageMajorService.selectMajorNames(majorIds);
        ajaxResult.put("majorNames",majorNames);
        return ajaxResult;
    }
        return AjaxResult.success(instrorInfoService.selectInstrorInfoById(id));
    }

    /**
     * 新增辅导员基础信息
     */
    @PreAuthorize("@ss.hasPermi('instructor:info:add')")
    @Log(title = "辅导员基础信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody InstrorInfo instrorInfo)
    {   instrorInfo.setCreateBy(getUsername());
        return toAjax(instrorInfoService.insertInstrorInfo(instrorInfo));
    }

    /**
     * 修改辅导员基础信息
     */
//    @PreAuthorize("@ss.hasPermi('instructor:info:edit')")
    @Log(title = "辅导员基础信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody InstrorInfo instrorInfo)
    {    instrorInfo.setUpdateBy(getUsername());
        return toAjax(instrorInfoService.updateInstrorInfo(instrorInfo));
    }

    /**
     * 删除辅导员基础信息
     */
    @PreAuthorize("@ss.hasPermi('instructor:info:remove')")
    @Log(title = "辅导员基础信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(instrorInfoService.deleteInstrorInfoByIds(ids));
    }

    /*
  检验职工号的唯一性
  */
    @GetMapping("/checkId/{id}")
    public  AjaxResult checkUnqiue(@PathVariable String id){
        int n=instrorInfoService.checkId(id);
        if(n>0){
            return AjaxResult.success(n);
        }
        return AjaxResult.success();
    }
    /*
    数据导入
     */
    @Log(title = "辅导员基础信息", businessType = BusinessType.IMPORT)
    @PreAuthorize("@ss.hasPermi('instructor:info:import')")
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception
    {
        ExcelUtil<InstrorInfo> util = new ExcelUtil<InstrorInfo>(InstrorInfo.class);
        List<InstrorInfo> stuList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = instrorInfoService.importInstuctorData(stuList, updateSupport, operName);
        return AjaxResult.success(message);
    }

    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response)
    {
        ExcelUtil<InstrorInfo> util = new ExcelUtil<InstrorInfo>(InstrorInfo.class);
        util.importTemplateExcel(response, "辅导员基础信息数据");
    }

}

