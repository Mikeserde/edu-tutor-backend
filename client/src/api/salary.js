import request from '@/utils/request'

// 获取工资分页列表
export function getSalaries(params) {
  return request({
    url: '/salaries',
    method: 'get',
    params
  })
}

// 获取工资详情
export function getSalaryDetail(teacherId, month) {
  return request({
    url: `/salaries/${teacherId}/${month}`,
    method: 'get'
  })
}