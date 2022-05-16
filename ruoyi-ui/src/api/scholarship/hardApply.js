import request from '@/utils/request'

// 查询助学金申请列表
export function listApply(query) {
  return request({
    url: '/hard/info/list',
    method: 'get',
    params: query
  })
}

// 查询助学金申请详细
export function getApply(applyId) {
  return request({
    url: '/hard/info/' + applyId,
    method: 'get'
  })
}

// 新增助学金申请
export function addApply(data) {
  return request({
    url: '/hard/info',
    method: 'post',
    data: data
  })
}

// 修改助学金申请
export function updateApply(data) {
  return request({
    url: '/hard/info',
    method: 'put',
    data: data
  })
}

// 删除助学金申请
export function delApply(applyId) {
  return request({
    url: '/hard/info/' + applyId,
    method: 'delete'
  })
}

// 审核奖学金申请
export function checkApply(data) {
  return request({
    url: '/hard/info/checkApply',
    method: 'post',
    data:data
  })
}
// 发布助学金的审核结果
export function publishApply(data) {
  return request({
    url: '/hard/info/publishApply',
    method: 'post',
    data:data
  })
}


// 统计该学年辅导员已审核通过的人数
export function getProveNum(params) {
  return request({
    url: '/hard/info/getProveNum',
    method: 'post',
    data:params
  })
}
