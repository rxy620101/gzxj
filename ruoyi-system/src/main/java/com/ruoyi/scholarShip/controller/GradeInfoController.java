package com.ruoyi.scholarShip.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.scholarShip.domain.GradeInfo;
import com.ruoyi.scholarShip.service.IGradeInfoService;
import com.ruoyi.system.domain.Course;
import com.ruoyi.system.service.ISysColleageMajorService;
import org.apache.commons.beanutils.ConvertUtils;
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

    /**
     * 查询成绩信息列表
     */
    @PreAuthorize("@ss.hasPermi('grade:info:list')")
    @GetMapping("/list")
    public TableDataInfo list(GradeInfo gradeInfo) {   //获取专业名
        String major = gradeInfo.getParams().get("majorNames").toString();
        String[] majorNames = major.split(",");
        Map<String, Object> map = new HashMap<>();
        map.put("majorNames", majorNames);
        gradeInfo.setParams(map);
        List<GradeInfo> list = gradeInfoService.selectGradeInfoList(gradeInfo);
//         return getDataTable(list);
        //将一个同学的成绩对应成一个list 降序排列 只能2.2.1.1 或者2.1
        List<Map<String, Object>> allStuList = new ArrayList<>();
        //冒泡法
        for (int i = 0; i < list.size()-1; i++) {
            //最外层返回的数据
            Map<String, Object> stuList = new HashMap<>();
            List<Map<String, Object>> courseList = new ArrayList<>();
            //先判断最外层是否存在该学号,存在则直接跳过 ,不存在则存
            String outsno=list.get(i).getSno();
            if( ! allStuList.contains(list.get(i).getSno())){
                //最里层对应的课程-成绩
                Map<String, Object> courseMap = new HashMap<>();
                stuList.put("sno",list.get(i).getSno());
                stuList.put("stuName",list.get(i).getStuName());
                courseMap.put("course",list.get(i).getCourse());
                courseMap.put("soce",list.get(i).getScore());
                courseList.add(courseMap);
            }
            //存在则直接跳过
            else{
                continue;
            }
            for(int j=i+1;j<list.size()-i-1;j++){
               String inerSno=list.get(j).getSno();
               if(inerSno.equals(outsno)){
                   //最里层对应的课程-成绩
                   Map<String, Object> courseMap = new HashMap<>();
                   courseMap.put("course",list.get(i).getCourse());
                   courseMap.put("soce",list.get(i).getScore());
                   courseList.add(courseMap);
               }
            }
            stuList.put("courseList",courseList);
            allStuList.add(stuList);


//            String before_sno = list.get(i).getSno();
//            String after_sno = list.get(i + 1).getSno();
//            //先判断第i个是否存在 不存在则先添加 每次只存一个
//            if (!allStuList.contains(before_sno)) {
//                stuList.put("sno", before_sno);
//                stuList.put("stuName", list.get(i).getStuName());
//                courseMap.put("courseInfo", list.get(i).getCourse());
//                courseMap.put("score", list.get(i).getScore());
//                courseList.add(courseMap);
//                stuList.put("courseList", courseList);
//            }
//            if (before_sno.equals(after_sno)) {
//                //相等只取其中一个  已存在 传入课程信息  1.1的情况
//                courseMap.put("courseInfo", list.get(i + 1).getCourse());
//                courseMap.put("score", list.get(i).getScore());
//                courseList.add(courseMap);
//                stuList.put("courseList", courseList);
//
//            } else {
//                if (!allStuList.contains(before_sno)) {
//                    // 1.2 的情况
//                    courseMap.put("courseInfo", list.get(i).getCourse());
//                    courseMap.put("score", list.get(i).getScore());
//                    courseList.add(courseMap);
//                    stuList.put("courseList", courseList);
//                    stuList.put("sno", before_sno);
//                    stuList.put("stuName", list.get(i).getStuName());
//                }
//                //存后面的
//                courseMap.put("course", list.get(i + 1).getCourse());
//                courseMap.put("score", list.get(i + 1).getScore());
//                courseList.add(courseMap);
//                stuList.put("courseList", courseList);
//                stuList.put("sno", after_sno);
//                stuList.put("stuName", list.get(i + 1).getStuName());
//            }
//            allStuList.add(stuList);
//            //判断此时i的位置
//            if (i == list.size() - 2) {
//                break;
//            }
        }
        startPage();
        return getDataTable(allStuList);
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
