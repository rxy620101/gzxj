import request from '@/utils/request'

// 查询课程列表
export function listCourse(query) {
  return request({
    url: '/system/course/list',
    method: 'get',
    params: query
  })
}

// 查询课程详细
export function getCourse(coseId) {
  return request({
    url: '/system/course/' + coseId,
    method: 'get'
  })
}

// 新增课程
export function addCourse(data) {
  return request({
    url: '/system/course',
    method: 'post',
    data: data
  })
}

// 修改课程
export function updateCourse(data) {
  return request({
    url: '/system/course',
    method: 'put',
    data: data
  })
}

// 删除课程
export function delCourse(params) {
  const coseIds=params.coseIds
  const collegeId=params.collegeId
  return request({
    url: '/system/course/del/'+coseIds+'/'+collegeId,
    method: 'get',
  })
}
// 用户状态修改
export function changeCourseStatus(coseId, status) {
  const data = {
    coseId,
    status
  }
  return request({
    url: '/system/course/changeStatus',
    method: 'put',
    data: data
  })
}

