import request from '@/utils/request'

// 获取排班列表
export function getOccupationsSchedules(params) {
  return request({
    url: '/occupation-schedules',
    method: 'get',
    params
  })
}

// 创建排班
export function createSchedule(data) {
  return request({
    url: '/occupation-schedules',
    method: 'post',
    data
  })
}

// 更新排班
export function updateSchedule(id, data) {
  return request({
    url: `/occupation-schedules/${id}`,
    method: 'put',
    data
  })
}

// 删除排班
export function deleteSchedule(id) {
  return request({
    url: `/occupation-schedules/${id}`,
    method: 'delete'
  })
}