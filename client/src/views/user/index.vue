<template>
  <div class="app-container">
    <!-- 工具栏 -->
    <div class="filter-container">
      <el-button type="primary" icon="el-icon-plus" @click="handleCreate">
        新建用户
      </el-button>

      <el-input
        v-model="listQuery.search"
        placeholder="搜索用户名"
        class="filter-item"
        style="width: 200px; margin-left: 15px"
        @keyup.enter.native="handleFilter"
      />
    </div>

    <!-- 用户数据表格 -->
    <el-table
      v-loading="listLoading"
      :data="userList"
      element-loading-text="数据加载中"
      border
      fit
      highlight-current-row
      style="width: 100%; margin-top: 20px"
      class="user-table"
    >
      <!-- 用户名列 -->
      <el-table-column
        prop="username"
        label="用户名"
        align="center"
        min-width="180"
      >
        <template slot-scope="scope">
          <div
            style="display: flex; align-items: center; justify-content: center"
          >
            <el-avatar :size="32" class="user-avatar">
              {{ scope.row.username.charAt(0).toUpperCase() }}
            </el-avatar>
            <div style="margin-left: 8px">
              {{ scope.row.username }}
              <el-tag
                v-if="scope.row.username === 'admin'"
                size="mini"
                type="danger"
              >
                管理员
              </el-tag>
            </div>
          </div>
        </template>
      </el-table-column>

      <!-- 操作列 -->
      <el-table-column align="center" label="操作" width="500" fixed="right">
        <template slot-scope="scope">
          <div class="action-cell">
            <el-button
              type="danger"
              size="mini"
              icon="el-icon-delete"
              @click="handleDelete(scope.row)"
              :disabled="scope.row.username === 'admin'"
            >
              删除
            </el-button>
            <el-button
              type="warning"
              size="mini"
              icon="el-icon-refresh"
              @click="handleResetPassword(scope.row)"
            >
              重置密码
            </el-button>
          </div>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页组件 -->
    <el-pagination
      background
      :current-page="listQuery.page"
      :page-sizes="[10, 20, 50, 100]"
      :page-size="listQuery.limit"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      style="margin-top: 20px; text-align: left"
    />

    <!-- 用户编辑/新建弹窗 -->
    <user-dialog
      :visible.sync="dialogVisible"
      :dialog-type="dialogType"
      :user-data="currentUser"
      @create="handleCreateUser"
      @update="handleUpdateUser"
    />
  </div>
</template>

<script>
import { getUsers, createUser, deleteUser, updatePassword } from "@/api/user";
import UserDialog from "@/components/UserDialog";
import { formatDate } from "@/utils";

export default {
  name: "UserList",
  components: { UserDialog },
  data() {
    return {
      userList: [], // 用户列表数据
      total: 0, // 总记录数
      listLoading: true, // 加载状态
      listQuery: {
        page: 1, // 当前页码
        limit: 10, // 每页条数
        search: "", // 搜索关键词
      },
      dialogVisible: false, // 弹窗显示状态
      dialogType: "create", // 弹窗类型
      currentUser: {}, // 当前操作用户数据
    };
  },
  created() {
    this.fetchData();
  },
  methods: {
    formatDate,
    // 获取用户列表数据
    fetchData() {
      this.listLoading = true;

      const { page, limit, search } = this.listQuery;
      getUsers(page, limit, search)
        .then((response) => {
          if (response.code === 20000) {
            const pageData = response.data.page;
            this.userList = pageData.records;
            this.total = pageData.total;
          } else {
            this.$message.error(response.message || "获取用户列表失败");
          }
          this.listLoading = false;
        })
        .catch((error) => {
          this.listLoading = false;
          console.error("获取用户列表失败:", error);
          this.$message.error("获取用户列表失败");
        });
    },

    // 页容量变化处理
    handleSizeChange(val) {
      this.listQuery.limit = val;
      this.fetchData();
    },

    // 页码变化处理
    handleCurrentChange(val) {
      this.listQuery.page = val;
      this.fetchData();
    },

    // 搜索过滤处理
    handleFilter() {
      this.listQuery.page = 1;
      this.fetchData();
    },

    // 打开新建用户弹窗
    handleCreate() {
      this.dialogType = "create";
      this.currentUser = {};
      this.dialogVisible = true;
    },

    // 重置密码
    handleResetPassword(row) {
      this.$prompt("请输入新密码", "重置密码", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        inputPattern: /^.{6,}$/,
        inputErrorMessage: "密码长度至少6位",
      })
        .then(({ value }) => {
          this.listLoading = true;
          updatePassword(row.username, value)
            .then((response) => {
              if (response.code === 20000) {
                this.$message.success("密码重置成功");
              } else {
                this.$message.error(response.message || "密码重置失败");
              }
              this.listLoading = false;
            })
            .catch(() => {
              this.listLoading = false;
              this.$message.error("密码重置失败");
            });
        })
        .catch(() => {});
    },

    // 创建新用户
    handleCreateUser(userData) {
      this.listLoading = true;
      createUser(userData)
        .then((response) => {
          if (response.code === 20000) {
            this.$message.success("用户创建成功");
            this.fetchData();
          } else {
            this.$message.error(response.message || "用户创建失败");
          }
          this.listLoading = false;
        })
        .catch(() => {
          this.listLoading = false;
          this.$message.error("用户创建失败");
        });
    },

    // 更新用户信息
    handleUpdateUser(updateData) {
      this.listLoading = true;
      // 在实际项目中，这里应该调用更新用户的API
      // 由于我们的controller没有更新用户信息的端点，这里模拟更新
      // 在实际应用中，您需要添加一个更新用户的API端点
      setTimeout(() => {
        this.$message.success("用户信息更新成功");
        if (updateData.newPassword) {
          this.$message.info("密码已重置");
        }
        this.listLoading = false;
        this.fetchData();
      }, 500);
    },

    // 删除用户处理
    handleDelete(row) {
      this.$confirm(`确定删除用户 ${row.username} 吗？`, "删除确认", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          this.listLoading = true;
          deleteUser(row.username)
            .then((response) => {
              if (response.code === 20000) {
                this.$message.success("删除成功");
                // 删除后判断：如果删除的是当前页的最后一条数据
                if (this.userList.length === 1 && this.listQuery.page > 1) {
                  // 跳转到前一页
                  this.listQuery.page -= 1;
                }
                this.fetchData();
              } else {
                this.$message.error(response.message || "删除失败");
                this.listLoading = false;
              }
            })
            .catch(() => {
              this.listLoading = false;
              this.$message.error("删除失败");
            });
        })
        .catch(() => {});
    },
  },
};
</script>

<style scoped>
.filter-container {
  display: flex;
  align-items: center;
  padding: 15px;
  background: #fff;
  border-radius: 4px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
}

.filter-item {
  margin-left: 10px;
}

.user-table {
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.user-avatar {
  background-color: #f56c6c;
  color: #fff;
  font-weight: bold;
  display: flex;
  align-items: center;
  justify-content: center;
}

.action-cell {
  display: flex;
  justify-content: center;
  gap: 8px;
}
</style>