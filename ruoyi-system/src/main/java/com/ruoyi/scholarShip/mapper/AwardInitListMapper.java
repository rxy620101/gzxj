package com.ruoyi.scholarShip.mapper;

import java.util.List;
import com.ruoyi.scholarShip.domain.AwardInitList;

/**
 * 奖学金初始名单Mapper接口
 * 
 * @author raoxy
 * @date 2022-04-26
 */
public interface AwardInitListMapper 
{
    /**
     * 查询奖学金初始名单
     * 
     * @param initId 奖学金初始名单主键
     * @return 奖学金初始名单
     */
    public AwardInitList selectAwardInitListByInitId(Long initId);

    /**
     * 查询奖学金初始名单列表
     * 
     * @param awardInitList 奖学金初始名单
     * @return 奖学金初始名单集合
     */
    public List<AwardInitList> selectAwardInitListList(AwardInitList awardInitList);

    /**
     * 新增奖学金初始名单
     * 
     * @param awardInitList 奖学金初始名单
     * @return 结果
     */
    public int insertAwardInitList(AwardInitList awardInitList);

    /**
     * 修改奖学金初始名单
     * 
     * @param awardInitList 奖学金初始名单
     * @return 结果
     */
    public int updateAwardInitList(AwardInitList awardInitList);

    /**
     * 删除奖学金初始名单
     * 
     * @param initId 奖学金初始名单主键
     * @return 结果
     */
    public int deleteAwardInitListByInitId(Long initId);

    /**
     * 批量删除奖学金初始名单
     * 
     * @param initIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAwardInitListByInitIds(Long[] initIds);
}
