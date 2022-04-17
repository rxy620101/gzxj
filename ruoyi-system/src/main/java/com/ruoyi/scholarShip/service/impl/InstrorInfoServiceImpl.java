package com.ruoyi.scholarShip.service.impl;

import java.util.List;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.bean.BeanValidators;
import com.ruoyi.system.service.impl.SysCollageMajorServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.scholarShip.mapper.InstrorInfoMapper;
import com.ruoyi.scholarShip.domain.InstrorInfo;
import com.ruoyi.scholarShip.service.IInstrorInfoService;

import javax.validation.Validator;

/**
 * 辅导员基础信息Service业务层处理
 *
 * @author raoxy
 * @date 2022-04-11
 */
@Service
public class InstrorInfoServiceImpl implements IInstrorInfoService {
    @Autowired
    private InstrorInfoMapper instrorInfoMapper;

    @Autowired
    private SysCollageMajorServiceImpl sysCollageMajorService;

    private static final Logger log = LoggerFactory.getLogger(InstrorInfoServiceImpl.class);

    @Autowired
    protected Validator validator;

    /**
     * 查询辅导员基础信息
     *
     * @param id 辅导员基础信息主键
     * @return 辅导员基础信息
     */
    @Override
    public InstrorInfo selectInstrorInfoById(Long id) {
        return instrorInfoMapper.selectInstrorInfoById(id);
    }

    /**
     * 查询辅导员基础信息列表
     *
     * @param instrorInfo 辅导员基础信息
     * @return 辅导员基础信息
     */
    @Override
    public List<InstrorInfo> selectInstrorInfoList(InstrorInfo instrorInfo) {
        return instrorInfoMapper.selectInstrorInfoList(instrorInfo);
    }

    /**
     * 新增辅导员基础信息
     *
     * @param instrorInfo 辅导员基础信息
     * @return 结果
     */
    @Override
    public int insertInstrorInfo(InstrorInfo instrorInfo) {
        instrorInfo.setCreateTime(DateUtils.getNowDate());
        return instrorInfoMapper.insertInstrorInfo(instrorInfo);
    }

    /**
     * 修改辅导员基础信息
     *
     * @param instrorInfo 辅导员基础信息
     * @return 结果
     */
    @Override
    public int updateInstrorInfo(InstrorInfo instrorInfo) {
        instrorInfo.setUpdateTime(DateUtils.getNowDate());
        return instrorInfoMapper.updateInstrorInfo(instrorInfo);
    }

    /**
     * 批量删除辅导员基础信息
     *
     * @param ids 需要删除的辅导员基础信息主键
     * @return 结果
     */
    @Override
    public int deleteInstrorInfoByIds(Long[] ids) {
        return instrorInfoMapper.deleteInstrorInfoByIds(ids);
    }

    /**
     * 删除辅导员基础信息信息
     *
     * @param id 辅导员基础信息主键
     * @return 结果
     */
    @Override
    public int deleteInstrorInfoById(Long id) {
        return instrorInfoMapper.deleteInstrorInfoById(id);
    }

    /**
     * 校验职工号是否唯一
     *
     * @param id 职工号
     * @return 结果
     */
    @Override
    public int checkId(String id) {
        return instrorInfoMapper.checkId(id);
    }

    /**
     * 导入辅导员数据
     *
     * @param instructorList  用户数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    public String importInstuctorData(List<InstrorInfo> instructorList, Boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(instructorList) || instructorList.size() == 0) {
            throw new ServiceException("导入辅导员数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (InstrorInfo instrorInfo : instructorList) {
            try {
                //选择更新 先判断是否存在，不更新也要判断是否传值
                // 验证是否存在这个sno
                int u = instrorInfoMapper.checkId(instrorInfo.getInstructorId());
                if (isUpdateSupport) {
                    if (u != 1) {
                        //执行插入
                            doInsert(instrorInfo, operName);
                            successNum++;
                            successMsg.append("<br/>" + successNum + "、辅导员 " + instrorInfo.getInstructorName() + " 导入成功");
                    } else {
                        //执行更新
                        BeanValidators.validateWithException(validator, instrorInfo);
                        instrorInfo.setUpdateBy(operName);
                        //学院、专业id的处理
                        Long colleaId = sysCollageMajorService.selectIdbyName(instrorInfo.getCollegeName());
                        instrorInfo.setCollegeId(colleaId);
                        //专业id的处理
                        String guideMajorIds = this.getMajorIds(instrorInfo);
                        instrorInfo.setGuideMajorIds(guideMajorIds);
                        this.updateInstrorInfo(instrorInfo);
                        successNum++;
                        successMsg.append("<br/>" + successNum + "、辅导员 " + instrorInfo.getInstructorName() + " 更新成功");
                    }
                } else {
                    //执行插入
                    if (u != 1) {
                        doInsert(instrorInfo, operName);
                        successNum++;
                        successMsg.append("<br/>" + successNum + "、辅导员 " + instrorInfo.getInstructorName() + " 导入成功");
                    } else {
                        failureNum++;
                        failureMsg.append("<br/>" + failureNum + "、职工号为 " + instrorInfo.getInstructorId() + "的辅导员" + instrorInfo.getInstructorName() + " 的信息已存在");

                    }
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、辅导员 " + instrorInfo.getInstructorName() + " 导入失败：";
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

    public String getMajorIds(InstrorInfo instrorInfo) {
        String[] majorNames = instrorInfo.getGuideMajorNames().split("，");
        //根据专业名称获取id
        Long[] majorIds = sysCollageMajorService.selectIdsByNames(majorNames);
        String guideMajorIds = StringUtils.join(majorIds, ",");
        return guideMajorIds;
    }

    public int doInsert(InstrorInfo instrorInfo, String operName) {
        BeanValidators.validateWithException(validator, instrorInfo);
        instrorInfo.setCreateBy(operName);
        //学院、专业id的处理
        Long colleaId = sysCollageMajorService.selectIdbyName(instrorInfo.getCollegeName());
        instrorInfo.setCollegeId(colleaId);
        //专业id的处理
        String guideMajorIds = this.getMajorIds(instrorInfo);
        instrorInfo.setGuideMajorIds(guideMajorIds);
        return (this.insertInstrorInfo(instrorInfo));
    }
}
