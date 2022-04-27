package com.ruoyi.scholarShip.service;

import java.util.List;
import java.util.Map;

import com.ruoyi.scholarShip.domain.AwardInitList;

/**
 * 奖学金初始名单Service接口
 * 
 * @author raoxy
 * @date 2022-04-26
 */
public interface IAwardInitListService 
{
    /**
     * 查询奖学金初始名单
     * 
     * @param initId 奖学金初始名单主键
     * @return 奖学金初始名单
     */
    public AwardInitList selectAwardInitListByInitId(Long initId);

    /**
     * 查询奖学金初始名单列表
     * 
     * @param awardInitList 奖学金初始名单
     * @return 奖学金初始名单集合
     */
    public List<AwardInitList> selectAwardInitListList(AwardInitList awardInitList);

    /**
     * 新增奖学金初始名单
     * 
     * @param awardInitList 奖学金初始名单
     * @return 结果
     */
    public int insertAwardInitList(AwardInitList awardInitList);

    /**
     * 修改奖学金初始名单
     * 
     * @param awardInitList 奖学金初始名单
     * @return 结果
     */
    public int updateAwardInitList(AwardInitList awardInitList);

    /**
     * 批量删除奖学金初始名单
     * 
     * @param initIds 需要删除的奖学金初始名单主键集合
     * @return 结果
     */
    public int deleteAwardInitListByInitIds(Long[] initIds);

    /**
     * 删除奖学金初始名单信息
     * 
     * @param initId 奖学金初始名单主键
     * @return 结果
     */
    public int deleteAwardInitListByInitId(Long initId);

    //接收初始的成绩信息
    public int getGradeInfo(Map<String,Object> gradeInfo);
}
