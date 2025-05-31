/**
 * Created by PanJiaChen on 16/11/18.
 */

/**
 * @param {string} path
 * @returns {Boolean}
 */
export function isExternal(path) {
  return /^(https?:|mailto:|tel:)/.test(path)
}

/**
 * @param {string} str
 * @returns {Boolean}
 */
export function validUsername(str) {
  // const valid_map = ['admin', 'editor']
  // return valid_map.indexOf(str.trim()) >= 0
  // 更宽松的验证：允许任何非空字符串
  return typeof str === 'string' && str.trim().length > 0;
}

export function validatePassword(str) {
  // 更宽松的验证：允许任何非空字符串
  return typeof str === 'string' && str.trim().length > 0;
}
