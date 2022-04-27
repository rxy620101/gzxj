package com.ruoyi.scholarShip.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        int n=0;
        for (Map map : list) {
            //插入对应信息
            AwardInitList awardInitList = new AwardInitList();
            awardInitList.setYearInfo(yearInfo);
            awardInitList.setTermInfo(termInfo);
            awardInitList.setMajorName(majorName);
            awardInitList.setGrade(grade);
            this.setAwardInfo(awardInitList,map);
            //执行插入
            n=this.insertAwardInitList(awardInitList);
        }
        return n;
    }

    public void setAwardInfo(AwardInitList awardInitList, Map map) {
        awardInitList.setSno(map.get("sno").toString());
        awardInitList.setStuName(map.get("stuName").toString());
        awardInitList.setTotalScore(BigDecimal.valueOf(Double.parseDouble(map.get("totalScore").toString())));
        awardInitList.setGradeCredit(BigDecimal.valueOf(Double.parseDouble(map.get("totalCredit").toString())));
        awardInitList.setGradeRank((Integer) map.get("ranking"));
    }
}
