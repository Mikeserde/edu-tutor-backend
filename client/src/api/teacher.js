import request from '@/utils/request'

/**
 * 获取教师分页列表
 * @param {Object} params - 查询参数
 * @param {number} [params.pageNum=1] - 当前页码
 * @param {number} [params.pageSize=10] - 每页大小
 * @param {string} [params.name] - 教师姓名（可选）
 * @returns {Promise} Promise对象
 */
export function getTeachers(params) {
  return request({
    url: '/teachers',
    method: 'get',
    params
  })
}

/**
 * 创建教师
 * @param {Object} data - 教师数据
 * @returns {Promise} Promise对象
 */
export function createTeacher(data) {
  return request({
    url: '/teachers',
    method: 'post',
    data
  })
}

/**
 * 更新教师信息
 * @param {Object} data - 教师数据（必须包含teacherId）
 * @returns {Promise} Promise对象
 */
export function updateTeacher(data) {
  return request({
    url: `/teachers/${data.teacherId}`,
    method: 'put',
    data
  })
}

/**
 * 根据ID获取教师详情
 * @param {number} id - 教师ID
 * @returns {Promise} Promise对象
 */
export function getTeacherById(id) {
  return request({
    url: `/teachers/${id}`,
    method: 'get'
  })
}

/**
 * 删除教师
 * @param {number} id - 教师ID
 * @returns {Promise} Promise对象
 */
export function deleteTeacher(id) {
  return request({
    url: `/teachers/${id}`,
    method: 'delete'
  })
}