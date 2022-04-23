import request from '@/utils/request'

// 查询奖学金参数列表
export function listAward(query) {
  return request({
    url: '/award/info/list',
    method: 'get',
    params: query
  })
}

// 查询奖学金参数详细
export function getAward(settingId) {
  return request({
    url: '/award/info/' + settingId,
    method: 'get'
  })
}

// 新增奖学金参数
export function addAward(data) {
  return request({
    url: '/award/info',
    method: 'post',
    data: data
  })
}

// 修改奖学金参数
export function updateAward(data) {
  return request({
    url: '/award/info',
    method: 'put',
    data: data
  })
}

// 删除奖学金参数
export function delAward(settingId) {
  return request({
    url: '/award/info/' + settingId,
    method: 'delete'
  })
}
// 根据ParentId查询
export function selByParentId(parentId) {
  return request({
    url: '/award/info/selByParentId/' + parentId,
    method: 'get'
  })
}

// 根据名字查询
export function checkByName(name) {
  return request({
    url: '/award/info/checkByName',
    method: 'post',
    data:name,
  })
}
// 查询列表（排除节点）
export function listExcludeChild(settingId) {
  return request({
    url: '/award/info/list/exclude/' + settingId,
    method: 'get'
  })
}
