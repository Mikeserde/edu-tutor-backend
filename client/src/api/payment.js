import request from '@/utils/request'

/**
 * 分页查询支付记录
 * @param {Object} params 查询参数
 * @returns Promise
 */
export function getPaymentsPage(params) {
  return request({
    url: '/payments',
    method: 'get',
    params
  })
}

/**
 * 按职业ID查询支付记录
 * @param {Number} occupationId 职业ID
 * @returns Promise
 */
export function getByOccupation(occupationId) {
  return request({
    url: `/payments/by-occupation/${occupationId}`,
    method: 'get'
  })
}

/**
 * 检查支付记录是否存在
 * @param {Number} occupationId 职业ID
 * @param {String} date 查询日期 (YYYY-MM-DD)
 * @returns Promise
 */
export function checkPaymentExists(occupationId, date) {
  return request({
    url: '/payments/existence',
    method: 'get',
    params: {
      occupationId,
      date
    }
  })
}