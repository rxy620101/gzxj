package com.ruoyi.scholarShip.service.impl;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.scholarShip.domain.GradeInfo;
import com.ruoyi.scholarShip.mapper.GradeInfoMapper;
import com.ruoyi.scholarShip.service.IGradeInfoService;
import com.ruoyi.system.domain.Course;
import com.ruoyi.system.service.impl.CourseServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 成绩信息Service业务层处理
 *
 * @author raoxy
 * @date 2022-04-16
 */
@Service
public class GradeInfoServiceImpl implements IGradeInfoService {
    @Autowired
    private GradeInfoMapper gradeInfoMapper;

    @Autowired
    private CourseServiceImpl courseService;
    private static final Logger log = LoggerFactory.getLogger(GradeInfoServiceImpl.class);


    /**
     * 查询成绩信息
     *
     * @param sno 成绩信息主键
     * @return 成绩信息
     */
    @Override
    public GradeInfo selectGradeInfoByGradeId(String sno) {
        return gradeInfoMapper.selectGradeInfoBysno(sno);
    }

    /**
     * 查询成绩信息列表
     *
     * @param gradeInfo 成绩信息
     * @return 成绩信息
     */
    @Override
    public List<GradeInfo> selectGradeInfoList(GradeInfo gradeInfo) {
        return gradeInfoMapper.selectGradeInfoList(gradeInfo);
    }

    /**
     * 新增成绩信息
     *
     * @param gradeInfo 成绩信息
     * @return 结果
     */
    @Transactional
    @Override
    public int insertGradeInfo(GradeInfo gradeInfo) {
        gradeInfo.setCreateTime(DateUtils.getNowDate());
        int rows = gradeInfoMapper.insertGradeInfo(gradeInfo);
        return rows;
    }

    /**
     * 修改成绩信息
     *
     * @param gradeInfo 成绩信息
     * @return 结果
     */
    @Transactional
    @Override
    public int updateGradeInfo(GradeInfo gradeInfo) {
        gradeInfo.setUpdateTime(DateUtils.getNowDate());
        return gradeInfoMapper.updateGradeInfo(gradeInfo);
    }

    /**
     * 批量删除成绩信息
     *
     * @param gradeIds 需要删除的成绩信息主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteGradeInfoByGradeIds(Long[] gradeIds) {
        return gradeInfoMapper.deleteGradeInfoByGradeIds(gradeIds);
    }

    /**
     * 删除成绩信息信息
     *
     * @param gradeId 成绩信息主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteGradeInfoByGradeId(Long gradeId) {

        return gradeInfoMapper.deleteGradeInfoByGradeId(gradeId);
    }

    //根据map查询是否存在该课程成绩
    public int selectGradeByMap(Map<String, Object> map) {
        return gradeInfoMapper.selectGradeByMap(map);
    }

    /**
     * 导入成绩信息
     *
     * @param gradeList       数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @param map             参数
     * @return 结果
     * @Param titleNames 表头标题
     */
    @Override
    public String importGradeInfo(List<String[]> gradeList, Boolean isUpdateSupport, String operName, Map<String, Object> map, String[] titleNames) {
        if (StringUtils.isNull(gradeList) || gradeList.size() == 0) {
            throw new ServiceException("导入的成绩数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        //根据专业名称获取id
        Long[] coseIds = courseService.selectIdsByNames(titleNames,map);

        //学号 姓名 课程名
        for (String[] stuGrade : gradeList) {
            try {
                //选择更新 先判断是否存在，不更新也要判断是否传值
                //遍历成绩
                for (int i = 2; i < stuGrade.length; i++) {
                    //存入该专业 学年 学期 学号 课程id
                    map.put("sno", stuGrade[0]);
                    map.put("courseId", coseIds[i-2]);
                    // 验证是否存在这个sno的课程成绩
                    int u = this.selectGradeByMap(map);
                    GradeInfo gradeInfo = null;
                    if (u != 1) {
                        //执行插入
                        gradeInfo=new GradeInfo();
                      this.insertOrUpdate(gradeInfo,stuGrade,coseIds,i,operName,"insert");
                        successNum++;
                        successMsg.append("<br/>" + successNum + "、 " + gradeInfo.getStuName() + "的" + titleNames[i] + " 成绩导入成功");
                    } else {
                        if (isUpdateSupport) {
                            //更新
                            gradeInfo=new GradeInfo();
                            this.insertOrUpdate(gradeInfo,stuGrade,coseIds,i,operName,"update");
                            successNum++;
                            successMsg.append("<br/>" + successNum + "、 " + gradeInfo.getStuName() + "的" + titleNames[i] + " 成绩更新成功");;
                        } else {
                            //已存在
                            failureNum++;
                            failureMsg.append("<br/>" + failureNum +"、"+ stuGrade[1] + "的" + titleNames[i]+ " 成绩已存在！！");
                        }
                    }
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum +"、"+ stuGrade[0] + "的成绩 导入失败：";
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
    public void insertOrUpdate(GradeInfo gradeInfo,String [] stuGrade,Long[] coseIds,int i,String operName,String operate){
        gradeInfo.setSno(stuGrade[0]);
        gradeInfo.setStuName(stuGrade[1]);
        gradeInfo.setCoseId(coseIds[i-2]);
        gradeInfo.setScore(Double.valueOf(stuGrade[i]));
        //根据课程Id得到课程学分
        Course course=courseService.selectCourseByCoseId(coseIds[i-2]);
        gradeInfo.setCredit(course.getCredit());
        if(operate.equals("insert")){
            gradeInfo.setCreateBy(operName);
            gradeInfoMapper.insertGradeInfo(gradeInfo);
        }
        else{
            gradeInfo.setUpdateBy(operName);
            gradeInfoMapper.updateGradeInfo(gradeInfo);
        }
    }
}
