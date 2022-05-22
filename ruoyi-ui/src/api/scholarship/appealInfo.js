import request from '@/utils/request'

// 查询成绩申诉列表
export function listInfo(query) {
  return request({
    url: '/appeal/info/list',
    method: 'get',
    params: query
  })
}

// 查询成绩申诉详细
export function getDetail(appealId) {
  return request({
    url: '/appeal/info/' + appealId,
    method: 'get'
  })
}

// 新增成绩申诉
export function addAppeal(data) {
  return request({
    url: '/appeal/info',
    method: 'post',
    data: data
  })
}

// 审核成绩申诉
export function checkAppeal(data) {
  return request({
    url: '/appeal/info/checkAppealInfo',
    method: 'post',
    data: data
  })
}


// 查询是否存在申诉
export function isExistAppeal(data) {
  return request({
    url: '/appeal/info/isExistAppeal',
    method: 'post',
    data: data
  })
}
