import request from '@/utils/request'

// 查询贫困等级列表
export function listRank(query) {
  return request({
    url: '/hardRank/info/list',
    method: 'get',
    params: query
  })
}

// 查询贫困等级详细
export function getRank(rangkId) {
  return request({
    url: '/hardRank/info/' + rangkId,
    method: 'get'
  })
}

// 新增贫困等级
export function addRank(data) {
  return request({
    url: '/hardRank/info',
    method: 'post',
    data: data
  })
}

// 修改贫困等级
export function updateRank(data) {
  return request({
    url: '/hardRank/info/updateRank',
    method: 'post',
    data: data
  })
}

// 删除贫困等级
export function delRank(rangkId) {
  return request({
    url: '/hardRank/info/' + rangkId,
    method: 'delete'
  })
}
