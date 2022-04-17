package com.ruoyi.web.controller.system;

import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.core.domain.entity.SysColleageMajor;
import com.ruoyi.system.service.ISysColleageMajorService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.Course;
import com.ruoyi.system.service.ICourseService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 课程Controller
 * 
 * @author raoxy
 * @date 2022-04-05
 */
@RestController
@RequestMapping("/system/course")
public class CourseController extends BaseController
{
    @Autowired
    private ICourseService courseService;
    @Autowired
    private ISysColleageMajorService sysColleageMajorService;


    /**
     * 查询课程列表
     */
    @PreAuthorize("@ss.hasPermi('system:course:list')")
    @GetMapping("/list")
    public TableDataInfo list(Course course)
    {
        startPage();
        List<Course> list = courseService.selectCourseList(course);
        return getDataTable(list);
    }

    /**
     * 导出课程列表
     */
    @PreAuthorize("@ss.hasPermi('system:course:export')")
    @Log(title = "课程", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Course course)
    {
        List<Course> list = courseService.selectCourseList(course);
        ExcelUtil<Course> util = new ExcelUtil<Course>(Course.class);
        util.exportExcel(response, list, "课程数据");
    }
    /*
    导入课程表
     */
    @Log(title = "课程管理", businessType = BusinessType.IMPORT)
    @PreAuthorize("@ss.hasPermi('system:course:import')")
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file) throws Exception
    {
        ExcelUtil<Course> util = new ExcelUtil<Course>(Course.class);
        List<Course> courseList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = courseService.importCourse(courseList,operName);
        return AjaxResult.success(message);
    }

    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response)
    {
        ExcelUtil<Course> util = new ExcelUtil<Course>(Course.class);
        util.importTemplateExcel(response, "课程数据");
    }


    /**
     * 获取课程详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:course:query')")
    @GetMapping(value ={"/", "/{coseId}"})
    public AjaxResult getInfo(@PathVariable(value = "coseId",required = false) Long coseId)
    {

      AjaxResult ajax = AjaxResult.success();
        Course course=courseService.selectCourseByCoseId(coseId);
        ajax.put(AjaxResult.DATA_TAG, course);
        ajax.put("majorIds", course.getMajors().stream().map(SysColleageMajor::getcollegeId).collect(Collectors.toList()));
        return ajax;
    }

    /**
     * 新增课程
     */
    @PreAuthorize("@ss.hasPermi('system:course:add')")
    @Log(title = "课程管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add( @Validated @RequestBody Course course)
    {
//        if (UserConstants.NOT_UNIQUE.equals(courseService.checkCourseNameUnique(course.getCoseName())))
//        {
//            return AjaxResult.error("新增课程'" + course.getCoseName() + "'失败，该课程名称已存在");
//        }
        course.setCreateBy(getUsername());
        return toAjax(courseService.insertCourse(course));
    }

    /**
     * 修改课程
     */
    @PreAuthorize("@ss.hasPermi('system:course:edit')")
    @Log(title = "课程管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody Course course)
    {
        return toAjax(courseService.updateCourse(course));
    }

    /**
     * 删除课程
     */
    @PreAuthorize("@ss.hasPermi('system:course:remove')")
    @Log(title = "课程管理", businessType = BusinessType.DELETE)
    @GetMapping("/del/{coseIds}/{collegeId}")
    public AjaxResult remove(@PathVariable Long[] coseIds,@PathVariable  Long collegeId)
    {
    return toAjax(courseService.deleteCourses(coseIds,collegeId));

    }
    /**
     * 状态修改
     */
    @PreAuthorize("@ss.hasPermi('system:course:edit')")
    @Log(title = "课程管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody Course course)
    {
//        course.checkUserDataScope(course.getUserId());
        course.setUpdateBy(getUsername());
        return toAjax(courseService.updateCourseStatus(course));
    }

}
