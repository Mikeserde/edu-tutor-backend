<template>
  <div class="app-container">
    <!-- 顶部导航和标题 -->
    <div class="page-header">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item :to="{ path: '/dashboard' }"
          >Dashboard</el-breadcrumb-item
        >
        <el-breadcrumb-item>职业注册管理</el-breadcrumb-item>
        <el-breadcrumb-item>职业注册列表</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <!-- 过滤区域 -->
    <div class="filter-container">
      <el-button type="primary" icon="el-icon-plus" @click="handleCreate">
        + 新建职业注册
      </el-button>

      <div class="filter-group">
        <el-select
          v-model="listQuery.occupationTypeId"
          placeholder="选择职业类型"
          clearable
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
          style="margin-left: 15px"
          @click="handleFilter"
        >
          搜索
        </el-button>
      </div>
    </div>

    <!-- 数据表格 -->
    <el-table
      :data="formattedList"
      v-loading="listLoading"
      border
      fit
      style="width: 100%"
    >
      <!-- 注册ID -->
      <el-table-column
        prop="occupationId"
        label="注册ID"
        width="120"
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
      <el-table-column label="操作" width="180" align="center" fixed="right">
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
      style="margin-top: 20px; text-align: right"
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
  },

  methods: {
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
.app-container {
  padding: 20px;
  background-color: #fff;
}

.page-header {
  margin-bottom: 20px;
}

.filter-container {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.filter-group {
  display: flex;
  margin-left: auto;
}

.student-info {
  line-height: 1.5;
}

.student-name {
  font-weight: bold;
  color: #333;
}

.student-id {
  font-size: 12px;
  color: #999;
}

.el-table {
  margin-top: 20px;
}
</style>