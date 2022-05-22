package com.ruoyi.scholarShip.service.impl;

import com.ruoyi.scholarShip.domain.AppealInfo;
import com.ruoyi.scholarShip.mapper.AppealInfoMapper;
import com.ruoyi.scholarShip.service.IAppealInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class AppealInfoServiceImpl implements IAppealInfoService {
    @Autowired
    private AppealInfoMapper appealInfoMapper;

    /**
     * 查询成绩申诉
     *
     * @param appealId 成绩申诉主键
     * @return 成绩申诉
     */
    @Override
    public AppealInfo selectAppealInfoByAppealId(Long appealId)
    {
        return appealInfoMapper.selectAppealInfoByAppealId(appealId);
    }

    /**
     * 查询成绩申诉列表
     *
     * @param appealInfo 成绩申诉
     * @return 成绩申诉
     */
    @Override
    public List<AppealInfo> selectAppealInfoList(AppealInfo appealInfo)
    {
        return appealInfoMapper.selectAppealInfoList(appealInfo);
    }

    /**
     * 新增成绩申诉
     *
     * @param appealInfo 成绩申诉
     * @return 结果
     */
    @Override
    public int insertAppealInfo(AppealInfo appealInfo)
    {
        return appealInfoMapper.insertAppealInfo(appealInfo);
    }

    /**
     * 修改成绩申诉
     *
     * @param appealInfo 成绩申诉
     * @return 结果
     */
    @Override
    public int checkAppealInfo(AppealInfo appealInfo)
    {
        return appealInfoMapper.checkAppealInfo(appealInfo);
    }

    /**
     * 查询是否存在成绩申诉
     *
     * @param appealInfo 成绩申诉
     * @return 结果
     */
    @Override
    public AppealInfo  isExistAppeal(AppealInfo appealInfo){
        return  appealInfoMapper.isExistAppeal(appealInfo);
    }
}
