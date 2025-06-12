import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/user/login',
    method: 'post',
    data
  })
}

export function getInfo(token) {
  return request({
    url: '/user/info',
    method: 'get',
    params: { token }
  })
}

export function logout() {
  return request({
    url: '/user/logout',
    method: 'post'
  })
}


export function register(data) {
  return request({
    url: '/user/register',
    method: 'post',
    data
  })
}

export function getUsers(page = 1, limit = 10, search = '') {
  return request({
    url: '/user',
    method: 'get',
    params: { page, limit, search }
  })
}

export function createUser(data) {
  return request({
    url: '/user/register',
    method: 'post',
    data
  })
}

export function deleteUser(username) {
  return request({
    url: `/user/${username}`,
    method: 'delete'
  })
}

export function updatePassword(username, newPassword) {
  return request({
    url: `/user/${username}/password`,
    method: 'put',
    params: { newPassword }
  })
}
