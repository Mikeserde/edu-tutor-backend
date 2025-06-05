<template>
  <div class="app-container">
    <!-- 筛选区域 -->
    <div class="filter-container">
      <el-card shadow="never">
        <el-form
          ref="queryForm"
          :model="queryParams"
          :inline="true"
          class="filter-form"
        >
          <el-form-item label="统计范围" prop="dateRange">
            <el-date-picker
              v-model="dateRange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              value-format="yyyy-MM-dd"
              :picker-options="pickerOptions"
            />
          </el-form-item>

          <el-form-item>
            <el-button
              type="primary"
              icon="el-icon-s-data"
              :loading="loading"
              @click="handleQuery"
              >查询统计</el-button
            >
            <el-button icon="el-icon-refresh" @click="resetQuery"
              >重置</el-button
            >
          </el-form-item>
        </el-form>
      </el-card>
    </div>

    <!-- 统计卡片 -->
    <div class="stat-cards" v-if="reportData.length > 0">
      <el-row :gutter="20">
        <el-col :span="6">
          <stat-card
            title="总教师数"
            :value="reportData.length"
            icon="el-icon-user-solid"
            color="#1890ff"
          />
        </el-col>
        <el-col :span="6">
          <stat-card
            title="总课时数"
            :value="totalHours"
            icon="el-icon-timer"
            color="#36cbcb"
            unit="小时"
          />
        </el-col>
        <el-col :span="6">
          <stat-card
            title="平均课时"
            :value="averageHours"
            icon="el-icon-office-building"
            color="#faad14"
            unit="小时/人"
          />
        </el-col>
        <el-col :span="6">
          <stat-card
            title="最勤奋教师"
            :value="busiestTeacher.teacherName"
            :sub-value="`${busiestTeacher.totalHours}小时`"
            icon="el-icon-medal-1"
            color="#52c41a"
          />
        </el-col>
      </el-row>
    </div>

    <!-- 数据表格 -->
    <el-card shadow="never" style="margin-top: 20px">
      <el-table
        v-loading="loading"
        :data="paginatedData"
        border
        stripe
        fit
        highlight-current-row
        class="data-table"
        style="width: 100%"
        v-if="reportData.length > 0"
      >
        <el-table-column
          label="教师ID"
          prop="teacherId"
          align="center"
          min-width="100"
        />
        <el-table-column
          label="教师姓名"
          prop="teacherName"
          align="center"
          min-width="120"
        />
        <el-table-column label="总课时(小时)" align="center" min-width="120">
          <template slot-scope="{ row }">
            <span class="hours-value">{{ row.totalHours }}</span>
          </template>
        </el-table-column>
      </el-table>

      <!-- 空数据状态 -->
      <el-empty v-else description="暂无数据" class="no-data">
        <el-button type="primary" size="small" @click="resetQuery"
          >重置查询</el-button
        >
      </el-empty>

      <!-- 分页 -->
      <div class="table-pagination" v-if="reportData.length > pageSize">
        <el-pagination
          :current-page="currentPage"
          :page-size="pageSize"
          :total="reportData.length"
          layout="prev, pager, next"
          @current-change="handlePageChange"
        />
      </div>
    </el-card>
  </div>
</template>

<script>
import { getTeacherHoursReport } from "@/api/report";
import StatCard from "@/components/StatCard";

