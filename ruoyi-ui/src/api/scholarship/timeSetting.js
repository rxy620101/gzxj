import request from '@/utils/request'

// 查询申请时间参数列表
export function listSetting(query) {
  return request({
    url: '/timeSetting/info/list',
    method: 'get',
    params: query
  })
}

// 查询申请时间参数详细
export function getSetting(settingId) {
  return request({
    url: '/timeSetting/info/' + settingId,
    method: 'get'
  })
}

// 新增申请时间参数
export function addSetting(data) {
  return request({
    url: '/timeSetting/info',
    method: 'post',
    data: data
  })
}

// 修改申请时间参数
export function updateSetting(data) {
  return request({
    url: '/timeSetting/info',
    method: 'put',
    data: data
  })
}

// 删除申请时间参数
export function delSetting(settingId) {
  return request({
    url: '/timeSetting/info/' + settingId,
    method: 'delete'
  })
}

// 查询最新的时间参数 1 将 2 助
export function getAtLeast(setType) {
  return request({
    url: '/timeSetting/info/getAtLeast/'+setType,
    method: 'get'
  })
}
