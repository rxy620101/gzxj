package com.ruoyi.system.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.bean.BeanValidators;
import com.ruoyi.system.domain.MajorCourse;
import com.ruoyi.system.mapper.MajorCourseMapper;
import com.ruoyi.system.mapper.SysColleageMajorMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.CourseMapper;
import com.ruoyi.system.domain.Course;
import com.ruoyi.system.service.ICourseService;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Validator;

/**
 * 课程Service业务层处理
 *
 * @author raoxy
 * @date 2022-04-05
 */
@Service
public class CourseServiceImpl implements ICourseService {
    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private MajorCourseMapper majorCourseMapper;

    @Autowired
    private SysColleageMajorMapper sysColleageMajorMapper;

    @Autowired
    protected Validator validator;

    private static final Logger log = LoggerFactory.getLogger(CourseServiceImpl.class);

    /**
     * 查询课程
     *
     * @param coseId 课程主键
     * @return 课程
     */
    @Override
    public Course selectCourseByCoseId(Long coseId) {
        return courseMapper.selectCourseByCoseId(coseId);
    }

    /**
     * 查询课程列表
     *
     * @param course 课程
     * @return 课程
     */
    @Override
    public List<Course> selectCourseList(Course course) {
        return courseMapper.selectCourseList(course);
    }

    /**
     * 新增课程
     *
     * @param course 课程
     * @return 结果
     */
    @Override
    @Transactional
    public int insertCourse(Course course) {
        // 新增
        int rows = courseMapper.insertCourse(course);
        // 新增课程与专业关联表
        insertMajorCourse(course);
        return rows;
    }

    /**
     * 修改课程
     *
     * @param course 课程
     * @return 结果
     */
    @Override
    @Transactional
    public int updateCourse(Course course) {
        Long couseId = course.getCoseId();
        // 删除专业和课程关联
        majorCourseMapper.deleteMajorCourseByCouseId(couseId);
        // 新增专业和课程管理
        insertMajorCourse(course);
        return courseMapper.updateCourse(course);
    }

    /**
     * 批量删除课程
     *
     * @param coseIds   需要删除的课程主键
     * @param collegeId
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteCourses(Long[] coseIds, Long collegeId) {
        // 删除专业与课程关联
        int result = majorCourseMapper.deleteMajorCourses(coseIds, collegeId);
        if (collegeId != 0) {
            return result;
        } else {
            return courseMapper.deleteCourseByCoseIds(coseIds);
        }
    }
    /**
     * 删除课程信息
     *
     * @param course 课程
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteCourse(Course course) {      // 删除专业与课程关联
        MajorCourse majorCourse = new MajorCourse();
        majorCourse.setParams(course.getParams());
        if (course.getParams().get("collegeId") != null && course.getParams().get("collegeId") != "") {
            majorCourse.setCollegeId((Long) course.getParams().get("collegeId"));
            return majorCourseMapper.deleteMajorCourse(majorCourse);
        } else {
            majorCourseMapper.deleteMajorCourse(majorCourse);
            return courseMapper.deleteCourseByCoseId(course.getCoseId());
        }

    }

    /*
    检验名称是否唯一

     */
    @Override
    public int checkCourseUnique(Course course) {
        return courseMapper.checkCourseUnique(course);
    }

    /**
     * 校验课程是否唯一
     *
     * @param majorName 专业名字
     * @return 结果
     * @Param course 课程信息
     */
    @Override
    public int checkCourseUnique(Course course, String majorName) {
        return courseMapper.checkCourseUniqueByName(course, majorName);
    }


    @Override
    public int updateCourseStatus(Course course) {
        return courseMapper.updateCourseStatus(course);
    }

    /**
     * 新增专业-课程信息
     *
     * @param course 课程对象
     */
    public void insertMajorCourse(Course course) {
        Long[] collegeIds = course.getMajorIds();
        if (StringUtils.isNotNull(collegeIds)) {
            // 新增专业-课程信息
            List<MajorCourse> list = new ArrayList<MajorCourse>();
            for (Long collegeId : collegeIds) {
                MajorCourse ur = new MajorCourse();
                ur.setCourseId(course.getCoseId());
                ur.setCollegeId(collegeId);
                list.add(ur);
            }
            if (list.size() > 0) {
                majorCourseMapper.batchMajorCourse(list);
            }
        }
    }

    /**
     * 修改
     *
     * @param courseId 课程id
     * @param majorIds 专业ids
     */
    public void insertMajorCourse(Long courseId, Long[] majorIds) {
        if (StringUtils.isNotNull(majorIds)) {
            // 新增专业-课程信息
            List<MajorCourse> list = new ArrayList<MajorCourse>();
            for (Long collegeId : majorIds) {
                MajorCourse ur = new MajorCourse();
                ur.setCourseId(courseId);
                ur.setCollegeId(collegeId);
                list.add(ur);
            }
            if (list.size() > 0) {
                majorCourseMapper.batchMajorCourse(list);
            }
        }
    }
    @Override
    public Long[] selectIdsByNames(String[] coseNames, Map<String,Object> map){
        return courseMapper.selectIdsByNames(coseNames,map);
    }

    /**
     * 导入数据
     *
     * @param courseList 数据列表
     *                   暂不支持更新
     * @param operName   操作用户
     * @return 结果
     */
    @Override
    public String importCourse(List<Course> courseList, String operName) {
        if (StringUtils.isNull(courseList) || courseList.size() == 0) {
            throw new ServiceException("导入课程数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (Course course : courseList) {
            try {
                /*
                  1.专业不存在，课程已存在
                  2.专业课程都存在
                  3.都不存在
                 */
                //判断第二种情况
                int u = courseMapper.checkCourseUniqueByName(course, course.getMajorName());
                if (u != 1) {
                    int n = courseMapper.checkCourseUnique(course);
                    if (n != 1) {
                        //都不存在，同时插入课程信息和专业-课程联系表
                        BeanValidators.validateWithException(validator, course);
                        course.setCreateBy(operName);
                        this.insertCourse(course);
                        //获取专业id 和课程id
                        Long courseId=courseMapper.selectIdByCourse(course);
                        course.setCoseId(courseId);
                        this.addMajorCourse(course);
                        successNum++;
                        successMsg.append("<br/>" + successNum + "、专业为 " + course.getMajorName() + "的" + course.getCoseName() + " 课程信息导入成功");
                    } else {
                        //已存在该课程，只插入到专业-课程联系表中
                        Long courseId=courseMapper.selectIdByCourse(course);
                        course.setCoseId(courseId);
                        this.addMajorCourse(course);
                        successNum++;
                        successMsg.append("<br/>" + successNum + "、专业为 " + course.getMajorName() + "的" + course.getCoseName() + " 课程信息导入成功");
                    }
                } else {
                    //课程信息和专业-课程均已经存在
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、专业为 " + course.getMajorName() + "的" + course.getCoseName() + " 课程信息已存在!!!");
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、专业为 " + course.getMajorName() + "的" + course.getCoseName() + "的课程信息导入失败：";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new ServiceException(failureMsg.toString());
        } else {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }

    public void addMajorCourse(Course course) {
        //获取专业id
        Long majorId = sysColleageMajorMapper.selectIdbyName(course.getMajorName());
        MajorCourse majorCourse = new MajorCourse();
        majorCourse.setCourseId(course.getCoseId());
        majorCourse.setCollegeId(majorId);
        majorCourseMapper.addMajorCourse(majorCourse);
    }
}
