package com.ruoyi.scholarShip.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysColleageMajor;
import com.ruoyi.scholarShip.domain.AwardSetting;
import com.ruoyi.scholarShip.domain.HardApply;
import com.ruoyi.scholarShip.domain.HardRank;
import com.ruoyi.scholarShip.service.impl.AwardApplyServiceImpl;
import com.ruoyi.scholarShip.service.impl.AwardSettingServiceImpl;
import com.ruoyi.scholarShip.service.impl.HardApplyServiceImpl;
import com.ruoyi.scholarShip.service.impl.HardRankServiceImpl;
import com.ruoyi.system.mapper.SysColleageMajorMapper;
import com.ruoyi.system.service.impl.SysCollageMajorServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 统计模块Controller
 *
 * @author raoxy
 * @date 2022-05-03
 */
@RestController
@RequestMapping("/summary/info")
public class SummaryController extends BaseController {
    @Autowired
    private AwardSettingServiceImpl awardSettingService;
    @Autowired
    private HardRankServiceImpl hardRankService;
    @Autowired
    private AwardApplyServiceImpl awardApplyService;
    @Autowired
    private HardApplyServiceImpl hardApplyService;
    @Autowired
    private SysCollageMajorServiceImpl sysCollageMajorService;

    //奖学金合计
    @PostMapping("/totalAward")
    public AjaxResult totalAward(@RequestBody Map<String, Object> map) {
        List<Map<String, Object>> totalList = new ArrayList<>();
        //获取collegeId，applyYear,applyTerm
        String applyYear = map.get("applyYear").toString();
        String applyTem = map.get("applyTerm").toString();
        AwardSetting awardSetting = new AwardSetting();
        awardSetting.setSetYear(applyYear);
        awardSetting.setSetTerm(applyTem);
        //获取所有奖项设置,具体类
        List<AwardSetting> awardSettingList = awardSettingService.selectAwardSettingList(awardSetting);
        if (map.get("collegeId") == null) {
            //默认查找所有的学院统计信息
            List<SysColleageMajor> allCollege = sysCollageMajorService.selectAllByParentId(Long.valueOf("100"));
            for (SysColleageMajor sysColleageMajor : allCollege) {
                Long collegeId = sysColleageMajor.getcollegeId();
                String collegeName=sysCollageMajorService.selectCollegeName(collegeId);
                //循环遍历每种奖项的申请人数
                this.getTypesNum(awardSettingList, totalList, collegeId,collegeName);
            }
        } else {
            Long collegeId = Long.valueOf(map.get("collegeId").toString());
            String collegeName=sysCollageMajorService.selectCollegeName(collegeId);
            //循环遍历每种奖项的申请人数
            this.getTypesNum(awardSettingList, totalList, collegeId,collegeName);
        }
        return AjaxResult.success(totalList);
    }

    //循环遍历每种奖项的申请人数
    public void getTypesNum(List<AwardSetting> awardSettingList, List<Map<String, Object>> totalList, Long collegeId,String collegeName) {
        for (AwardSetting awardSetting : awardSettingList) {
            Map<String, Object> map = new HashMap<>();
            map.put("applyYear", awardSetting.getSetYear());
            map.put("applyTerm", awardSetting.getSetTerm());
            map.put("collegeId", collegeId);
            map.put("rankId", awardSetting.getSettingId());
            //存储每个等级的人数、金额
            Map<String, Object> totalInfo = new HashMap<>();
            totalInfo.put("awardName", awardSetting.getAwardNames());
            totalInfo.put("money", awardSetting.getMoney());
            //获取对应的申请人数
            int n = awardApplyService.selectTotalPeople(map);
            totalInfo.put("allPeople", n);
            totalInfo.put("allMoney", awardSetting.getMoney() * n);
            totalInfo.put("collegeName", collegeName);
            totalList.add(totalInfo);
        }
    }

    //助学金合计
    @PostMapping("/totalHard")
    public AjaxResult totalHard(@RequestBody Map<String, Object> map) {
        List<Map<String, Object>> totalList = new ArrayList<>();
//获取collegeId，applyYear
        String applyYear = map.get("applyYear").toString();
        //获取所有的评定等级
        List<HardRank>hardRankList=hardRankService.selectHardRankList();
        if (map.get("collegeId") == null) {
            //默认查找所有的学院统计信息
            List<SysColleageMajor> allCollege = sysCollageMajorService.selectAllByParentId(Long.valueOf("100"));
            for (SysColleageMajor sysColleageMajor : allCollege) {
                Long collegeId = sysColleageMajor.getcollegeId();
                String collegeName=sysCollageMajorService.selectCollegeName(collegeId);
                //循环遍历每种等级的申请人数
                this.getRanksNum(hardRankList, totalList, collegeId,collegeName,applyYear);
            }
        } else {
            Long collegeId = Long.valueOf(map.get("collegeId").toString());
            String collegeName=sysCollageMajorService.selectCollegeName(collegeId);
            //循环遍历每种等级的申请人数
            this.getRanksNum(hardRankList, totalList, collegeId,collegeName,applyYear);
        }
        return AjaxResult.success(totalList);
    }

    //获取每个等级的申请人数总和

    public void getRanksNum(List<HardRank> hardRankList, List<Map<String, Object>> totalList, Long collegeId,String collegeName,String applyYear){
        for (HardRank hardRank : hardRankList) {
            Map<String, Object> map = new HashMap<>();
            map.put("applyYear", applyYear);
            map.put("collegeId", collegeId);
            map.put("rankId", hardRank.getRangkId());
            //存储每个等级的人数、金额
            Map<String, Object> totalInfo = new HashMap<>();
            totalInfo.put("rankName", hardRank.getRankName());
            totalInfo.put("money", hardRank.getMoney());
            //获取对应的申请人数
            int n = hardApplyService.selectTotalPeople(map);
            totalInfo.put("allPeople", n);
            totalInfo.put("allMoney", hardRank.getMoney().intValue()* n);
            totalInfo.put("collegeName", collegeName);
            totalList.add(totalInfo);
        }

    }
}
