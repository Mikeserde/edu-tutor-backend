<template>
  <div class="app-container">
    <!-- 工具栏 -->
    <div class="filter-container">
      <el-button type="primary" icon="el-icon-plus" @click="handleCreate">
        新建教师
      </el-button>

      <el-input
        v-model="listQuery.name"
        placeholder="搜索教师姓名"
        class="filter-item"
        style="width: 200px; margin-left: 15px"
        @keyup.enter.native="handleFilter"
      />
    </div>

    <!-- 教师数据表格 - 优化后的自适应版本 -->
    <el-table
      v-loading="listLoading"
      :data="formattedList"
      element-loading-text="数据加载中"
      border
      fit
      highlight-current-row
      style="width: 100%; margin-top: 20px"
      class="teacher-table"
      ref="teacherTable"
    >
      <!-- ID列自适应 -->
      <el-table-column
        prop="teacherId"
        align="center"
        label="ID"
        min-width="90"
        sortable
      >
        <template slot-scope="scope">
          <span>{{ scope.row.teacherId }}</span>
        </template>
      </el-table-column>

      <!-- 姓名列自适应 -->
      <el-table-column prop="name" label="姓名" align="center" min-width="120">
        <template slot-scope="scope">
          <span>{{ scope.row.name }}</span>
        </template>
      </el-table-column>

      <!-- 性别列自适应 -->
      <el-table-column
        prop="gender"
        label="性别"
        align="center"
        min-width="100"
      >
        <template slot-scope="scope">
          <el-tag
            :type="scope.row.gender === '男' ? 'primary' : 'danger'"
            size="small"
          >
            {{ scope.row.gender }}
          </el-tag>
        </template>
      </el-table-column>

      <!-- 联系方式自适应 -->
      <el-table-column
        prop="phone"
        label="联系方式"
        align="center"
        min-width="140"
      >
        <template slot-scope="scope">
          {{ formatPhone(scope.row.phone) }}
        </template>
      </el-table-column>

      <!-- 课时费自适应 -->
      <el-table-column
        prop="hourlyFee"
        label="课时费（元/小时）"
        align="center"
        min-width="150"
      >
        <template slot-scope="scope">
          <span class="fee-cell">¥ {{ scope.row.hourlyFee.toFixed(2) }}</span>
        </template>
      </el-table-column>

      <!-- 操作列固定宽度 -->
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

    <!-- 教师编辑/新建弹窗 -->
    <teacher-dialog
      :visible.sync="dialogVisible"
      :teacher-data="currentTeacher"
      :dialog-type="dialogType"
      @refresh="handleRefresh"
    />
  </div>
</template>

<script>
import { getTeachers, deleteTeacher } from "@/api/teacher";
import TeacherDialog from "@/components/TeacherDialog";

export default {
  name: "TeacherList",
  components: { TeacherDialog },
  data() {
    return {
      rawList: [], // 原始数据
      total: 0, // 总记录数
      listLoading: true, // 加载状态
      listQuery: {
        page: 1, // 当前页码
        limit: 10, // 每页条数
        name: null, // 搜索姓名
      },
      dialogVisible: false, // 弹窗显示状态
      dialogType: "create", // 弹窗类型
      currentTeacher: {}, // 当前操作的教师数据
    };
  },
  computed: {
    // 确保数据格式正确
    formattedList() {
      return this.rawList.map((teacher) => ({
        teacherId: teacher.teacherId,
        name: teacher.name,
        gender: teacher.gender,
        phone: teacher.phone,
        // 确保hourlyFee为数字类型
        hourlyFee:
          typeof teacher.hourlyFee === "number"
            ? teacher.hourlyFee
            : parseFloat(teacher.hourlyFee) || 0,
      }));
    },
  },
  mounted() {
    // 如果表格渲染后仍然有空白区域，可手动触发重排
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
    // 处理刷新和关闭弹窗的方法
    handleRefresh() {
      this.dialogVisible = false; // 关闭弹窗
      this.fetchData(); // 刷新数据
    },
    // 触发表格重新布局
    doLayout() {
      this.$refs.teacherTable.doLayout();
    },

    // 格式化手机号显示
    formatPhone(phone) {
      if (!phone) return "";
      return phone.replace(/(\d{3})(\d{4})(\d{4})/, "$1 $2 $3");
    },

    // 获取教师列表数据
    fetchData() {
      this.listLoading = true;

      const params = {
        pageNum: this.listQuery.page,
        pageSize: this.listQuery.limit,
        name: this.listQuery.name,
      };

      getTeachers(params)
        .then((response) => {
          if (response.code === 20000) {
            // 重要：使用深度拷贝确保Vue检测到变化
            this.rawList = [...response.data.page.records];
            this.total = response.data.page.total;
            // 数据加载后重新布局表格
            this.$nextTick(() => {
              this.doLayout();
            });
          } else {
            // 使用后端返回的message信息
            this.$message.error(response.message || "获取教师列表失败");
          }
          this.listLoading = false;
        })
        .catch((error) => {
          this.listLoading = false;
          console.error("获取教师列表失败:", error);
          this.$message.error("获取教师列表失败");
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

    // 打开新建教师弹窗
    handleCreate() {
      this.dialogType = "create";
      this.currentTeacher = {};
      this.dialogVisible = true;
    },

    // 打开编辑教师弹窗
    handleEdit(row) {
      this.dialogType = "edit";
      this.currentTeacher = { ...row };
      this.dialogVisible = true;
    },

    // 删除教师处理
    handleDelete(row) {
      this.$confirm(`确定删除教师 ${row.name} 吗？`, "删除确认", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          return deleteTeacher(row.teacherId);
        })
        .then((response) => {
          // 这里也要使用20000状态码
          if (response.code === 20000) {
            this.$message.success("删除成功");
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

.teacher-table {
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

/* 解决表格空白问题 */
.el-table {
  width: 100% !important;
}

.el-table__body {
  width: 100% !important;
}

.el-table::after {
  display: none;
}

.el-table::before {
  display: none;
}

/* 确保表格内容完全显示 */
.el-table .cell {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.fee-cell {
  font-weight: 600;
  color: #f56c6c;
}

.action-cell {
  display: flex;
  justify-content: center;
  gap: 8px;
}
</style>