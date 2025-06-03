<template>
  <div class="app-container">
    <!-- 过滤区域 - 使用统一样式 -->
    <div class="filter-container">
      <el-button
        type="primary"
        icon="el-icon-plus"
        class="filter-item"
        @click="handleCreate"
      >
        新建职业注册
      </el-button>

      <!-- 筛选组 -->
      <div class="filter-group">
        <el-select
          v-model="listQuery.occupationTypeId"
          placeholder="选择职业类型"
          clearable
          class="filter-item"
          style="width: 200px; margin-left: 15px"
        >
          <el-option
            v-for="item in occupationTypeOptions"
            :key="item.occupationTypeId"
            :label="item.name"
            :value="item.occupationTypeId"
          />
        </el-select>

        <el-select
          v-model="listQuery.studentId"
          placeholder="选择学生"
          clearable
          class="filter-item"
          style="width: 220px; margin-left: 15px"
        >
          <el-option
            v-for="student in studentOptions"
            :key="student.studentId"
            :label="student.name"
            :value="student.studentId"
          />
        </el-select>

        <el-button
          type="primary"
          class="filter-item"
          style="margin-left: 15px"
          @click="handleFilter"
        >
          搜索
        </el-button>
      </div>
    </div>

    <!-- 数据表格 - 使用统一data-table样式 -->
    <el-table
      :data="formattedList"
      v-loading="listLoading"
      border
      fit
      highlight-current-row
      class="data-table"
      element-loading-text="数据加载中"
      style="width: 100%"
      ref="occupationTable"
    >
      <!-- 注册ID -->
      <el-table-column
        prop="occupationId"
        label="注册ID"
        min-width="100"
        align="center"
        sortable
      />

      <!-- 学生信息 -->
      <el-table-column label="学生信息" align="center" min-width="200">
        <template slot-scope="{ row }">
          <div class="student-info">
            <div class="student-name">{{ row.studentName }}</div>
            <div class="student-id">ID: {{ row.studentId }}</div>
          </div>
        </template>
      </el-table-column>

      <!-- 职业类型 -->
      <el-table-column label="职业类型" align="center" min-width="180">
        <template slot-scope="{ row }">
          <el-tag type="success" size="medium">
            {{ row.occupationTypeName }}
          </el-tag>
        </template>
      </el-table-column>

      <!-- 操作 -->
      <el-table-column
        label="操作"
        min-width="180"
        align="center"
        fixed="right"
      >
        <template slot-scope="{ row }">
          <el-button size="mini" type="primary" @click="handleEdit(row)">
            编辑
          </el-button>
          <el-button size="mini" type="danger" @click="handleDelete(row)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页组件 - 与教师管理页面统一 -->
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

    <!-- 新建/编辑对话框 -->
    <occupation-registration-dialog
      :visible.sync="dialogVisible"
      :dialog-type="dialogType"
      :registration-data="currentRegistration"
      :occupation-type-options="occupationTypeOptions"
      :student-options="studentOptions"
      @refresh="fetchData"
    />
  </div>
</template>

<script>
import {
  getOccupationRegistrations,
  getAllOccupationTypes,
  getAllStudents,
  deleteOccupationRegistration,
} from "@/api/occupationRegistration";
import OccupationRegistrationDialog from "@/components/OccupationRegistrationDialog";

