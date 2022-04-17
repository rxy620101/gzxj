package com.ruoyi.scholarShip.mapper;


import com.ruoyi.scholarShip.domain.GradeInfo;

import java.util.List;

/**
 * 成绩信息Mapper接口
 * 
 * @author raoxy
 * @date 2022-04-16
 */
public interface GradeInfoMapper 
{
    /**
     * 查询成绩信息
     * 
     * @param sno 成绩信息主键
     * @return 成绩信息
     */
    public GradeInfo selectGradeInfoBysno(String sno);

    /**
     * 查询成绩信息列表
     * 
     * @param gradeInfo 成绩信息
     * @return 成绩信息集合
     */
    public List<GradeInfo> selectGradeInfoList(GradeInfo gradeInfo);

    /**
     * 新增成绩信息
     * 
     * @param gradeInfo 成绩信息
     * @return 结果
     */
    public int insertGradeInfo(GradeInfo gradeInfo);

    /**
     * 修改成绩信息
     * 
     * @param gradeInfo 成绩信息
     * @return 结果
     */
    public int updateGradeInfo(GradeInfo gradeInfo);

    /**
     * 删除成绩信息
     * 
     * @param gradeId 成绩信息主键
     * @return 结果
     */
    public int deleteGradeInfoByGradeId(Long gradeId);

    /**
     * 批量删除成绩信息
     * 
     * @param gradeIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteGradeInfoByGradeIds(Long[] gradeIds);



}
