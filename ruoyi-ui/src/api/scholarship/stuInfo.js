import request from '@/utils/request'

// 查询学生基础信息列表
export function listInfo(query) {
  return request({
    url: '/scholarShip/stuInfo/list',
    method: 'get',
    params: query
  })
}

// 查询学生基础信息详细
export function getInfo(id) {
  return request({
    url: '/scholarShip/stuInfo/' + id,
    method: 'get'
  })
}

// 查询学生基础信息详细
export function getInfoBysno(sno) {
  return request({
    url: '/scholarShip/stuInfo/selBySno/' + sno,
    method: 'get'
  })
}

// 新增学生基础信息
export function addInfo(data) {
  return request({
    url: '/scholarShip/stuInfo',
    method: 'post',
    data: data
  })
}

// 修改学生基础信息
export function updateInfo(data) {
  return request({
    url: '/scholarShip/stuInfo',
    method: 'put',
    data: data
  })
}

// 删除学生基础信息
export function delInfo(id) {
  return request({
    url: '/scholarShip/stuInfo/' + id,
    method: 'delete'
  })
}

// 查询年级列表
export function selectGrade(majorId) {
  return request({
    url: '/scholarShip/stuInfo/selectGrade/' + majorId,
    method: 'get'
  })
}

// 查询班级列表
export function selectClass(grade) {
  return request({
    url: '/scholarShip/stuInfo/selectClass/' + grade,
    method: 'get'
  })
}

// 辅导员查询班级列表
export function selClassByMajorIds(majorId) {
  return request({
    url: '/scholarShip/stuInfo/selClassByMajorIds/' + majorId,
    method: 'get'
  })
}

// // 管理员查询班级列表
// export function selClass(majorId,grade) {
//   return request({
//     url: '/scholarShip/stuInfo/selClass/' + majorId+"/"+grade,
//     method: 'get'
//   })
// }

//检验sno是否唯一
export function checkSno(sno) {
  return request({
    url: '/scholarShip/stuInfo/checkSno/' + sno,
    method: 'get'
  })
}
