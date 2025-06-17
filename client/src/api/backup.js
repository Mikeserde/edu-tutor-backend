import request from '@/utils/request'

// 创建备份，传备注
export function createBackup(comment) {
    return request({
        url: '/backup-records',
        method: 'post',
        params: { comment }  // 注意：用params传递query参数
    })
}

// 恢复备份，路径参数传递backupId
export function restoreBackup(backupId) {
    return request({
        url: `/backup-records/${backupId}`,
        method: 'put'
    })
}

// 清理备份，传保留数量和天数
export function cleanBackupData(keepCount, keepDays) {
    return request({
        url: '/backup-records',
        method: 'delete',
        params: { keepCount, keepDays }
    })
}

// 获取备份列表
export function listBackups() {
    return request({
        url: '/backup-records',
        method: 'get'
    })
}
