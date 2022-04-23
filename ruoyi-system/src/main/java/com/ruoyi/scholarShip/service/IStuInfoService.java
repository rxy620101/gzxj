package com.ruoyi.scholarShip.service;

import java.util.List;
import com.ruoyi.scholarShip.domain.StuInfo;

/**
 * 学生基础信息Service接口
 * 
 * @author raoxy
 * @date 2022-04-11
 */
public interface IStuInfoService 
{
    /**
     * 查询学生基础信息
     * 
     * @param id 学生基础信息主键
     * @return 学生基础信息
     */
    public StuInfo selectStuInfoById(Long id);

    /**
     * 查询学生基础信息列表
     * 
     * @param stuInfo 学生基础信息
     * @return 学生基础信息集合
     */
    public List<StuInfo> selectStuInfoList(StuInfo stuInfo, Long[] majorIds,String grade);

    /**
     * 新增学生基础信息
     * 
     * @param stuInfo 学生基础信息
     * @return 结果
     */
    public int insertStuInfo(StuInfo stuInfo);

    /**
     * 修改学生基础信息
     * 
     * @param stuInfo 学生基础信息
     * @return 结果
     */
    public int updateStuInfo(StuInfo stuInfo);

    /**
     * 批量删除学生基础信息
     * 
     * @param ids 需要删除的学生基础信息主键集合
     * @return 结果
     */
    public int deleteStuInfoByIds(Long[] ids);

    /**
     * 删除学生基础信息信息
     * 
     * @param id 学生基础信息主键
     * @return 结果
     */
    public int deleteStuInfoById(Long id);

    /**
     * 根据majorId查询对应的grade
     *
     * @param majorId
     * @return 结果
     */
    public String[] selectGrade(Long majorId);

    /**
     * 根据grade查询对应的班级
     *
     * @param grade 需要删除的数据主键集合
     * @return 结果
     */
    public String[] selectClass(String grade);

    /**
     * 校验学号是否唯一
     *
     * @param sno 学号
     * @return 结果
     */
    public int checkSnoUnique(String sno);


    /**
     * 导入学生数据
     *
     * @param stuList 用户数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    public String importStuData(List<StuInfo> stuList, Boolean isUpdateSupport,String operName);

    /**
     * 查询学生基础信息
     *
     * @param sno 学生基础信息主键
     * @return 学生基础信息
     */
    public StuInfo selectStuInfoBySno(String  sno);

    /**
     * 根据majorId查询对应的班级
     *
     * @param majorId
     * @return 结果
     */
    public String[] selClassByMajorIds(Long majorId);

}
