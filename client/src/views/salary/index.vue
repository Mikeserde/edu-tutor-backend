<template>
  <div class="app-container">
    <el-alert
      title="注意"
      type="info"
      :closable="false"
      description="工资数据由系统自动计算生成，无法手动修改"
      style="margin-bottom: 20px"
    />

    <!-- 搜索区域 - 使用统一filter-container样式 -->
    <div class="filter-container">
      <el-input
        v-model="listQuery.teacherId"
        placeholder="输入教师ID"
        clearable
        class="filter-item"
        style="width: 200px; margin-right: 10px"
        @keyup.enter.native="handleFilter"
      />

      <el-date-picker
        v-model="listQuery.month"
        type="month"
        placeholder="选择月份"
        format="yyyy-MM"
        value-format="yyyy-MM"
        class="filter-item"
        style="width: 200px; margin-right: 10px"
      />

      <el-button
        type="primary"
        icon="el-icon-search"
        class="filter-item"
        @click="handleFilter"
      >
        查询
      </el-button>
      <el-button
        icon="el-icon-refresh"
        class="filter-item"
        @click="resetFilter"
      >
        重置
      </el-button>
    </div>

    <!-- 数据表格 - 使用teacher-table样式 -->
    <el-table
      v-loading="listLoading"
      :data="list"
      element-loading-text="数据加载中"
      border
      fit
      highlight-current-row
      class="teacher-table"
      style="width: 100%; margin-top: 20px"
    >
      <el-table-column
        prop="teacherId"
        align="center"
        label="教师ID"
        min-width="90"
        sortable
      >
        <template slot-scope="{ row }">
          <span>{{ row.teacherId }}</span>
        </template>
      </el-table-column>

      <el-table-column label="月份" align="center" min-width="120">
        <template slot-scope="{ row }">
          <span>{{ row.month }}</span>
        </template>
      </el-table-column>

      <el-table-column label="总工时(小时)" align="center" min-width="150">
        <template slot-scope="{ row }">
          <span class="fee-cell">{{ row.totalHours.toFixed(2) }}</span>
        </template>
      </el-table-column>

      <el-table-column label="总金额(元)" align="center" min-width="150">
        <template slot-scope="{ row }">
          <span class="fee-cell">¥ {{ row.totalAmount.toFixed(2) }}</span>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页组件 - 使用统一样式 -->
    <el-pagination
      background
      :current-page="listQuery.pageNum"
      :page-sizes="[10, 20, 50, 100]"
      :page-size="listQuery.pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      style="margin-top: 20px; text-align: left"
    />
  </div>
</template>

<script>
import { getSalaries } from "@/api/salary";

export default {
  name: "SalaryManagement",
  data() {
    return {
      list: [], // 工资列表数据
      listLoading: false, // 加载状态
      total: 0, // 总记录数

      // 查询参数
      listQuery: {
        pageNum: 1,
        pageSize: 10,
        teacherId: null,
        month: null,
      },
    };
  },
  created() {
    this.fetchData();
  },
  methods: {
    // 获取工资数据
    fetchData() {
      this.listLoading = true;

      // 构建查询参数
      const params = { ...this.listQuery };

      getSalaries(params)
        .then((response) => {
          if (response.code === 20000) {
            this.list = response.data?.page?.records || [];
            this.total = response.data?.page?.total || 0;
          } else {
            this.$message.error(response.message || "获取数据失败");
          }
        })
        .catch((error) => {
          console.error("获取工资数据失败:", error);
          this.$message.error(
            "获取数据失败: " + (error.message || "请稍后重试")
          );
        })
        .finally(() => {
          this.listLoading = false;
        });
    },

    // 处理筛选
    handleFilter() {
      this.listQuery.pageNum = 1;
      this.fetchData();
    },

    // 重置筛选条件
    resetFilter() {
      this.listQuery = {
        pageNum: 1,
        pageSize: 10,
        teacherId: null,
        month: null,
      };
      this.fetchData();
    },

    // 每页数量改变
    handleSizeChange(val) {
      this.listQuery.pageSize = val;
      this.fetchData();
    },

    // 当前页码改变
    handleCurrentChange(val) {
      this.listQuery.pageNum = val;
      this.fetchData();
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

/* 表格样式统一 */
.teacher-table {
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

/* 金额单元格样式 */
.fee-cell {
  font-weight: 600;
  color: #f56c6c;
}

/* 分页左对齐 */
.el-pagination {
  justify-content: flex-start !important;
}
</style>