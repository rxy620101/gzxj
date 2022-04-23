package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.TreeSelect;
import com.ruoyi.common.core.domain.entity.SysColleageMajor;

import java.util.List;

/**
 * 部门管理 服务层
 * 
 * @author ruoyi
 */
public interface ISysColleageMajorService
{
    /**
     * 查询部门管理数据
     * 
     * @param dept 部门信息
     * @return 部门信息集合
     */
    public List<SysColleageMajor> selectDeptList(SysColleageMajor dept);

    /**
     * 构建前端所需要树结构
     * 
     * @param depts 部门列表
     * @return 树结构列表
     */
    public List<SysColleageMajor> buildDeptTree(List<SysColleageMajor> depts);

    /**
     * 构建前端所需要下拉树结构
     * 
     * @param depts 部门列表
     * @return 下拉树结构列表
     */
    public List<TreeSelect> buildDeptTreeSelect(List<SysColleageMajor> depts);

//    /**
//     * 根据角色ID查询部门树信息
//     *
//     * @param roleId 角色ID
//     * @return 选中部门列表
//     */
//    public List<Long> selectDeptListByRoleId(Long roleId);


    /**
     * 根据部门ID查询信息
     * 
     * @param deptId 部门ID
     * @return 部门信息
     */
    public SysColleageMajor selectDeptById(Long deptId);

    /**
     * 根据ID查询所有子部门（正常状态）
     * 
     * @param deptId 部门ID
     * @return 子部门数
     */
    public int selectNormalChildrenDeptById(Long deptId);

    /**
     * 是否存在部门子节点
     * 
     * @param deptId 部门ID
     * @return 结果
     */
    public boolean hasChildByDeptId(Long deptId);

    /**
     * 查询部门是否存在用户
     * 
     * @param deptId 部门ID
     * @return 结果 true 存在 false 不存在
     */
    public boolean checkDeptExistUser(Long deptId);

    /**
     * 校验部门名称是否唯一
     * 
     * @param dept 部门信息
     * @return 结果
     */
    public String checkDeptNameUnique(SysColleageMajor dept);

    /**
     * 校验部门是否有数据权限
     * 
     * @param deptId 部门id
     */
    public void checkDeptDataScope(Long deptId);

    /**
     * 新增保存部门信息
     * 
     * @param dept 部门信息
     * @return 结果
     */
    public int insertDept(SysColleageMajor dept);

    /**
     * 修改保存部门信息
     * 
     * @param dept 部门信息
     * @return 结果
     */
    public int updateDept(SysColleageMajor dept);

    /**
     * 删除部门管理信息
     * 
     * @param deptId 部门ID
     * @return 结果
     */
    public int deleteDeptById(Long deptId);

    /**
     * 根据名称查询专业id
     *
     * @param majorName 专业名称
     * @return 结果
     */
    public Long selectIdbyName(String majorName);

    /**
     * 根据名称查询专业id
     *
     * @param majorName 专业名称
     * @return 结果
     */
    public Long[] selectIdsByNames(String[] majorName);

    /**
     * 返回所有的二级部门
     *  @param  parentId  父id
     *   @return 结果
     */
    public List<SysColleageMajor> selectAllByParentId(Long parentId);

    /**
     *根据majorIds 查询专业名
     *  @param    majorIds
     *   @return 结果
     */
    public String[] selectMajorNames(Long[] majorIds);

    //返回所有的专业名称
    public List<SysColleageMajor>getAllMajor();
}
