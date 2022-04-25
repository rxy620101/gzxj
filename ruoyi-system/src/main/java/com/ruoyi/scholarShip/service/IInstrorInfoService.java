package com.ruoyi.scholarShip.service;

import java.util.List;
import com.ruoyi.scholarShip.domain.InstrorInfo;


/**
 * 辅导员基础信息Service接口
 * 
 * @author raoxy
 * @date 2022-04-11
 */
public interface IInstrorInfoService 
{
    /**
     * 查询辅导员基础信息
     * 
     * @param id 辅导员基础信息主键
     * @return 辅导员基础信息
     */
    public InstrorInfo selectInstrorInfoById(Long id);

    /**
     * 查询辅导员基础信息列表
     * 
     * @param instrorInfo 辅导员基础信息
     * @return 辅导员基础信息集合
     */
    public List<InstrorInfo> selectInstrorInfoList(InstrorInfo instrorInfo);

    /**
     * 新增辅导员基础信息
     * 
     * @param instrorInfo 辅导员基础信息
     * @return 结果
     */
    public int insertInstrorInfo(InstrorInfo instrorInfo);

    /**
     * 修改辅导员基础信息
     * 
     * @param instrorInfo 辅导员基础信息
     * @return 结果
     */
    public int updateInstrorInfo(InstrorInfo instrorInfo);

    /**
     * 批量删除辅导员基础信息
     * 
     * @param ids 需要删除的辅导员基础信息主键集合
     * @return 结果
     */
    public int deleteInstrorInfoByIds(Long[] ids);

    /**
     * 删除辅导员基础信息信息
     * 
     * @param id 辅导员基础信息主键
     * @return 结果
     */
    public int deleteInstrorInfoById(Long id);
    /**
     * 根据学院id和指导年级查询辅导员
     *
     * @param collegeId
     * @param guideGrade
     * @return 结果
     */
    public List<InstrorInfo> selectByParams(Long collegeId,String guideGrade);

    /**
     * 校验职工号是否唯一
     *
     * @param id 职工号
     * @return 结果
     */
    public int checkId(String id);

    /**
     * 导入辅导员数据
     *
     * @param instructorList 用户数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    public String importInstuctorData(List<InstrorInfo> instructorList, Boolean isUpdateSupport, String operName);
}
