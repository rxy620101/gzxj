import request from '@/utils/request'

// 查询学生所获奖项列表
export function listPrizes(query) {
  return request({
    url: '/stuPrizes/info/list',
    method: 'get',
    params: query
  })
}

// 查询学生所获奖项详细
export function getPrizes(awardId) {
  return request({
    url: '/stuPrizes/info/' + awardId,
    method: 'get'
  })
}

// 新增学生所获奖项
export function addPrizes(data) {
  return request({
    url: '/stuPrizes/info',
    method: 'post',
    data: data
  })
}

// 修改学生所获奖项
export function updatePrizes(data) {
  return request({
    url: '/stuPrizes/info',
    method: 'put',
    data: data
  })
}

// 删除学生所获奖项
export function delPrizes(awardId) {
  return request({
    url: '/stuPrizes/info/' + awardId,
    method: 'delete'
  })
}

//修改审核状态
export function updateByIds(params) {
  return request({
    url: '/stuPrizes/info/updateByIds' ,
    method: 'post',
    data:params
  })
}
