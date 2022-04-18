package com.ruoyi.scholarShip.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.JsonUtil;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.scholarShip.domain.GradeInfo;
import com.ruoyi.scholarShip.service.IGradeInfoService;
import com.ruoyi.system.domain.Course;
import com.ruoyi.system.service.ICourseService;
import com.ruoyi.system.service.ISysColleageMajorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 成绩信息Controller
 *
 * @author raoxy
 * @date 2022-04-16
 */
@RestController
@RequestMapping("/grade/info")
public class GradeInfoController extends BaseController {
    @Autowired
    private IGradeInfoService gradeInfoService;
    @Autowired
    private ISysColleageMajorService sysColleageMajorService;
    @Autowired
    private ICourseService courseService;

    /**
     * 查询成绩信息列表
     */
    @PreAuthorize("@ss.hasPermi('grade:info:list')")
    @GetMapping("/list")
    public TableDataInfo list(GradeInfo gradeInfo) {   //获取专业名
//        String major = gradeInfo.getParams().get("majorNames").toString();
//        String[] majorNames = major.split(",");
//        Map<String, Object> map = gradeInfo.getParams();
//        map.put("majorNames", majorNames);
//        gradeInfo.setParams(map);
        List<GradeInfo> list = gradeInfoService.selectGradeInfoList(gradeInfo);
        if (list.size() == 0) {
            //如果为0 ，返回对应的课程信息
            Course course=new Course();
            this.getCourse(gradeInfo,course);
            List<Course> courseList = courseService.selectCourseList(course);
            startPage();
            return getDataTable(courseList);
        } else {

            //将一个同学的成绩对应成一个list 降序排列 只能2.2.1.1 或者2.1
            List<Map<String, Object>> allStuList = new ArrayList<>();
            //先存入第一个值
            if (allStuList.size() == 0) {
                Map<String, Object> firstSno = new HashMap<>();
                List<Course> courseList = new ArrayList<>();
                String sno = list.get(0).getSno();
                this.addStuList(sno, firstSno, courseList, list, 0);
                allStuList.add(firstSno);
            }
            int num = 0;
            for (int i = 1; i <= list.size() - 1; i++) {
                String sno = list.get(i).getSno();
                Map<String, Object> stuMap = new HashMap<>();
                List<Course> courseList = new ArrayList<Course>();
                if (allStuList.get(num).get("sno").toString().equals(sno)) {
                    //取出对应的map 和courseList
                    stuMap = allStuList.get(num);
                    //取出原有的值
                    courseList = (List<Course>) allStuList.get(num).get("courseInfo");
                    Course course = list.get(i).getCourse();
                    course.setScore(list.get(i).getScore());
                    course.setHaveCredit(list.get(i).getCredit());
                    courseList.add(course);
                    stuMap.put("courseInfo", courseList);
                    allStuList.remove(num);
                    allStuList.add(stuMap);
                } else {
                    //新增
                    this.addStuList(sno, stuMap, courseList, list, i);
                    allStuList.add(stuMap);
                    num++;
                }
            }
            startPage();
            return getDataTable(allStuList);
        }
    }

    //新增一个stu
    public void addStuList(String sno, Map<String, Object> stuMap, List<Course> courseList, List<GradeInfo> list, int index) {
        stuMap.put("sno", sno);
        stuMap.put("stuName", list.get(index).getStuName());
        Course course = list.get(index).getCourse();
        course.setScore(list.get(index).getScore());
        course.setHaveCredit(list.get(index).getCredit());
        courseList.add(course);
        stuMap.put("courseInfo", courseList);

    }
    //查找课程信息
    public void getCourse(GradeInfo gradeInfo,Course course){
        String startYear=gradeInfo.getParams().get("startYear").toString();
        String validTerm=gradeInfo.getParams().get("validTerm").toString();
        String grade=gradeInfo.getParams().get("grade").toString();
        course.setParams(gradeInfo.getParams());
        course.setStartYear(startYear);
        course.setValidTerm(validTerm);
        course.setGrade(grade);
    }

    /**
     * 导出成绩信息列表
     */
    @PreAuthorize("@ss.hasPermi('grade:info:export')")
    @Log(title = "成绩信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, GradeInfo gradeInfo) {
        List<GradeInfo> list = gradeInfoService.selectGradeInfoList(gradeInfo);
        ExcelUtil<GradeInfo> util = new ExcelUtil<GradeInfo>(GradeInfo.class);
        util.exportExcel(response, list, "成绩信息数据");
    }

    /**
     * 获取成绩信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('grade:info:query')")
    @GetMapping(value = "/{sno}")
    public AjaxResult getInfo(@PathVariable("sno") String sno) {
        return AjaxResult.success(gradeInfoService.selectGradeInfoByGradeId(sno));
    }

    /**
     * 新增成绩信息
     */
    @PreAuthorize("@ss.hasPermi('grade:info:add')")
    @Log(title = "成绩信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody GradeInfo gradeInfo) {
        return toAjax(gradeInfoService.insertGradeInfo(gradeInfo));
    }

    /**
     * 修改成绩信息
     */
    @PreAuthorize("@ss.hasPermi('grade:info:edit')")
    @Log(title = "成绩信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody GradeInfo gradeInfo) {
        return toAjax(gradeInfoService.updateGradeInfo(gradeInfo));
    }

    /**
     * 删除成绩信息
     */
    @PreAuthorize("@ss.hasPermi('grade:info:remove')")
    @Log(title = "成绩信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{gradeIds}")
    public AjaxResult remove(@PathVariable Long[] gradeIds) {
        return toAjax(gradeInfoService.deleteGradeInfoByGradeIds(gradeIds));
    }
}
