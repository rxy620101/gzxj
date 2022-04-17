package com.ruoyi.scholarShip.mapper;

import java.util.List;
import com.ruoyi.scholarShip.domain.InstrorInfo;

/**
 * 辅导员基础信息Mapper接口
 * 
 * @author raoxy
 * @date 2022-04-11
 */
public interface InstrorInfoMapper 
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
     * 删除辅导员基础信息
     * 
     * @param id 辅导员基础信息主键
     * @return 结果
     */
    public int deleteInstrorInfoById(Long id);

    /**
     * 批量删除辅导员基础信息
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteInstrorInfoByIds(Long[] ids);

    /**
     * 校验职工号是否唯一
     *
     * @param id 职工号
     * @return 结果
     */
    public int checkId(String id);
}
