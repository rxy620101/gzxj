package com.ruoyi.scholarShip.controller;

import java.util.*;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.scholarShip.domain.GradeInfo;
import com.ruoyi.system.domain.Course;
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
public class HardSettingController extends BaseController {
    @Autowired
    private IHardSettingService hardSettingService;
    private List<HardSetting> instructors;

    /**
     * 查询助学金参数设置列表
     */
    @PreAuthorize("@ss.hasPermi('hardSetting:info:list')")
    @GetMapping("/list")
    public TableDataInfo list(HardSetting hardSetting) {
        startPage();
        List<HardSetting> list = hardSettingService.selectHardSettingList(hardSetting);
        //返回一个学院一个年级对应一条记录 [ {[]}]
        //返回总的
        List<Map<String,Object>> allList=new ArrayList<>();
        //记录辅导员与限额
        if(list.size()!=0){
            int num=0;
            for(int i=0;i<list.size();i++){
                //传入第一个
                List<HardSetting> instrctorList=new ArrayList<>();
                //记录学院、年级、总数和辅导员信息
                Map<String,Object> hardMap=new HashMap<>();
                if(i==0){
                    this.addInstructor(hardMap,instrctorList,list,0);
                    allList.add(hardMap);
                }
                else{
                   Long collegeId=list.get(i).getCollegeId();
                   String grade=list.get(i).getGrade();
                   int allNum=list.get(i).getAllNum();
                    Long existCollegeId=Long.valueOf(allList.get(num).get("collegeId").toString());
                    String existGrade=allList.get(num).get("grade").toString();
                    int existAllNum=(int)allList.get(num).get("allNum");
                    if (collegeId == existCollegeId && grade.equals(existGrade) && allNum==existAllNum) {
                        //取出对应的map 和courseList
                        hardMap = allList.get(num);
                        //取出原有的值
                        instrctorList = (List<HardSetting>) allList.get(num).get("instructorList");
                        instrctorList.add(list.get(i));
                        hardMap.put("instructorList", instrctorList);
                        allList.remove(num);
                        allList.add(hardMap);
                    } else {
                        //新增
                       this.addInstructor(hardMap,instrctorList,list,i);
                        allList.add(hardMap);
                        num++;
                    }
                }
            }
        }
        return getDataTable(allList);
    }
    //新增一个instructor
    public void addInstructor( Map<String, Object> hardMap, List<HardSetting> instrctorList, List<HardSetting> list, int index) {
        hardMap.put("collegeId",list.get(index).getCollegeId());
        hardMap.put("collegeName",list.get(index).getCollegeName());
        hardMap.put("setYear",list.get(index).getSetYear());
        hardMap.put("grade",list.get(index).getGrade());
        hardMap.put("allNum",list.get(index).getAllNum());
        hardMap.put("createTime",list.get(index).getCreateTime());
        instrctorList.add(list.get(index));
        hardMap.put("instructorList",instrctorList);
    }
    /**
     * 导出助学金参数设置列表
     */
    @PreAuthorize("@ss.hasPermi('hardSetting:info:export')")
    @Log(title = "助学金参数设置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, HardSetting hardSetting) {
        List<HardSetting> list = hardSettingService.selectHardSettingList(hardSetting);
        ExcelUtil<HardSetting> util = new ExcelUtil<HardSetting>(HardSetting.class);
        util.exportExcel(response, list, "助学金参数设置数据");
    }

    /**
     * 获取助学金参数设置详细信息
     */
    @PreAuthorize("@ss.hasPermi('hardSetting:info:query')")
    @GetMapping(value = "/{settingId}")
    public AjaxResult getInfo(@PathVariable("settingId") Long settingId) {
        return AjaxResult.success(hardSettingService.selectHardSettingBySetting(settingId));
    }

    //获取辅导员的限额数组
    @PostMapping("/getInstructor")
    public void getInstructor(@RequestBody List<HardSetting> hardSettings) {
        this.instructors = hardSettings;
    }

    /**
     * 新增助学金参数设置
     */
    @PreAuthorize("@ss.hasPermi('hardSetting:info:add')")
    @Log(title = "助学金参数设置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody HardSetting hardSetting) {
        List<HardSetting> hardSettings = new ArrayList<>();
        for (HardSetting hard : this.instructors) {
            HardSetting hardset = new HardSetting();
            hardset.setCollegeId(hardSetting.getCollegeId());
            hardset.setAllNum(hardSetting.getAllNum());
            hardset.setGrade(hardSetting.getGrade());
            hardset.setSetYear(hardSetting.getSetYear());
            hardset.setInstructorId(hard.getInstructorId());
            hardset.setCreateBy(getUsername());
            hardset.setNum(hard.getNum());
//           hardSettingService.insertHardSetting(hardset);
            hardSettings.add(hardset);
        }
        return toAjax(hardSettingService.batchAdd(hardSettings));
    }

    /**
     * 修改助学金参数设置
     */
    @PreAuthorize("@ss.hasPermi('hardSetting:info:edit')")
    @Log(title = "助学金参数设置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody HardSetting hardSetting) {
        return toAjax(hardSettingService.updateHardSetting(hardSetting));
    }

    /**
     * 删除助学金参数设置
     */
    @PreAuthorize("@ss.hasPermi('hardSetting:info:remove')")
    @Log(title = "助学金参数设置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{settings}")
    public AjaxResult remove(@PathVariable Long[] settings) {
        return toAjax(hardSettingService.deleteHardSettingBySettings(settings));
    }
}
