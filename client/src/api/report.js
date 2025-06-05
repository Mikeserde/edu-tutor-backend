import request from '@/utils/request'

/**
 * 获取教师工时统计报表
 * @param {string} startDate 开始日期 (YYYY-MM-DD)
 * @param {string} endDate 结束日期 (YYYY-MM-DD)
 * @returns Promise
 */
export function getTeacherHoursReport(startDate, endDate) {
  return request({
    url: '/reports/teacher-hours',
    method: 'get',
    params: {
      startDate,
      endDate
    }
  })
}

/**
 * 获取职业需求统计报表
 * @returns Promise
 */
export function getOccupationDemandReport() {
  return request({
    url: '/reports/occupation-demand',
    method: 'get'
  })
}