export default {
  name: "TeacherHoursReport",
  components: { StatCard },
  data() {
    return {
      loading: false,
      reportData: [],
      dateRange: [this.getDefaultStartDate(), this.getDefaultEndDate()],
      pickerOptions: {
        // disabledDate(time) {
        //   return time.getTime() > Date.now();
        // },//假数据需要注释一下
        shortcuts: [
          {
            text: "本周",
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setDate(start.getDate() - start.getDay());
              picker.$emit("pick", [start, end]);
            },
          },
          {
            text: "本月",
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setDate(1);
              picker.$emit("pick", [start, end]);
            },
          },
          {
            text: "上月",
            onClick(picker) {
              const end = new Date();
              end.setMonth(end.getMonth());
              end.setDate(0);
              const start = new Date();
              start.setDate(1);
              start.setMonth(start.getMonth() - 1);
              picker.$emit("pick", [start, end]);
            },
          },
          {
            text: "本年",
            onClick(picker) {
              const today = new Date();
              const year = today.getFullYear();
              // 本年第一天
              const start = new Date(year, 0, 1);
              // 本年最后一天
              const end = new Date(year, 11, 31);
              picker.$emit("pick", [start, end]);
            },
          },
        ],
      },
      queryParams: {},
      currentPage: 1,
      pageSize: 10,
    };
  },
  computed: {
    paginatedData() {
      const start = (this.currentPage - 1) * this.pageSize;
      const end = start + this.pageSize;
      return this.reportData.slice(start, end);
    },
    totalHours() {
      return this.reportData.reduce((sum, item) => sum + item.totalHours, 0);
    },
    averageHours() {
      return this.reportData.length > 0
        ? (this.totalHours / this.reportData.length).toFixed(2)
        : 0;
    },
    busiestTeacher() {
      if (this.reportData.length === 0)
        return { teacherName: "-", totalHours: 0 };

      let busiest = this.reportData[0];
      this.reportData.forEach((item) => {
        if (item.totalHours > busiest.totalHours) {
          busiest = item;
        }
      });

      return {
        teacherId: busiest.teacherId,
        teacherName: busiest.teacherName || `教师${busiest.teacherId}`,
        totalHours: busiest.totalHours,
      };
    },
  },
  created() {
    this.loadReport();
  },
  methods: {
    // 获取本月第一天（格式：YYYY-MM-DD）
    getDefaultStartDate() {
      const date = new Date();
      const year = date.getFullYear();
      const month = date.getMonth();
      // 使用1号确保是本月第一天
      return this.formatDate(new Date(year, month, 1));
    },

    // 获取本月最后一天（格式：YYYY-MM-DD）
    getDefaultEndDate() {
      const date = new Date();
      const year = date.getFullYear();
      const month = date.getMonth();
      // 计算下月第一天，再减去1天得到本月最后一天
      const lastDayOfMonth = new Date(year, month + 1, 0);
      return this.formatDate(lastDayOfMonth);
    },

    // 通用日期格式化方法
    formatDate(dateObj) {
      if (!dateObj) return "";

      const year = dateObj.getFullYear();
      const month = (dateObj.getMonth() + 1).toString().padStart(2, "0");
      const day = dateObj.getDate().toString().padStart(2, "0");

      return `${year}-${month}-${day}`;
    },

    async loadReport() {
      if (!this.dateRange || this.dateRange.length !== 2) {
        this.$message.warning("请选择日期范围");
        return;
      }

      try {
        this.loading = true;
        const res = await getTeacherHoursReport(
          this.dateRange[0],
          this.dateRange[1]
        );

        console.log("API响应数据:", res); // 添加调试信息

        if (res.code === 20000) {
          // 根据实际API返回数据结构调整
          // 返回的数据结构是：{ data: { list: [...] } }
          this.reportData = res.data?.list || [];

          if (this.reportData.length === 0) {
            this.$message.info("所选日期范围内没有数据");
          }
        } else {
          this.$message.error(res.message || "获取数据失败");
        }
      } catch (error) {
        console.error("获取教师工时统计失败:", error);
        this.$message.error("获取统计数据失败");
      } finally {
        this.loading = false;
      }
    },

    handleQuery() {
      this.currentPage = 1;
      this.loadReport();
    },

    resetQuery() {
      this.dateRange = [this.getDefaultStartDate(), this.getDefaultEndDate()];
      this.$nextTick(() => {
        this.handleQuery();
      });
    },

    handlePageChange(page) {
      this.currentPage = page;
    },
  },
};
</script>

<style scoped>
.app-container {
  padding: 20px;
}

.breadcrumb {
  margin-bottom: 20px;
}

.filter-container {
  margin-bottom: 20px;
}

.filter-form {
  display: flex;
  align-items: center;
}

.stat-cards {
  margin-bottom: 20px;
}

.data-table {
  margin-top: 20px;
}

.hours-value {
  font-weight: bold;
  font-size: 16px;
  color: #1890ff;
}

.table-pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.no-data {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 300px;
}
</style>