package com.ruoyi.system.mapper;

import com.ruoyi.common.core.domain.entity.SysColleageMajor;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 院系-专业Mapper接口
 *
 * @author raoxy
 * @date 2022-04-04
 */
public interface SysColleageMajorMapper {

    /**
     * 查询部门管理数据
     *
     * @param dept 部门信息
     * @return 部门信息集合
     */
    public List<SysColleageMajor> selectDeptList(SysColleageMajor dept);

//    /**
//     * 根据角色ID查询部门树信息
//     *
//     * @param roleId 角色ID
//     * @param deptCheckStrictly 部门树选择项是否关联显示
//     * @return 选中部门列表
//     */
//    public List<Long> selectDeptListByRoleId(@Param("roleId") Long roleId, @Param("deptCheckStrictly") boolean deptCheckStrictly);

    /**
     * 根据部门ID查询信息
     *
     * @param deptId 部门ID
     * @return 部门信息
     */
    public SysColleageMajor selectDeptById(Long deptId);

    /**
     * 根据ID查询所有子部门
     *
     * @param deptId 部门ID
     * @return 部门列表
     */
    public List<SysColleageMajor> selectChildrenDeptById(Long deptId);

    /**
     * 根据ID查询所有子部门（正常状态）
     *
     * @param deptId 部门ID
     * @return 子部门数
     */
    public int selectNormalChildrenDeptById(Long deptId);

    /**
     * 是否存在子节点
     *
     * @param deptId 部门ID
     * @return 结果
     */
    public int hasChildByDeptId(Long deptId);

    /**
     * 查询部门是否存在用户
     *
     * @param deptId 部门ID
     * @return 结果
     */
    public int checkDeptExistUser(Long deptId);

    /**
     * 校验部门名称是否唯一
     *
     * @param name 部门名称
     * @param parentId 父部门ID
     * @return 结果
     */
    public SysColleageMajor checkDeptNameUnique(@Param("name") String name, @Param("parentId") Long parentId);

    /**
     * 新增部门信息
     *
     * @param dept 部门信息
     * @return 结果
     */
    public int insertDept(SysColleageMajor dept);

    /**
     * 修改部门信息
     *
     * @param dept 部门信息
     * @return 结果
     */
    public int updateDept(SysColleageMajor dept);

    /**
     * 修改所在部门正常状态
     *
     * @param deptIds 部门ID组
     */
    public void updateDeptStatusNormal(Long[] deptIds);

    /**
     * 修改子元素关系
     *
     * @param depts 子元素
     * @return 结果
     */
    public int updateDeptChildren(@Param("depts") List<SysColleageMajor> depts);

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

    //返回学院名称
    public String selectCollegeName(Long collegeId);

}
