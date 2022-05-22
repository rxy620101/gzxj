package com.ruoyi.scholarShip.mapper;

import com.ruoyi.scholarShip.domain.AppealInfo;

import java.util.List;

public interface AppealInfoMapper {
    /**
     * 查询成绩申诉
     *
     * @param appealId 成绩申诉主键
     * @return 成绩申诉
     */
    public AppealInfo selectAppealInfoByAppealId(Long appealId);

    /**
     * 查询成绩申诉列表
     *
     * @param appealInfo 成绩申诉
     * @return 成绩申诉集合
     */
    public List<AppealInfo> selectAppealInfoList(AppealInfo appealInfo);

    /**
     * 新增成绩申诉
     *
     * @param appealInfo 成绩申诉
     * @return 结果
     */
    public int insertAppealInfo(AppealInfo appealInfo);

    /**
     * 审核成绩申诉
     *
     * @param appealInfo 成绩申诉
     * @return 结果
     */
    public int checkAppealInfo(AppealInfo appealInfo);

    /**
     * 查询是否存在成绩申诉
     *
     * @param appealInfo 成绩申诉
     * @return 结果
     */
    public AppealInfo  isExistAppeal(AppealInfo appealInfo);

}
