package com.ruoyi.scholarShip.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.scholarShip.domain.GradeInfo;
import com.ruoyi.scholarShip.mapper.GradeInfoMapper;
import com.ruoyi.scholarShip.service.IGradeInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 成绩信息Service业务层处理
 * 
 * @author raoxy
 * @date 2022-04-16
 */
@Service
public class GradeInfoServiceImpl implements IGradeInfoService
{
    @Autowired
    private GradeInfoMapper gradeInfoMapper;

    /**
     * 查询成绩信息
     * 
     * @param sno 成绩信息主键
     * @return 成绩信息
     */
    @Override
    public GradeInfo selectGradeInfoByGradeId(String  sno)
    {
        return gradeInfoMapper.selectGradeInfoBysno(sno);
    }

    /**
     * 查询成绩信息列表
     * 
     * @param gradeInfo 成绩信息
     * @return 成绩信息
     */
    @Override
    public List<GradeInfo> selectGradeInfoList(GradeInfo gradeInfo)
    {
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
    public int insertGradeInfo(GradeInfo gradeInfo)
    {
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
    public int updateGradeInfo(GradeInfo gradeInfo)
    {
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
    public int deleteGradeInfoByGradeIds(Long[] gradeIds)
    {
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
    public int deleteGradeInfoByGradeId(Long gradeId)
    {

        return gradeInfoMapper.deleteGradeInfoByGradeId(gradeId);
    }

}
