package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysColleageMajor;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.service.ISysColleageMajorService;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;
import java.util.List;

/**
 * 院系-专业信息
 * 
 * @author raoxy
 */
@RestController
@RequestMapping("/system/dept")
public class SysColleageMajorController extends BaseController
{
    @Autowired
    private ISysColleageMajorService deptService;

    /**
     * 获取部门列表
     */
    @PreAuthorize("@ss.hasPermi('college:major:list')")
    @GetMapping("/list")
    public AjaxResult list(SysColleageMajor dept)
    {
        List<SysColleageMajor> depts = deptService.selectDeptList(dept);
        return AjaxResult.success(depts);
    }

    /**
     * 查询部门列表（排除节点）
     */
    @PreAuthorize("@ss.hasPermi('college:major:list')")
    @GetMapping("/list/exclude/{collegeId}")
    public AjaxResult excludeChild(@PathVariable(value = "collegeId", required = false) Long deptId)
    {
        List<SysColleageMajor> depts = deptService.selectDeptList(new SysColleageMajor());
        Iterator<SysColleageMajor> it = depts.iterator();
        while (it.hasNext())
        {
            SysColleageMajor d = (SysColleageMajor) it.next();
            if (d.getcollegeId().intValue() == deptId
                    || ArrayUtils.contains(StringUtils.split(d.getAncestors(), ","), deptId + ""))
            {
                it.remove();
            }
        }
        return AjaxResult.success(depts);
    }

    /**
     * 根据部门编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('college:major:query')")
    @GetMapping(value = "/{collegeId}")
    public AjaxResult getInfo(@PathVariable Long collegeId)
    {
        deptService.checkDeptDataScope(collegeId);
        return AjaxResult.success(deptService.selectDeptById(collegeId));
    }

    /**
     * 获取部门下拉树列表
     */
    @GetMapping("/treeselect")
    public AjaxResult treeselect(SysColleageMajor dept)
    {
        List<SysColleageMajor> depts = deptService.selectDeptList(dept);
        return AjaxResult.success(deptService.buildDeptTreeSelect(depts));
    }

//    /**
//     * 加载对应角色部门列表树
//     */
//    @GetMapping(value = "/roleDeptTreeselect/{roleId}")
//    public AjaxResult roleDeptTreeselect(@PathVariable("roleId") Long roleId)
//    {
//        List<SysColleageMajor> depts = deptService.selectDeptList(new SysColleageMajor());
//        AjaxResult ajax = AjaxResult.success();
//        ajax.put("checkedKeys", deptService.selectDeptListByRoleId(roleId));
//        ajax.put("depts", deptService.buildDeptTreeSelect(depts));
//        return ajax;
//    }

    /**
     * 新增部门
     */
    @PreAuthorize("@ss.hasPermi('college:major:add')")
    @Log(title = "部门管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysColleageMajor dept)
    {
        if (UserConstants.NOT_UNIQUE.equals(deptService.checkDeptNameUnique(dept)))
        {
            return AjaxResult.error("新增院系或专业'" + dept.getName() + "'失败，名称已存在");
        }
        dept.setCreateBy(getUsername());
        return toAjax(deptService.insertDept(dept));
    }

    /**
     * 修改部门
     */
    @PreAuthorize("@ss.hasPermi('college:major:edit')")
    @Log(title = "部门管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysColleageMajor dept)
    {
        Long deptId = dept.getcollegeId();
        deptService.checkDeptDataScope(deptId);
        if (UserConstants.NOT_UNIQUE.equals(deptService.checkDeptNameUnique(dept)))
        {
            return AjaxResult.error("修改部门'" + dept.getName() + "'失败，名称已存在");
        }
        else if (dept.getParentId().equals(deptId))
        {
            return AjaxResult.error("修改部门'" + dept.getName() + "'失败，上级部门不能是自己");
        }
        else if (StringUtils.equals(UserConstants.DEPT_DISABLE, dept.getStatus()) && deptService.selectNormalChildrenDeptById(deptId) > 0)
        {
            return AjaxResult.error("该部门包含未停用的子部门！");
        }
        dept.setUpdateBy(getUsername());
        return toAjax(deptService.updateDept(dept));
    }

    /**
     * 删除部门
     */
    @PreAuthorize("@ss.hasPermi('college:major:remove')")
    @Log(title = "部门管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{collegeId}")
    public AjaxResult remove(@PathVariable Long collegeId)
    {
        if (deptService.hasChildByDeptId(collegeId))
        {
            return AjaxResult.error("存在下级部门,不允许删除");
        }
        if (deptService.checkDeptExistUser(collegeId))
        {
            return AjaxResult.error("部门存在用户,不允许删除");
        }
        deptService.checkDeptDataScope(collegeId);
        return toAjax(deptService.deleteDeptById(collegeId));
    }
    /**
     * 根据parentId获取部门列表
     */
    @GetMapping("/getListByParentId/{parentId}")
    public AjaxResult getListByParentId(@PathVariable Long parentId)
    {
        List<SysColleageMajor> depts = deptService.selectAllByParentId(parentId);
        return AjaxResult.success(depts);
    }
    /**
     * 根据MajorIds获取专业名字
     */
    @PostMapping("/selectMajorNames")
    public AjaxResult selectMajorNames(@RequestBody Long[] majorIds)
    {
      return AjaxResult.success(deptService.selectMajorNames(majorIds));

    }

}
