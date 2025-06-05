<template>
  <div class="app-container">
    <!-- 筛选区域 -->
    <div class="filter-container">
      <el-card shadow="never">
        <el-form :inline="true" class="filter-form">
          <el-form-item>
            <el-button
              type="primary"
              icon="el-icon-refresh"
              @click="loadReport"
              :loading="loading"
              >刷新数据</el-button
            >
          </el-form-item>
        </el-form>
      </el-card>
    </div>

    <!-- 数据展示区域 -->
    <div class="data-container">
      <el-row :gutter="20">
        <el-col :span="16">
          <!-- 职业需求表格 -->
          <el-card shadow="never" style="margin-bottom: 20px">
            <div slot="header">
              <span>职业需求排名</span>
              <el-tag type="warning" style="margin-left: 10px">
                统计时间: {{ currentDate }}
              </el-tag>
            </div>
            <el-table
              v-loading="loading"
              :data="reportData"
              border
              stripe
              fit
              highlight-current-row
              class="data-table"
              v-if="reportData.length > 0"
            >
              <el-table-column
                type="index"
                label="排名"
                align="center"
                width="80"
              />
              <el-table-column
                label="职业类型"
                prop="occupationName"
                align="center"
                min-width="180"
              />
              <el-table-column
                label="需求量"
                prop="demandCount"
                align="center"
                min-width="120"
              >
                <template slot-scope="{ row }">
                  <el-tag :type="getDemandTagType(row.demandCount)">
                    {{ row.demandCount }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column label="占比" align="center" min-width="120">
                <template slot-scope="{ row }">
                  <span class="percentage-value">
                    {{ calculatePercentage(row.demandCount) }}%
                  </span>
                </template>
              </el-table-column>
            </el-table>
            <div v-else class="no-data">
              <el-empty description="暂无数据"></el-empty>
            </div>
          </el-card>
        </el-col>

        <el-col :span="8">
          <!-- 职业需求饼图 -->
          <el-card shadow="never" class="chart-card">
            <div slot="header">
              <span>职业需求分布图</span>
            </div>
            <div v-loading="loading" class="chart-container">
              <div
                v-if="reportData.length > 0"
                id="occupation-demand-chart"
                style="height: 400px"
              ></div>
              <div v-else class="no-data">
                <el-empty description="暂无数据"></el-empty>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { getOccupationDemandReport } from "@/api/report";
import * as echarts from "echarts";

export default {
  name: "OccupationDemandReport",
  data() {
    return {
      loading: true,
      reportData: [],
      chartInstance: null,
      currentDate: new Date().toLocaleDateString(),
    };
  },
  mounted() {
    this.loadReport();
  },
  beforeDestroy() {
    if (this.chartInstance) {
      this.chartInstance.dispose();
    }
  },
  methods: {
    async loadReport() {
      try {
        this.loading = true;
        const res = await getOccupationDemandReport();

        console.log("API Response:", res); // 打印API响应以便调试

        if (res.code === 20000 && res.data.list) {
          this.reportData = res.data.list;
          this.$nextTick(() => {
            this.renderChart();
          });
        } else {
          this.$message.error(res.message || "获取数据失败");
        }
      } catch (error) {
        console.error("获取职业需求统计失败:", error);
        this.$message.error("获取统计数据失败");
      } finally {
        this.loading = false;
      }
    },

    renderChart() {
      if (this.reportData.length === 0) return;

      // 准备图表数据 - 根据实际返回的字段名进行调整
      const chartData = this.reportData.slice(0, 10).map((item) => ({
        value: item.demandCount,
        name: item.occupationName,
      }));

      // 初始化图表
      const chartDom = document.getElementById("occupation-demand-chart");
      if (!chartDom) return;

      this.chartInstance = echarts.init(chartDom);

      const option = {
        tooltip: {
          trigger: "item",
          formatter: "{a} <br/>{b}: {c} ({d}%)",
        },
        legend: {
          orient: "vertical",
          right: 10,
          top: "center",
          formatter: (name) => {
            return name.length > 8 ? name.substring(0, 8) + "..." : name;
          },
        },
        series: [
          {
            name: "职业需求",
            type: "pie",
            radius: ["40%", "70%"],
            avoidLabelOverlap: false,
            itemStyle: {
              borderRadius: 10,
              borderColor: "#fff",
              borderWidth: 2,
            },
            label: {
              show: false,
              position: "center",
            },
            emphasis: {
              label: {
                show: true,
                fontSize: "18",
                fontWeight: "bold",
              },
            },
            labelLine: {
              show: false,
            },
            data: chartData,
          },
        ],
      };

      this.chartInstance.setOption(option);

      // 监听窗口变化自适应图表大小
      window.addEventListener("resize", () => {
        this.chartInstance && this.chartInstance.resize();
      });
    },

    calculatePercentage(value) {
      const total = this.reportData.reduce(
        (sum, item) => sum + item.demandCount,
        0
      );
      return total > 0 ? ((value / total) * 100).toFixed(2) : "0.00";
    },

    getDemandTagType(count) {
      const maxDemand = Math.max(
        ...this.reportData.map((item) => item.demandCount)
      );

      if (count >= maxDemand * 0.8) return "danger";
      if (count >= maxDemand * 0.5) return "warning";
      return "success";
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

.data-container {
  margin-top: 20px;
}

.chart-card {
  height: 100%;
}

.percentage-value {
  font-weight: bold;
  color: #1890ff;
}

.data-table {
  margin-top: 10px;
  width: 100%;
}

.chart-container {
  height: 400px;
}

.no-data {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 200px;
}
</style>