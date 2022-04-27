import request from '@/utils/request'
import { parseStrEmpty } from "@/utils/ruoyi";


// 查询成绩信息列表
export function listInfo(query) {
  return request({
    url: '/grade/info/list',
    method: 'get',
    params: query
  })
}

// 查询成绩信息详细
export function getGradeInfo(gradeId) {
  return request({
    url: '/grade/info/' + gradeId,
    method: 'get'
  })
}

// 新增成绩信息
export function addInfo(data) {
  return request({
    url: '/grade/info',
    method: 'post',
    data: data
  })
}

// 修改成绩信息
export function updateInfo(data) {
  return request({
    url: '/grade/info',
    method: 'put',
    data: data
  })
}

// 删除成绩信息
export function delInfo(gradeId) {
  return request({
    url: '/grade/info/' + gradeId,
    method: 'delete'
  })
}
// 获取表头信息
export function getHeader(data) {
  return request({
    url: '/grade/info/getHeader',
    method: 'post',
    data:data
  })
}

// 获取专业、学年、学期
export function getParams(data) {
  return request({
    url: '/grade/info/getParams',
    method: 'post',
    data:data
  })
}

