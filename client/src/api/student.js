import request from '@/utils/request'

/**
 * 获取学生分页列表
 * @param {Object} params - 查询参数
 * @param {number} [params.pageNum=1] - 当前页码
 * @param {number} [params.pageSize=10] - 每页大小
 * @param {string} [params.name] - 学生姓名（可选）
 * @param {string} [params.gender] - 学生性别（可选）
 * @param {number} [params.grade] - 学生年级（可选）
 * @returns {Promise} Promise对象
 */
export function getStudents(params) {
  return request({
    url: '/students',
    method: 'get',
    params
  })
}

/**
 * 创建学生
 * @param {Object} data - 学生数据
 * @returns {Promise} Promise对象
 */
export function createStudent(data) {
  return request({
    url: '/students',
    method: 'post',
    data
  })
}

/**
 * 更新学生信息
 * @param {Object} data - 学生数据（必须包含studentId）
 * @returns {Promise} Promise对象
 */
export function updateStudent(data) {
  return request({
    url: `/students/${data.studentId}`,
    method: 'put',
    data
  })
}

/**
 * 根据ID获取学生详情
 * @param {number} id - 学生ID
 * @returns {Promise} Promise对象
 */
export function getStudentById(id) {
  return request({
    url: `/students/${id}`,
    method: 'get'
  })
}

/**
 * 删除学生
 * @param {number} id - 学生ID
 * @returns {Promise} Promise对象
 */
export function deleteStudent(id) {
  return request({
    url: `/students/${id}`,
    method: 'delete'
  })
}