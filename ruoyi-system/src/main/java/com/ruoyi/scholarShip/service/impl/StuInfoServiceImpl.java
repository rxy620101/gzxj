package com.ruoyi.scholarShip.service.impl;

import java.util.List;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.bean.BeanValidators;
import com.ruoyi.system.mapper.SysColleageMajorMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.scholarShip.mapper.StuInfoMapper;
import com.ruoyi.scholarShip.domain.StuInfo;
import com.ruoyi.scholarShip.service.IStuInfoService;

import javax.validation.Validator;

/**
 * 学生基础信息Service业务层处理
 *
 * @author raoxy
 * @date 2022-04-11
 */
@Service
public class StuInfoServiceImpl implements IStuInfoService {
    @Autowired
    private StuInfoMapper stuInfoMapper;

    private static final Logger log = LoggerFactory.getLogger(StuInfoServiceImpl.class);

    @Autowired
    protected Validator validator;

    @Autowired
    private SysColleageMajorMapper sysColleageMajorMapper;

    /**
     * 查询学生基础信息
     *
     * @param id 学生基础信息主键
     * @return 学生基础信息
     */
    @Override
    public StuInfo selectStuInfoById(Long id) {
        return stuInfoMapper.selectStuInfoById(id);
    }
    /**
     * 查询学生基础信息
     *
     * @param sno 学生基础信息主键
     * @return 学生基础信息
     */
    @Override
    public StuInfo selectStuInfoBySno(String  sno) {
        return stuInfoMapper.selectStuInfoBySno(sno);
    }

    /**
     * 查询学生基础信息列表
     *
     * @param stuInfo 学生基础信息
     * @return 学生基础信息
     */
    @Override
    public List<StuInfo> selectStuInfoList(StuInfo stuInfo, Long[] majorIds,String grade) {
        return stuInfoMapper.selectStuInfoList(stuInfo,majorIds,grade);
    }

    /**
     * 新增学生基础信息
     *
     * @param stuInfo 学生基础信息
     * @return 结果
     */
    @Override
    public int insertStuInfo(StuInfo stuInfo) {
        stuInfo.setCreateTime(DateUtils.getNowDate());
        return stuInfoMapper.insertStuInfo(stuInfo);
    }

    /**
     * 修改学生基础信息
     *
     * @param stuInfo 学生基础信息
     * @return 结果
     */
    @Override
    public int updateStuInfo(StuInfo stuInfo) {
        stuInfo.setUpdateTime(DateUtils.getNowDate());
        return stuInfoMapper.updateStuInfo(stuInfo);
    }

    /**
     * 批量删除学生基础信息
     *
     * @param ids 需要删除的学生基础信息主键
     * @return 结果
     */
    @Override
    public int deleteStuInfoByIds(Long[] ids) {
        return stuInfoMapper.deleteStuInfoByIds(ids);
    }

    /**
     * 删除学生基础信息信息
     *
     * @param id 学生基础信息主键
     * @return 结果
     */
    @Override
    public int deleteStuInfoById(Long id) {
        return stuInfoMapper.deleteStuInfoById(id);
    }

    /**
     * 根据majorId查询对应的grade
     *
     * @param majorId
     * @return 结果
     */
    @Override
    public String[] selectGrade(Long majorId) {
        return stuInfoMapper.selectGrade(majorId);
    }

    /**
     * 根据grade查询对应的班级
     *
     * @param grade 需要删除的数据主键集合
     * @return 结果
     */
    @Override
    public String[] selectClass(String grade) {
        return stuInfoMapper.selectClass(grade);
    }

    /*
      检验sno是否唯一
     */
    @Override
    public int checkSnoUnique(String sno) {
        return stuInfoMapper.checkSnoUnique(sno);
    }

    /**
     * 导入学生数据
     *
     * @param stuList         用户数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    @Override
    public String importStuData(List<StuInfo> stuList, Boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(stuList) || stuList.size() == 0) {
            throw new ServiceException("导入学生数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (StuInfo stu : stuList) {
            try {
                // 验证是否存在这个sno
                int u = stuInfoMapper.checkSnoUnique(stu.getSno());
                if (isUpdateSupport) {
                    if (u != 1) {
                        //插入
                        this.doInser(stu, operName);
                        successNum++;
                        successMsg.append("<br/>" + successNum + "、学号 " + stu.getSno() + " 导入成功");

                    } else {
                        //更新
                        BeanValidators.validateWithException(validator, stu);
                        stu.setUpdateBy(operName);
                        //根据学院/专业名称查找id
                        Long collegeId = sysColleageMajorMapper.selectIdbyName(stu.getCollegeName());
                        Long majorId = sysColleageMajorMapper.selectIdbyName(stu.getMajorName());
                        stu.setCollegeId(collegeId);
                        stu.setMajorId(majorId);
                        this.updateStuInfo(stu);
                        successNum++;
                        successMsg.append("<br/>" + successNum + "、学号 " + stu.getSno() + " 更新成功");
                    }
                } else {
                    if (u != 1) {
                        //插入
                        this.doInser(stu, operName);
                        successNum++;
                        successMsg.append("<br/>" + successNum + "、学号 " + stu.getSno() + " 导入成功");
                    } else {
                        failureNum++;
                        failureMsg.append("<br/>" + failureNum + "、学号 " + stu.getSno() + " 已存在");
                    }
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、学号 " + stu.getSno() + " 导入失败：";
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

    public int doInser(StuInfo stu, String operName) {
        BeanValidators.validateWithException(validator, stu);
        stu.setCreateBy(operName);
        //根据学院/专业名称查找id
        Long collegeId = sysColleageMajorMapper.selectIdbyName(stu.getCollegeName());
        Long majorId = sysColleageMajorMapper.selectIdbyName(stu.getMajorName());
        stu.setCollegeId(collegeId);
        stu.setMajorId(majorId);
        return this.insertStuInfo(stu);
    }
}
