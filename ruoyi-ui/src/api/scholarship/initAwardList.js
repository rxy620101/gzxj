import request from '@/utils/request'

// 查询奖学金初始名单列表
export function listInitAward(query) {
  return request({
    url: '/initAward/info/list',
    method: 'get',
    params: query
  })
}

// 查询奖学金初始名单详细
export function getInitAward(initId) {
  return request({
    url: '/initAward/info/' + initId,
    method: 'get'
  })
}

// 新增奖学金初始名单
export function addInitAward(data) {
  return request({
    url: '/initAward/info',
    method: 'post',
    data: data
  })
}

// 修改奖学金初始名单
export function updateInitAward(data) {
  return request({
    url: '/initAward/info',
    method: 'put',
    data: data
  })
}

// 删除奖学金初始名单
export function delInitAward(initId) {
  return request({
    url: '/initAward/info/' + initId,
    method: 'delete'
  })
}

// 获取学生成绩信息
export function putGradeInfo(data) {
  return request({
    url: '/initAward/info/getGradeInfo',
    method: 'post',
    data:data
  })
}
