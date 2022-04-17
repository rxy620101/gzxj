package com.ruoyi.system.service.impl;

import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.TreeSelect;
import com.ruoyi.common.core.domain.entity.SysColleageMajor;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.system.mapper.SysColleageMajorMapper;
import com.ruoyi.system.service.ISysColleageMajorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 部门管理 服务实现
 * 
 * @author ruoyi
 */
@Service
public class SysCollageMajorServiceImpl implements ISysColleageMajorService
{
    @Autowired
    private SysColleageMajorMapper deptMapper;

    /**
     * 查询部门管理数据
     * 
     * @param dept 部门信息
     * @return 部门信息集合
     */
    @Override
    @DataScope(deptAlias = "d")
    public List<SysColleageMajor> selectDeptList(SysColleageMajor dept)
    {
        return deptMapper.selectDeptList(dept);
    }

    /**
     * 构建前端所需要树结构
     * 
     * @param depts 部门列表
     * @return 树结构列表
     */
    @Override
    public List<SysColleageMajor> buildDeptTree(List<SysColleageMajor> depts)
    {
        List<SysColleageMajor> returnList = new ArrayList<SysColleageMajor>();
        List<Long> tempList = new ArrayList<Long>();
        for (SysColleageMajor dept : depts)
        {
            tempList.add(dept.getcollegeId());
        }
        for (SysColleageMajor dept : depts)
        {
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(dept.getParentId()))
            {
                recursionFn(depts, dept);
                returnList.add(dept);
            }
        }
        if (returnList.isEmpty())
        {
            returnList = depts;
        }
        return returnList;
    }

    /**
     * 构建前端所需要下拉树结构
     * 
     * @param depts 部门列表
     * @return 下拉树结构列表
     */
    @Override
    public List<TreeSelect> buildDeptTreeSelect(List<SysColleageMajor> depts)
    {
        List<SysColleageMajor> deptTrees = buildDeptTree(depts);
        return deptTrees.stream().map(TreeSelect::new).collect(Collectors.toList());
    }

