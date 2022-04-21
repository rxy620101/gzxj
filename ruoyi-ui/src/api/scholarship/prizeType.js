import request from '@/utils/request'

// 查询奖项类别列表
export function listPrizetype(query) {
  return request({
    url: '/prizeType/info/list',
    method: 'get',
    params: query
  })
}

// 查询奖项类别详细
export function getPrizetype(typeId) {
  return request({
    url: '/prizeType/info/' + typeId,
    method: 'get'
  })
}

// 新增奖项类别
export function addPrizetype(data) {
  return request({
    url: '/prizeType/info',
    method: 'post',
    data: data
  })
}

// 修改奖项类别
export function updatePrizetype(data) {
  return request({
    url: '/prizeType/info',
    method: 'put',
    data: data
  })
}

// 删除奖项类别
export function delPrizetype(typeId) {
  return request({
    url: '/prizeType/info/' + typeId,
    method: 'delete'
  })
}
// 根据ParentId查询
export function selByParentId(parentId) {
  return request({
    url: '/prizeType/info/selByParentId/' + parentId,
    method: 'get'
  })
}

// 根据名字查询
export function checkByName(name) {
  return request({
    url: '/prizeType/info/checkByName',
    method: 'post',
    data:name,
  })
}

// 修改奖项状态
export function updateStatus(params) {
  return request({
    url: '/prizeType/info/updateStatus',
    method: 'post',
    data:params
  })
}

// 查询列表（排除节点）
export function listExcludeChild(typeId) {
  return request({
    url: '/prizeType/info/list/exclude/' + typeId,
    method: 'get'
  })
}
