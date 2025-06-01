<template>
  <div class="app-container">
    <!-- 工具栏 -->
    <div class="filter-container">
      <el-button type="primary" icon="el-icon-plus" @click="handleCreate">
        新建学生
      </el-button>

      <!-- 搜索区域 -->
      <el-input
        v-model="listQuery.name"
        placeholder="搜索学生姓名"
        class="filter-item"
        style="width: 200px; margin-left: 15px"
        @keyup.enter.native="handleFilter"
      />
    </div>

    <!-- 学生数据表格 -->
    <el-table
      v-loading="listLoading"
      :data="formattedList"
      element-loading-text="数据加载中"
      border
      fit
      highlight-current-row
      style="width: 100%; margin-top: 20px"
      class="student-table"
      ref="studentTable"
    >
      <!-- ID列 -->
      <el-table-column
        prop="studentId"
        align="center"
        label="ID"
        width="90"
        sortable
      />

      <!-- 姓名列 -->
      <el-table-column
        prop="name"
        label="姓名"
        align="center"
        min-width="120"
      />

      <!-- 性别列 -->
      <el-table-column prop="gender" label="性别" align="center" width="100">
        <template slot-scope="scope">
          <el-tag
            :type="scope.row.gender === '男' ? 'primary' : 'danger'"
            size="small"
          >
            {{ scope.row.gender }}
          </el-tag>
        </template>
      </el-table-column>

      <!-- 联系电话 -->
      <el-table-column
        prop="contactPhone"
        label="联系电话"
        align="center"
        min-width="150"
      >
        <template slot-scope="scope">
          {{ formatPhone(scope.row.contactPhone) }}
        </template>
      </el-table-column>

      <!-- 地址列 -->
      <el-table-column
        prop="address"
        label="地址"
        align="center"
        min-width="180"
        show-overflow-tooltip
      >
        <template slot-scope="scope">
          <el-tooltip
            effect="light"
            :content="scope.row.address"
            placement="top"
          >
            <span>{{ scope.row.address }}</span>
          </el-tooltip>
        </template>
      </el-table-column>

      <!-- 操作列 -->
      <el-table-column align="center" label="操作" width="180" fixed="right">
        <template slot-scope="scope">
          <div class="action-cell">
            <el-button
              type="primary"
              size="mini"
              icon="el-icon-edit"
              @click="handleEdit(scope.row)"
            >
              编辑
            </el-button>
            <el-button
              type="danger"
              size="mini"
              icon="el-icon-delete"
              @click="handleDelete(scope.row)"
            >
              删除
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

    <!-- 学生编辑/新建弹窗 -->
    <student-dialog
      :visible.sync="dialogVisible"
      :student-data="currentStudent"
      :dialog-type="dialogType"
      @refresh="handleRefresh"
    />
  </div>
</template>

<script>
import { getStudents, deleteStudent } from "@/api/student";
import StudentDialog from "@/components/StudentDialog";
import { formatPhone } from "@/utils";
export default {
  name: "StudentList",
  components: { StudentDialog },
  data() {
    return {
      rawList: [], // 原始数据
      total: 0, // 总记录数
      listLoading: true, // 加载状态
      listQuery: {
        page: 1, // 当前页码
        limit: 10, // 每页条数
        name: null, // 搜索姓名
        gender: null, // 性别筛选
      },
      dialogVisible: false, // 弹窗显示状态
      dialogType: "create", // 弹窗类型
      currentStudent: {}, // 当前操作的学生数据
    };
  },
  computed: {
    // 确保数据格式正确
    formattedList() {
      return this.rawList.map((student) => ({
        studentId: student.studentId,
        name: student.name,
        gender: student.gender,
        contactPhone: student.contactPhone,
        address: student.address,
      }));
    },
  },
  mounted() {
    // 添加窗口大小变化监听
    this.$nextTick(() => {
      window.addEventListener("resize", this.doLayout);
    });
  },
  beforeDestroy() {
    window.removeEventListener("resize", this.doLayout);
  },
  created() {
    this.fetchData();
  },
  methods: {
    formatPhone,
    // 刷新数据
    handleRefresh() {
      this.dialogVisible = false;
      this.fetchData();
    },

    // 触发表格重新布局
    doLayout() {
      this.$refs.studentTable.doLayout();
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

    // 打开新建学生弹窗
    handleCreate() {
      this.dialogType = "create";
      this.currentStudent = {};
      this.dialogVisible = true;
    },

    // 打开编辑学生弹窗
    handleEdit(row) {
      this.dialogType = "edit";
      this.currentStudent = { ...row };
      this.dialogVisible = true;
    },

    // 获取学生列表数据（带自动页码修复）
    fetchData() {
      this.listLoading = true;

      const params = {
        pageNum: this.listQuery.page,
        pageSize: this.listQuery.limit,
        name: this.listQuery.name,
        gender: this.listQuery.gender,
        grade: this.listQuery.grade,
      };

      getStudents(params)
        .then((response) => {
          if (response.code === 20000) {
            const pageData = response.data.page;

            // 计算总页数
            this.totalPages = Math.max(
              1, // 至少1页
              Math.ceil(pageData.total / this.listQuery.limit)
            );

            // 自动修复页码：如果请求的页码大于总页数，则跳转到最后一页
            if (this.listQuery.page > this.totalPages && this.totalPages > 0) {
              this.listQuery.page = this.totalPages;
              this.$nextTick(() => {
                this.fetchData();
              });
              return; // 提前返回，下一轮请求会再次执行
            }

            // 正常处理数据
            this.rawList = [...pageData.records];
            this.total = pageData.total;
            this.$nextTick(this.doLayout);
          } else {
            this.$message.error(response.message || "获取学生列表失败");
          }
          this.listLoading = false;
        })
        .catch((error) => {
          this.listLoading = false;
          console.error("获取学生列表失败:", error);
          this.$message.error("获取学生列表失败");
        });
    },

    // 删除学生处理（带页码优化）
    handleDelete(row) {
      this.$confirm(`确定删除学生 ${row.name} 吗？`, "删除确认", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          return deleteStudent(row.studentId);
        })
        .then((response) => {
          if (response.code === 20000) {
            this.$message.success("删除成功");

            // 删除后判断：如果删除的是当前页的最后一条数据
            if (this.rawList.length === 1 && this.listQuery.page > 1) {
              // 跳转到前一页
              this.listQuery.page -= 1;
            }

            // 重新加载数据
            this.fetchData();
          } else {
            this.$message.error(response.message || "删除失败");
          }
        })
        .catch((error) => {
          if (error !== "cancel") {
            console.error("删除失败:", error);
            this.$message.error("删除失败");
          }
        });
    },
  },
};
</script>

<style scoped>
/* 复用教师管理样式 */
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

.student-table {
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.el-table {
  width: 100% !important;
}

.el-table__body {
  width: 100% !important;
}

.action-cell {
  display: flex;
  justify-content: center;
  gap: 8px;
}

/* 地址单元格样式 */
.address-cell {
  max-width: 180px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
</style>