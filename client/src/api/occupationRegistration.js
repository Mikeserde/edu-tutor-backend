import request from '@/utils/request'

/**
 * 获取职业注册分页列表
 * @param {Object} params - 查询参数
 * @param {number} [params.pageNum=1] - 当前页码
 * @param {number} [params.pageSize=10] - 每页大小
 * @param {number} [params.occupationTypeId] - 职业类型ID（可选）
 * @param {number} [params.studentId] - 学生ID（可选）
 * @returns {Promise} Promise对象
 */
export function getOccupationRegistrations(params) {
  return request({
    url: '/occupation-registrations',
    method: 'get',
    params
  })
}

/**
 * 创建职业注册
 * @param {Object} data - 注册数据
 * @returns {Promise} Promise对象
 */
export function createOccupationRegistration(data) {
  return request({
    url: '/occupation-registrations',
    method: 'post',
    data
  })
}

/**
 * 更新职业注册信息
 * @param {Object} data - 注册数据（必须包含occupationId）
 * @returns {Promise} Promise对象
 */
export function updateOccupationRegistration(data) {
  return request({
    url: `/occupation-registrations/${data.occupationId}`,
    method: 'put',
    data
  })
}

/**
 * 根据ID获取职业注册详情
 * @param {number} id - 注册ID
 * @returns {Promise} Promise对象
 */
export function getOccupationRegistrationById(id) {
  return request({
    url: `/occupation-registrations/${id}`,
    method: 'get'
  })
}

/**
 * 删除职业注册
 * @param {number} id - 注册ID
 * @returns {Promise} Promise对象
 */
export function deleteOccupationRegistration(id) {
  return request({
    url: `/occupation-registrations/${id}`,
    method: 'delete'
  })
}

/**
 * 获取所有学生列表（用于下拉选择）
 * @returns {Promise} Promise对象
 */
export function getAllStudents() {
  return request({
    url: '/students/list-all', // 假设有获取所有学生的接口
    method: 'get'
  })
}

/**
 * 获取所有职业类型列表（用于下拉选择）
 * @returns {Promise} Promise对象
 */
export function getAllOccupationTypes() {
  return request({
    url: '/occupation-types/list-all', // 假设有获取所有职业类型的接口
    method: 'get'
  })
}