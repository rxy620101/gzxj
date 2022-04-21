package com.ruoyi.system.service;

import java.util.List;
import java.util.Map;

import com.ruoyi.system.domain.Course;
import org.apache.ibatis.annotations.Param;


/**
 * 课程Service接口
 * 
 * @author raoxy
 * @date 2022-04-05
 */
public interface ICourseService 
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
     * 批量删除课程
     * 
     * @param coseIds 需要删除的课程主键集合
     *  @param collegeId 需要删除的课程主键集合
     * @return 结果
     */
    public int deleteCourses(Long[]coseIds,Long collegeId);

    /**
     * 删除课程信息
     * 
     * @param course 课程
     * @return 结果
     */
    public int deleteCourse(Course course);


    /**
     * 校验课程是否唯一
     *
     * @param course 课程
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
    public int checkCourseUnique(Course course,String majorName);


    /**
     * 修改课程状态
     *
     * @param course 课程
     * @return 结果
     */
    public int updateCourseStatus(Course course);

    /**
     * 导入课程数据
     *
     * @param corseList 用户数据列表
     *暂不支持更新
     * @param operName 操作用户
     * @return 结果
     */
    public String importCourse(List<Course> corseList, String operName);

    //通过课程名称查询课程id
    public Long[] selectIdsByNames(String[] coseNames, Map<String,Object> map);



}
