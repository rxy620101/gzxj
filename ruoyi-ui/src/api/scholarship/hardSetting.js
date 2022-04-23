import request from '@/utils/request'

// 查询助学金参数设置列表
export function listHard(query) {
  return request({
    url: '/hardSetting/info/list',
    method: 'get',
    params: query
  })
}

// 查询助学金参数设置详细
export function getHard(settingId) {
  return request({
    url: '/hardSetting/info/' + settingId,
    method: 'get'
  })
}

// 新增助学金参数设置
export function addHard(data) {
  return request({
    url: '/hardSetting/info',
    method: 'post',
    data: data
  })
}

// 修改助学金参数设置
export function updateHard(data) {
  return request({
    url: '/hardSetting/info',
    method: 'put',
    data: data
  })
}

// 删除助学金参数设置
export function delHard(settingId) {
  return request({
    url: '/hardSetting/setting/' + settingId,
    method: 'delete'
  })
}
