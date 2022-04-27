package com.ruoyi.scholarShip.service.impl;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import com.ruoyi.scholarShip.domain.AwardSetting;
import com.ruoyi.scholarShip.mapper.AwardSettingMapper;
import com.ruoyi.scholarShip.mapper.StuPrizesMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.scholarShip.mapper.AwardInitListMapper;
import com.ruoyi.scholarShip.domain.AwardInitList;
import com.ruoyi.scholarShip.service.IAwardInitListService;

/**
 * 奖学金初始名单Service业务层处理
 *
 * @author raoxy
 * @date 2022-04-26
 */
@Service
public class AwardInitListServiceImpl implements IAwardInitListService {
    @Autowired
    private AwardInitListMapper awardInitListMapper;

    @Autowired
    private StuPrizesMapper stuPrizesMapper;
    @Autowired
    private AwardSettingMapper awardSettingMapper;

    /**
     * 查询奖学金初始名单
     *
     * @param initId 奖学金初始名单主键
     * @return 奖学金初始名单
     */
    @Override
    public AwardInitList selectAwardInitListByInitId(Long initId) {
        return awardInitListMapper.selectAwardInitListByInitId(initId);
    }

    /**
     * 查询奖学金初始名单列表
     *
     * @param awardInitList 奖学金初始名单
     * @return 奖学金初始名单
     */
    @Override
    public List<AwardInitList> selectAwardInitListList(AwardInitList awardInitList) {
        return awardInitListMapper.selectAwardInitListList(awardInitList);
    }

    /**
     * 新增奖学金初始名单
     *
     * @param awardInitList 奖学金初始名单
     * @return 结果
     */
    @Override
    public int insertAwardInitList(AwardInitList awardInitList) {
        return awardInitListMapper.insertAwardInitList(awardInitList);
    }

    /**
     * 修改奖学金初始名单
     *
     * @param awardInitList 奖学金初始名单
     * @return 结果
     */
    @Override
    public int updateAwardInitList(AwardInitList awardInitList) {
        return awardInitListMapper.updateAwardInitList(awardInitList);
    }

    /**
     * 批量删除奖学金初始名单
     *
     * @param initIds 需要删除的奖学金初始名单主键
     * @return 结果
     */
    @Override
    public int deleteAwardInitListByInitIds(Long[] initIds) {
        return awardInitListMapper.deleteAwardInitListByInitIds(initIds);
    }

    /**
     * 删除奖学金初始名单信息
     *
     * @param initId 奖学金初始名单主键
     * @return 结果
     */
    @Override
    public int deleteAwardInitListByInitId(Long initId) {
        return awardInitListMapper.deleteAwardInitListByInitId(initId);
    }

    //接收初始的成绩信息
    @Override
    public int getGradeInfo(Map<String, Object> gradeInfo) {
        //接收学年、学期、专业和成绩数组
        String yearInfo = gradeInfo.get("startYear").toString();
        String termInfo = gradeInfo.get("startTerm").toString();
        String majorName = gradeInfo.get("majorName").toString();
        String grade = gradeInfo.get("grade").toString();
        List<Map<String, Object>> list = (ArrayList<Map<String, Object>>) gradeInfo.get("gradeInfo");
        int n = 1;
        List<AwardInitList> awardInitLists = new ArrayList<>();
        for (Map map : list) {
            //插入对应信息
            AwardInitList awardInitList = new AwardInitList();
            awardInitList.setYearInfo(yearInfo);
            awardInitList.setTermInfo(termInfo);
            awardInitList.setMajorName(majorName);
            awardInitList.setGrade(grade);
            this.setAwardInfo(awardInitList, map);
            //计算名次和奖项
            awardInitLists.add(awardInitList);
        }
        //找出数据库中的对应奖项名称 和名额  如 三等：1  人数*比例向下取整
        List<AwardSetting> awardSettings = awardSettingMapper.selectByParentId(Long.valueOf("2"));
        //存放每个等级对应人数的集合
        List<Map<String, Object>> allNum = new ArrayList<>();
        //计算每个等级的人数
        this.getNum(allNum, awardSettings, awardInitLists.size());
        //对集合进行排序，并设置名次
        this.getSort(awardInitLists);
        //设置奖项等级
        this.setAwardRank(awardInitLists, allNum);
        //批量插入
       awardInitListMapper.batchAdd(awardInitLists);
        return n;
    }

