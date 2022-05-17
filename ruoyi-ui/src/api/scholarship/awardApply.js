import request from '@/utils/request'

// 查询奖学金申请列表
export function listApply(query) {
  return request({
    url: '/award/info/list',
    method: 'get',
    params: query
  })
}

// 查询奖学金申请详细
export function getApply(applyId) {
  return request({
    url: '/award/info/' + applyId,
    method: 'get'
  })
}

// 新增奖学金申请
export function addApply(data) {
  return request({
    url: '/award/info',
    method: 'post',
    data: data
  })
}

// 修改奖学金申请
export function updateApply(data) {
  return request({
    url: '/award/info',
    method: 'put',
    data: data
  })
}

// 删除奖学金申请
export function delApply(applyId) {
  return request({
    url: '/award/info/' + applyId,
    method: 'delete'
  })
}

// 审核奖学金申请
export function checkApply(data) {
  return request({
    url: '/award/info/checkApply',
    method: 'post',
    data:data
  })
}

// 发布奖学金的审核结果
export function publishApply(data) {
  return request({
    url: '/award/info/publishApply',
    method: 'post',
    data:data
  })
}

// 查询是否重复发布
export function checkIsApply(data) {
  return request({
    url: '/award/info/checkIsApply',
    method: 'post',
    data:data
  })
}
