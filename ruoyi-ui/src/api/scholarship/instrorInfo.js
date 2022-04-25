import request from '@/utils/request'
import { parseStrEmpty } from "@/utils/ruoyi";

// 查询辅导员基础信息列表
export function listInfo(query) {
  return request({
    url: '/instructor/info/list',
    method: 'get',
    params: query
  })
}

// 查询辅导员基础信息详细
export function getInfo(id) {
  return request({
    url: '/instructor/info/' + parseStrEmpty(id),
    method: 'get'
  })
}

// 新增辅导员基础信息
export function addInfo(data) {
  return request({
    url: '/instructor/info',
    method: 'post',
    data: data
  })
}

// 修改辅导员基础信息
export function updateInfo(data) {
  return request({
    url: '/instructor/info',
    method: 'put',
    data: data
  })
}

// 删除辅导员基础信息
export function delInfo(id) {
  return request({
    url: '/instructor/info/' + id,
    method: 'delete'
  })
}

// 检查辅导员工号是否唯一
export function checkUnique(instructorId) {
  return request({
    url: '/instructor/info/checkId/' + instructorId,
    method: 'get'
  })
}

// 根据学院和指导年级查询
export function selectByParams(collegeId,guideGrade) {
  return request({
    url: '/instructor/info/selectByParams/' + collegeId+"/"+guideGrade,
    method: 'get'
  })
}