export default {
  name: "OccupationRegistrationList",
  components: {
    OccupationRegistrationDialog,
  },
  data() {
    return {
      listLoading: false,
      dialogVisible: false, // 对话框显示状态
      dialogType: "create", // 对话框类型: create | edit
      currentRegistration: {}, // 当前编辑的注册数据
      rawList: [],
      total: 0,
      totalPages: 1,
      listQuery: {
        page: 1,
        limit: 10,
        occupationTypeId: null,
        studentId: null,
      },

      // 下拉框选项
      occupationTypeOptions: [],
      studentOptions: [],
    };
  },

  computed: {
    // 格式化的列表数据（添加名称信息）
    formattedList() {
      return this.rawList.map((item) => ({
        ...item,
        studentName: this.getStudentName(item.studentId),
        occupationTypeName: this.getOccupationTypeName(item.occupationTypeId),
      }));
    },
  },

  mounted() {
    // 初始加载所有数据
    this.loadOptions();
    this.fetchData();
    // 添加窗口大小监听
    window.addEventListener("resize", this.doLayout);
  },

  beforeDestroy() {
    window.removeEventListener("resize", this.doLayout);
  },

  methods: {
    // 触发表格重新布局
    doLayout() {
      this.$refs.occupationTable.doLayout();
    },

    // 加载所有选项数据
    async loadOptions() {
      try {
        // 加载职业类型
        const typeRes = await getAllOccupationTypes();
        if (typeRes.code === 20000 && typeRes.data && typeRes.data.data) {
          this.occupationTypeOptions = typeRes.data.data;
        }

        // 加载学生列表
        const studentRes = await getAllStudents();
        if (
          studentRes.code === 20000 &&
          studentRes.data &&
          studentRes.data.data
        ) {
          this.studentOptions = studentRes.data.data;
        }
      } catch (error) {
        console.error("加载选项数据失败:", error);
      }
    },

    // 根据学生ID获取学生名称
    getStudentName(studentId) {
      const student = this.studentOptions.find(
        (s) => s.studentId === studentId
      );
      return student ? student.name : `未知学生 (ID: ${studentId})`;
    },

    // 根据职业类型ID获取类型名称
    getOccupationTypeName(typeId) {
      const type = this.occupationTypeOptions.find(
        (t) => t.occupationTypeId === typeId
      );
      return type ? type.name : `未知类型 (ID: ${typeId})`;
    },

    // 核心数据获取方法
    async fetchData() {
      this.listLoading = true;

      const params = {
        pageNum: this.listQuery.page,
        pageSize: this.listQuery.limit,
        occupationTypeId: this.listQuery.occupationTypeId,
        studentId: this.listQuery.studentId,
      };

      try {
        const response = await getOccupationRegistrations(params);

        if (response.code !== 20000) {
          throw new Error(response.message || "获取数据失败");
        }

        // 确保数据结构正确
        const pageData = response.data?.page || {};
        this.total = pageData.total || 0;
        this.rawList = Array.isArray(pageData.records) ? pageData.records : [];
        this.totalPages =
          pageData.pages ||
          Math.max(1, Math.ceil(this.total / this.listQuery.limit));

        // 页码修正
        if (this.listQuery.page > this.totalPages && this.totalPages > 0) {
          this.listQuery.page = this.totalPages;
          // 重新加载数据
          await this.fetchData();
        }
      } catch (error) {
        console.error("获取注册列表失败:", error);
        this.$message.error("获取数据失败: " + (error.message || "请稍后重试"));
      } finally {
        this.listLoading = false;
        this.$nextTick(() => {
          this.doLayout();
        });
      }
    },

    // 页大小变化
    handleSizeChange(val) {
      this.listQuery.limit = val;
      this.listQuery.page = 1; // 重置到第一页
      this.fetchData();
    },

    // 页码变化
    handleCurrentChange(val) {
      this.listQuery.page = val;
      this.fetchData();
    },

    // 搜索/过滤
    handleFilter() {
      this.listQuery.page = 1;
      this.fetchData();
    },

    // 新建注册
    handleCreate() {
      this.dialogType = "create";
      this.currentRegistration = {}; // 清空当前数据
      this.dialogVisible = true; // 打开对话框
    },

    // 编辑
    handleEdit(row) {
      this.dialogType = "edit";
      // 复制当前行数据并传递给对话框
      this.currentRegistration = { ...row };
      this.dialogVisible = true; // 打开对话框
    },

    handleDelete(row) {
      this.$confirm(`确定删除职业注册 ${row.occupationId} 吗？`, "删除确认", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          return deleteOccupationRegistration(row.occupationId); // 调用删除API
        })
        .then((response) => {
          if (response.code === 20000) {
            this.$message.success("删除成功");
            // 删除后判断：如果删除的是当前页的最后一条数据
            if (this.rawList.length === 1 && this.listQuery.page > 1) {
              // 跳转到前一页
              this.listQuery.page -= 1;
            }
            this.fetchData(); // 刷新列表
          } else {
            this.$message.error(response.message || "删除失败");
          }
        })
        .catch((error) => {
          // 只有当不是用户取消操作时才报错
          if (error && error !== "cancel") {
            console.error("删除失败:", error);
            this.$message.error(
              "删除失败: " + (error.message || "请联系管理员")
            );
          }
        });
    },
  },
};
</script>

<style scoped>
/* 整体布局 - 与教师管理页面完全一致 */
.app-container {
  padding: 20px;
}

.breadcrumb {
  margin-bottom: 20px;
}

/* 搜索区样式统一 */
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
  margin-right: 10px;
}

/* 内部筛选组 */
.filter-group {
  display: flex;
  margin-left: auto;
  flex-wrap: wrap;
}

/* 表格样式统一 */
.data-table {
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  margin-top: 0;
}

/* 解决表格空白问题 */
.el-table {
  width: 100% !important;
}

.el-table__body {
  width: 100% !important;
}

/* 学生信息样式 */
.student-info {
  line-height: 1.5;
  text-align: center;
  padding: 5px 0;
}

.student-name {
  font-weight: bold;
  color: #333;
  font-size: 14px;
}

.student-id {
  font-size: 12px;
  color: #666;
}

/* 分页左对齐 */
.el-pagination {
  justify-content: flex-start !important;
}
</style>