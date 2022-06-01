package com.ruoyi.system.mapper;

import java.util.List;
import java.util.Map;

import com.ruoyi.common.core.domain.entity.SysMenu;
import com.ruoyi.system.domain.Course;
import org.apache.ibatis.annotations.Param;

/**
 * 课程Mapper接口
 * 
 * @author raoxy
 * @date 2022-04-05
 */
public interface CourseMapper 
{
    /**
     * 查询课程
     * 
     * @param coseId 课程主键
     * @return 课程
     */
    public Course selectCourseByCoseId(Long coseId);

    /**
     * 查询课程列表
     * 
     * @param course 课程
     * @return 课程集合
     */
    public List<Course> selectCourseList(Course course);

    /**
     * 校验课程是否唯一
     *
     * @param course
     * @return 结果
     */
    public int checkCourseUnique(Course course);

    /**
     * 校验课程是否唯一
     *
     * @Param course 课程信息
     * @param majorName  专业名字
     * @return 结果
     */
    public int checkCourseUniqueByName( @Param("course")Course course,@Param("majorName") String majorName);

    /**
     * 校验课程是否唯一
     *
     * @Param course 课程信息
     * @return 结果
     */
    public int checkCourseUniqueByMaiorId( Course course);


    /**
     * 新增课程
     * 
     * @param course 课程
     * @return 结果
     */
    public int insertCourse(Course course);

    /**
     * 修改课程
     * 
     * @param course 课程
     * @return 结果
     */
    public int updateCourse(Course course);

    /**
     * 删除课程
     * 
     * @param coseId 课程主键
     * @return 结果
     */
    public int deleteCourseByCoseId(Long coseId);

    /**
     * 批量删除课程
     * 
     * @param coseIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCourseByCoseIds(Long[] coseIds);

    /**
     * 根据专业id查询课程
     *
     * @param collegeId  需要查询的专业id
     * @return 结果
     */
    public int selectCourseListByCollegeId(Long collegeId);


    /**
     * 修改课程状态
     *
     * @param course 课程
     * @return 结果
     */
    public int updateCourseStatus(Course course);

    /**
     * 根据课程信息查询id
     *
     * @param course  课程信息
     * @return 结果
     */
    public Long selectIdByCourse(Course course);

    //通过课程名称查询课程id
    public Long[] selectIdsByNames(@Param("coseNames") String[] coseNames, @Param("map") Map<String,Object> map);




}
