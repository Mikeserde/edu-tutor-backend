import request from '@/utils/request'

/**
 * 获取职业类型分页列表
 * @param {Object} params - 查询参数
 * @param {number} [params.pageNum=1] - 当前页码
 * @param {number} [params.pageSize=10] - 每页大小
 * @param {string} [params.name] - 职业类型名称（可选）
 * @returns {Promise} Promise对象
 */
export function getOccupationTypes(params) {
  return request({
    url: '/occupation-types',
    method: 'get',
    params
  })
}

/**
 * 创建职业类型
 * @param {Object} data - 职业类型数据
 * @returns {Promise} Promise对象
 */
export function createOccupationType(data) {
  return request({
    url: '/occupation-types',
    method: 'post',
    data
  })
}

/**
 * 更新职业类型信息
 * @param {Object} data - 职业类型数据（必须包含occupationTypeId）
 * @returns {Promise} Promise对象
 */
export function updateOccupationType(data) {
  return request({
    url: `/occupation-types/${data.occupationTypeId}`,
    method: 'put',
    data
  })
}

/**
 * 根据ID获取职业类型详情
 * @param {number} id - 职业类型ID
 * @returns {Promise} Promise对象
 */
export function getOccupationTypeById(id) {
  return request({
    url: `/occupation-types/${id}`,
    method: 'get'
  })
}

/**
 * 删除职业类型
 * @param {number} id - 职业类型ID
 * @returns {Promise} Promise对象
 */
export function deleteOccupationType(id) {
  return request({
    url: `/occupation-types/${id}`,
    method: 'delete'
  })
}