//    /**
//     * 根据角色ID查询部门树信息
//     *
//     * @param roleId 角色ID
//     * @return 选中部门列表
//     */
//    @Override
//    public List<Long> selectDeptListByRoleId(Long roleId)
//    {
//        SysRole role = roleMapper.selectRoleById(roleId);
//        return deptMapper.selectDeptListByRoleId(roleId, role.isDeptCheckStrictly());
//    }

    /**
     * 根据部门ID查询信息
     * 
     * @param deptId 部门ID
     * @return 部门信息
     */
    @Override
    public SysColleageMajor selectDeptById(Long deptId)
    {
        return deptMapper.selectDeptById(deptId);
    }

    /**
     * 根据ID查询所有子部门（正常状态）
     * 
     * @param deptId 部门ID
     * @return 子部门数
     */
    @Override
    public int selectNormalChildrenDeptById(Long deptId)
    {
        return deptMapper.selectNormalChildrenDeptById(deptId);
    }

    /**
     * 是否存在子节点
     * 
     * @param deptId 部门ID
     * @return 结果
     */
    @Override
    public boolean hasChildByDeptId(Long deptId)
    {
        int result = deptMapper.hasChildByDeptId(deptId);
        return result > 0;
    }

    /**
     * 查询部门是否存在用户
     * 
     * @param deptId 部门ID
     * @return 结果 true 存在 false 不存在
     */
    @Override
    public boolean checkDeptExistUser(Long deptId)
    {
        int result = deptMapper.checkDeptExistUser(deptId);
        return result > 0;
    }

    /**
     * 校验部门名称是否唯一
     * 
     * @param dept 部门信息
     * @return 结果
     */
    @Override
    public String checkDeptNameUnique(SysColleageMajor dept)
    {
        Long deptId = StringUtils.isNull(dept.getcollegeId()) ? -1L : dept.getcollegeId();
        SysColleageMajor info = deptMapper.checkDeptNameUnique(dept.getName(), dept.getParentId());
        if (StringUtils.isNotNull(info) && info.getcollegeId().longValue() != deptId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验部门是否有数据权限
     * 
     * @param deptId 部门id
     */
    @Override
    public void checkDeptDataScope(Long deptId)
    {
        if (!SysUser.isAdmin(SecurityUtils.getUserId()))
        {
            SysColleageMajor dept = new SysColleageMajor();
            dept.setcollegeId(deptId);
            List<SysColleageMajor> depts = SpringUtils.getAopProxy(this).selectDeptList(dept);
            if (StringUtils.isEmpty(depts))
            {
                throw new ServiceException("没有权限访问部门数据！");
            }
        }
    }

    /**
     * 新增保存部门信息
     * 
     * @param dept 部门信息
     * @return 结果
     */
    @Override
    public int insertDept(SysColleageMajor dept)
    {
        SysColleageMajor info = deptMapper.selectDeptById(dept.getParentId());
        // 如果父节点不为正常状态,则不允许新增子节点
        if (!UserConstants.DEPT_NORMAL.equals(info.getStatus()))
        {
            throw new ServiceException("部门停用，不允许新增");
        }
        dept.setAncestors(info.getAncestors() + "," + dept.getParentId());
        return deptMapper.insertDept(dept);
    }

    /**
     * 修改保存部门信息
     * 
     * @param dept 部门信息
     * @return 结果
     */
    @Override
    public int updateDept(SysColleageMajor dept)
    {
        SysColleageMajor newParentDept = deptMapper.selectDeptById(dept.getParentId());
        SysColleageMajor oldDept = deptMapper.selectDeptById(dept.getcollegeId());
        if (StringUtils.isNotNull(newParentDept) && StringUtils.isNotNull(oldDept))
        {
            String newAncestors = newParentDept.getAncestors() + "," + newParentDept.getcollegeId();
            String oldAncestors = oldDept.getAncestors();
            dept.setAncestors(newAncestors);
            updateDeptChildren(dept.getcollegeId(), newAncestors, oldAncestors);
        }
        int result = deptMapper.updateDept(dept);
        if (UserConstants.DEPT_NORMAL.equals(dept.getStatus()) && StringUtils.isNotEmpty(dept.getAncestors())
                && !StringUtils.equals("0", dept.getAncestors()))
        {
            // 如果该部门是启用状态，则启用该部门的所有上级部门
            updateParentDeptStatusNormal(dept);
        }
        return result;
    }

    /**
     * 修改该部门的父级部门状态
     * 
     * @param dept 当前部门
     */
    private void updateParentDeptStatusNormal(SysColleageMajor dept)
    {
        String ancestors = dept.getAncestors();
        Long[] deptIds = Convert.toLongArray(ancestors);
        deptMapper.updateDeptStatusNormal(deptIds);
    }

    /**
     * 修改子元素关系
     * 
     * @param deptId 被修改的部门ID
     * @param newAncestors 新的父ID集合
     * @param oldAncestors 旧的父ID集合
     */
    public void updateDeptChildren(Long deptId, String newAncestors, String oldAncestors)
    {
        List<SysColleageMajor> children = deptMapper.selectChildrenDeptById(deptId);
        for (SysColleageMajor child : children)
        {
            child.setAncestors(child.getAncestors().replaceFirst(oldAncestors, newAncestors));
        }
        if (children.size() > 0)
        {
            deptMapper.updateDeptChildren(children);
        }
    }

    /**
     * 删除部门管理信息
     * 
     * @param deptId 部门ID
     * @return 结果
     */
    @Override
    public int deleteDeptById(Long deptId)
    {
        return deptMapper.deleteDeptById(deptId);
    }

    /**
     * 递归列表
     */
    private void recursionFn(List<SysColleageMajor> list, SysColleageMajor t)
    {
        // 得到子节点列表
        List<SysColleageMajor> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysColleageMajor tChild : childList)
        {
            if (hasChild(list, tChild))
            {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<SysColleageMajor> getChildList(List<SysColleageMajor> list, SysColleageMajor t)
    {
        List<SysColleageMajor> tlist = new ArrayList<SysColleageMajor>();
        Iterator<SysColleageMajor> it = list.iterator();
        while (it.hasNext())
        {
            SysColleageMajor n = (SysColleageMajor) it.next();
            if (StringUtils.isNotNull(n.getParentId()) && n.getParentId().longValue() == t.getcollegeId().longValue())
            {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<SysColleageMajor> list, SysColleageMajor t)
    {
        return getChildList(list, t).size() > 0;
    }


    /**
     * 根据名称查询专业id
     *
     * @param majorName 专业名称
     * @return 结果
     */
    @Override
    public Long selectIdbyName(String majorName){
        return deptMapper.selectIdbyName(majorName);
    }

    @Override
    /**
     * 根据名称查询专业id
     *
     * @param majorName 专业名称
     * @return 结果
     */
    public Long[] selectIdsByNames(String[] majorName){
        return deptMapper.selectIdsByNames(majorName);
    }
    /**
     * 返回所有的二级部门
     *  @param  parentId  父id
     *   @return 结果
     */
    @Override
    public List<SysColleageMajor> selectAllByParentId(Long parentId){
        return deptMapper.selectAllByParentId(parentId);
    }
    @Override
    /**
     *根据majorIds 查询专业名
     *  @param    majorIds
     *   @return 结果
     */
    public String[] selectMajorNames(Long[] majorIds){
        return deptMapper.selectMajorNames(majorIds);
    }

}
