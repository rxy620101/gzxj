package com.ruoyi.scholarShip.service;

import com.ruoyi.scholarShip.domain.GradeInfo;
import com.ruoyi.scholarShip.domain.InstrorInfo;

import java.util.List;
import java.util.Map;

/**
 * 成绩信息Service接口
 * 
 * @author raoxy
 * @date 2022-04-16
 */
public interface IGradeInfoService 
{
    /**
     * 查询成绩信息
     * 
     * @param sno
     * @return 成绩信息
     */
    public GradeInfo selectGradeInfoByGradeId(String  sno);

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
     * 批量删除成绩信息
     * 
     * @param gradeIds 需要删除的成绩信息主键集合
     * @return 结果
     */
    public int deleteGradeInfoByGradeIds(Long[] gradeIds);

    /**
     * 删除成绩信息信息
     * 
     * @param gradeId 成绩信息主键
     * @return 结果
     */
    public int deleteGradeInfoByGradeId(Long gradeId);

    //根据map查询是否存在该课程成绩
    public int selectGradeByMap(Map<String,Object> map);

    /**
     * 导入成绩信息
     *
     * @param gradeList 数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     *  @param map 参数
     * @Param titleNames 表头标题
     * @return 结果
     */
    public String importGradeInfo(List<String[]> gradeList, Boolean isUpdateSupport, String operName, Map<String,Object> map,String[] titleNames);
}
