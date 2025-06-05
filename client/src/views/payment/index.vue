<template>
  <div class="app-container">
    <el-alert
      title="注意"
      type="info"
      :closable="false"
      description="收费数据由系统自动计算生成，无法手动修改"
      style="margin-bottom: 20px"
    />
    <!-- 搜索区域 -->
    <div class="filter-container">
      <el-form
        ref="queryForm"
        :model="queryParams"
        :inline="true"
        class="filter-form"
      >
        <el-form-item label="职业ID" prop="occupationId">
          <el-input
            v-model.number="queryParams.occupationId"
            placeholder="输入职业ID"
            clearable
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>

        <el-form-item label="支付日期" prop="dateRange">
          <el-date-picker
            v-model="dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="yyyy-MM-dd"
          />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery"
            >查询</el-button
          >
          <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 数据表格 -->
    <el-card shadow="never" style="margin-top: 20px">
      <el-table
        v-loading="loading"
        :data="paymentList"
        border
        stripe
        fit
        highlight-current-row
        class="data-table"
      >
        <el-table-column
          label="ID"
          prop="paymentId"
          align="center"
          min-width="80"
        />
        <el-table-column
          label="职业ID"
          prop="occupationId"
          align="center"
          min-width="100"
        />
        <el-table-column
          label="支付日期"
          prop="paymentDate"
          align="center"
          min-width="120"
        >
          <template slot-scope="{ row }">
            {{ formatDate(row.paymentDate) }}
          </template>
        </el-table-column>
        <el-table-column
          label="金额"
          prop="amount"
          align="center"
          min-width="120"
        >
          <template slot-scope="{ row }">
            <span>¥{{ row.amount.toFixed(2) }}</span>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页组件 -->
      <el-pagination
        background
        :current-page.sync="queryParams.pageNum"
        :page-sizes="[10, 20, 50, 100]"
        :page-size.sync="queryParams.pageSize"
        layout="total, sizes, prev, pager, next"
        :total="total"
        @size-change="getList"
        @current-change="getList"
        style="margin-top: 20px"
      />
    </el-card>
  </div>
</template>

<script>
import { getPaymentsPage } from "@/api/payment";

export default {
  name: "PaymentList",
  data() {
    return {
      loading: true,
      paymentList: [],
      total: 0,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        occupationId: null,
      },
      dateRange: [],
    };
  },
  created() {
    this.getList();
  },
  methods: {
    async getList() {
      this.loading = true;
      try {
        const params = {
          ...this.queryParams,
        };

        // 添加日期范围参数
        if (this.dateRange && this.dateRange.length === 2) {
          params.startDate = this.dateRange[0];
          params.endDate = this.dateRange[1];
        }

        // 清理空值
        Object.keys(params).forEach((key) => {
          if (
            params[key] === null ||
            params[key] === "" ||
            params[key] === undefined
          ) {
            delete params[key];
          }
        });

        const res = await getPaymentsPage(params);
        // 使用后端返回的数据结构
        if (res.data && res.data.page && res.data.page.records) {
          this.paymentList = res.data.page.records;
          this.total = res.data.page.total;
        } else {
          console.warn("API返回数据格式不符:", res);
          this.$message.warning("返回数据格式异常");
        }
      } catch (error) {
        console.error("获取支付记录失败:", error);
        this.$message.error("获取支付记录失败");
      } finally {
        this.loading = false;
      }
    },

    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },

    resetQuery() {
      this.$refs.queryForm.resetFields();
      this.dateRange = [];
      this.queryParams = {
        pageNum: 1,
        pageSize: this.queryParams.pageSize,
        occupationId: null,
      };
      this.handleQuery();
    },

    formatDate(dateStr) {
      if (!dateStr) return "";
      try {
        // 处理ISO日期格式
        const date = new Date(dateStr);
        return date.toISOString().split("T")[0];

        // 或者更通用的方式：
        // return `${date.getFullYear()}-${(date.getMonth() + 1).toString().padStart(2, '0')}-${date.getDate().toString().padStart(2, '0')}`;
      } catch (e) {
        console.error("日期格式化错误:", e);
        return dateStr; // 返回原始字符串
      }
    },
  },
};
</script>

<style scoped>
/* 样式保持不变 */
</style>