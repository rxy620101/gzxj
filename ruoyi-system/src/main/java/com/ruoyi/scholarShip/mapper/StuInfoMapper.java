package com.ruoyi.scholarShip.mapper;

import java.util.List;
import com.ruoyi.scholarShip.domain.StuInfo;
import org.apache.ibatis.annotations.Param;

/**
 * 学生基础信息Mapper接口
 * 
 * @author raoxy
 * @date 2022-04-11
 */
public interface StuInfoMapper 
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
    public List<StuInfo> selectStuInfoList(@Param("stu") StuInfo stuInfo, @Param("majorIds") Long[] majorIds,@Param("grade") String grade);

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
     * 删除学生基础信息
     * 
     * @param id 学生基础信息主键
     * @return 结果
     */
    public int deleteStuInfoById(Long id);

    /**
     * 批量删除学生基础信息
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteStuInfoByIds(Long[] ids);

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
     * 查询学生基础信息
     *
     * @param sno 学生基础信息主键
     * @return 学生基础信息
     */
    public StuInfo selectStuInfoBySno(String  sno);
}
