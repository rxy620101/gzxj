package com.ruoyi.scholarShip.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.scholarShip.mapper.HardApplyMapper;
import com.ruoyi.scholarShip.domain.HardApply;
import com.ruoyi.scholarShip.service.IHardApplyService;

/**
 * 助学金申请Service业务层处理
 *
 * @author raoxy
 * @date 2022-04-29
 */
@Service
public class HardApplyServiceImpl implements IHardApplyService {
    @Autowired
    private HardApplyMapper hardApplyMapper;

    /**
     * 查询助学金申请
     *
     * @param applyId 助学金申请主键
     * @return 助学金申请
     */
    @Override
    public HardApply selectHardApplyByApplyId(Long applyId) {
        return hardApplyMapper.selectHardApplyByApplyId(applyId);
    }

    /**
     * 查询助学金申请列表
     *
     * @param hardApply 助学金申请
     * @return 助学金申请
     */
    @Override
    public List<HardApply> selectHardApplyList(HardApply hardApply) {
        return hardApplyMapper.selectHardApplyList(hardApply);
    }

    /**
     * 新增助学金申请
     *
     * @param hardApply 助学金申请
     * @return 结果
     */
    @Override
    public int insertHardApply(HardApply hardApply) {
        return hardApplyMapper.insertHardApply(hardApply);
    }

    /**
     * 修改助学金申请
     *
     * @param hardApply 助学金申请
     * @return 结果
     */
    @Override
    public int updateHardApply(HardApply hardApply) {
        return hardApplyMapper.updateHardApply(hardApply);
    }

    /**
     * 批量删除助学金申请
     *
     * @param applyIds 需要删除的助学金申请主键
     * @return 结果
     */
    @Override
    public int deleteHardApplyByApplyIds(Long[] applyIds) {
        return hardApplyMapper.deleteHardApplyByApplyIds(applyIds);
    }

    /**
     * 删除助学金申请信息
     *
     * @param applyId 助学金申请主键
     * @return 结果
     */
    @Override
    public int deleteHardApplyByApplyId(Long applyId) {
        return hardApplyMapper.deleteHardApplyByApplyId(applyId);
    }

    //审核
    @Override
    public int checkApply(HardApply hardApply) {
        return hardApplyMapper.checkApply(hardApply);
    }

    //发布
    @Override
    public int publishApply(HardApply hardApply) {
        return hardApplyMapper.publishApply(hardApply);
    }
    //统计申请人数
    @Override
    public int selectTotalPeople(Map<String, Object> map){
        return  hardApplyMapper.selectTotalPeople(map);
    }
}
