<template>
  <div class="app-container">
    <el-card>
      <div style="margin-bottom: 20px;">
        <el-input
          v-model="backupComment"
          placeholder="请输入备份备注"
          style="width: 300px; margin-right: 10px;"
        />
        <el-button type="primary" icon="el-icon-download" @click="handleCreate">备份数据库</el-button>
        <el-button type="warning" icon="el-icon-delete" @click="handleClean">清理备份</el-button>
      </div>

      <el-table :data="backupList" border stripe>
        <el-table-column prop="backupId" label="编号" width="80" />
        <el-table-column prop="comment" label="备注" />
        <el-table-column
          prop="backupTime"
          label="创建时间"
          width="180"
          :formatter="formatDateTime"
        />
        <el-table-column label="操作" width="160">
          <template slot-scope="scope">
            <el-button size="mini" type="success" @click="handleRestore(scope.row.backupId)">恢复</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
import { createBackup, restoreBackup, cleanBackupData, listBackups } from '@/api/backup'
import { MessageBox, Message } from 'element-ui'

export default {
  name: 'DatabaseManagement',
  data() {
    return {
      backupList: [],
      backupComment: ''
    }
  },
  created() {
    this.fetchBackups()
  },
  methods: {
    fetchBackups() {
      listBackups().then(res => {
        this.backupList = res.data.backups || []
      }).catch(() => {
        Message.error('获取备份列表失败')
      })
    },
    formatDateTime(row, column, cellValue) {
      if (!cellValue) return ''
      const date = new Date(cellValue)
      return date.toLocaleString()
    },
    handleCreate() {
      if (!this.backupComment) {
        return Message.warning('请输入备份备注')
      }
      createBackup(this.backupComment).then(() => {
        Message.success('数据库备份成功')
        this.backupComment = ''
        this.fetchBackups()
      }).catch(() => {
        Message.error('数据库备份失败')
      })
    },
    handleRestore(backupId) {
      MessageBox.confirm('确认要恢复此备份吗？操作不可撤销！', '警告', {
        type: 'warning'
      }).then(() => {
        restoreBackup(backupId).then(() => {
          Message.success('数据库恢复成功')
        }).catch(() => {
          Message.error('数据库恢复失败')
        })
      }).catch(() => {
        // 取消恢复操作不处理
      })
    },
    handleClean() {
      MessageBox.prompt('请输入保留的备份数量和天数（格式：数量,天数）', '清理备份', {
        inputPattern: /^\d+,\d+$/,
        inputErrorMessage: '格式错误，应为数字,数字'
      }).then(({ value }) => {
        const [keepCount, keepDays] = value.split(',').map(Number)
        cleanBackupData(keepCount, keepDays).then(() => {
          Message.success('备份清理成功')
          this.fetchBackups()
        }).catch(() => {
          Message.error('备份清理失败')
        })
      }).catch(() => {
        // 取消清理操作不处理
      })
    }
  }
}
</script>
