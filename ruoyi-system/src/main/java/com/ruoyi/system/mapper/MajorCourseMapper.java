package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.MajorCourse;
import org.apache.ibatis.annotations.Param;


/**
 * 专业-课程关联Mapper接口
 * 
 * @author raoxy
 * @date 2022-04-05
 */
public interface MajorCourseMapper 
{

    /**
     * 删除专业-课程关联
     *
     * @param coseId  课程关联id
     * @return 结果
     */
    public int deleteMajorCourseByCouseId(Long coseId);

    /**
     * 根据专业id和课程id 删除专业-课程关联
     *
     * @param majorCourse  课程
     * @return 结果
     */
    public int deleteMajorCourse(MajorCourse majorCourse);

    /**
     * 批量删除专业-课程关联
     * 
     * @param  coseIds, 需要删除的数据主键集合
     * @Param  colleagId
     * @return 结果
     */
    public int deleteMajorCourses(@Param("coseIds") Long[]coseIds, @Param("collegeId")Long collegeId);

    /**
     * 批量增加专业-课程关联
     *
     * @param  majorCourses 需要增加的数据主键集合
     * @return 结果
     */
    public int  batchMajorCourse(List<MajorCourse>majorCourses);

    /**
     * 增加专业-课程关联
     *
     * @param  majorCourse 需要增加的数据主键集合
     * @return 结果
     */
    public int  addMajorCourse(MajorCourse majorCourse);
}
