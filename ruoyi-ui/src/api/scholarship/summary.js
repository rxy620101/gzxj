import request from '@/utils/request'

// 统计奖学金申请列表
export function summaryAward(params) {
  return request({
    url: '/summary/info/totalAward',
    method: 'post',
    data: params
  })
}

// 统计助学金申请
export function summaryHard(params) {
  return request({
    url: '/summary/info/totalHard',
    method: 'post',
    data:params
  })
}

// 传递map
export function getParams(params) {
  return request({
    url: '/summary/info/getParams',
    method: 'post',
    data:params
  })
}