    //设置学生信息
    public void setAwardInfo(AwardInitList awardInitList, Map map) {
        awardInitList.setSno(map.get("sno").toString());
        awardInitList.setStuName(map.get("stuName").toString());
        awardInitList.setTotalScore(BigDecimal.valueOf(Double.parseDouble(map.get("totalScore").toString())));
        awardInitList.setGradeCredit(BigDecimal.valueOf(Double.parseDouble(map.get("totalCredit").toString())));
        awardInitList.setGradeRank((Integer) map.get("ranking"));
        //根据学号、学期、学年计算该学生总的加分项
        BigDecimal allExtraPoint = stuPrizesMapper.getAllPoint(awardInitList.getSno(), awardInitList.getYearInfo(), awardInitList.getTermInfo());
        awardInitList.setAllExtraPoint(allExtraPoint);
        if(allExtraPoint != null){
            awardInitList.setFinalCredit((awardInitList.getGradeCredit().add(allExtraPoint)).floatValue());
        }
        else{
            awardInitList.setFinalCredit(awardInitList.getGradeCredit().floatValue());
        }
    }

    //进行学生集合进行名次排序，并给出初始等级
    public void getSort(List<AwardInitList> awardInitLists) {
        System.out.println(awardInitLists.size());
        // 按照成绩排序
        awardInitLists.sort( new  Comparator<AwardInitList>() {
            @Override
            public  int  compare(AwardInitList s1, AwardInitList s2) {
                return  -Float.compare(s1.getFinalCredit(), s2.getFinalCredit());
            }
        });
        int  rank =  0 ; // 排名
        Float  lastScore = Float.valueOf(- 1); // 最近一次的分

        for  ( int  i =  0 ; i < awardInitLists.size(); i++) {
            AwardInitList s = awardInitLists.get(i);
            if  (Float.compare(lastScore, s.getFinalCredit())!= 0 ) {  // 如果成绩和上一名的成绩不相同,那么排名+1
                lastScore = s.getFinalCredit();
                rank++;
            }
            awardInitLists.get(i).setFinalRank(rank);
        }
//        //设置排名
//        for (AwardInitList award : awardInitList) {
//            award.setFinalRank(rank);
//
//        }
    }

    //计算每个等级对应的人数
    public void getNum(List<Map<String, Object>> allNum, List<AwardSetting> awardList, int allpeople) {
        for (AwardSetting awardSetting : awardList) {
            Map<String, Object> map = new HashMap<>();
            map.put("settingId", awardSetting.getSettingId());
            map.put("rankName", awardSetting.getAwardNames());
            if (awardSetting.getNum() == null) {
                //计算班级占比人数
                double ratio = Double.parseDouble((awardSetting.getRatio()));
                int num = (int) Math.floor(ratio / 100.0 * allpeople);
                map.put("num", num);
            } else {
                map.put("num", Integer.parseInt(awardSetting.getNum()));
            }
            allNum.add(map);
        }
    }

    //设置奖项等级
    public void setAwardRank(List<AwardInitList> awardInitList, List<Map<String, Object>> maps) {
        //计算每轮最后的索引
        int rank = 0;
        for (int i = 0; i < maps.size(); i++) {
            int num = (int) maps.get(i).get("num");
            int index=rank;
                for(;index<rank+num;index++){
                    awardInitList.get(index).setAwardRank(maps.get(i).get("rankName").toString());
                }
               rank=index;
            }
    }
}
