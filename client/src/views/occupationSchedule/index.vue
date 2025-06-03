<template>
  <div class="app-container">
    <!-- 搜索区域 - 使用统一样式 -->
    <div class="filter-container">
      <div class="filter-form">
        <el-input
          v-model="listQuery.teacherId"
          placeholder="输入教师ID"
          clearable
          class="filter-item"
          style="width: 200px; margin-right: 10px"
          @keyup.enter.native="handleFilter"
        />

        <el-date-picker
          v-model="listQuery.date"
          type="date"
          placeholder="选择日期"
          format="yyyy-MM-dd"
          value-format="yyyy-MM-dd"
          class="filter-item"
          style="width: 200px; margin-right: 10px"
        />

        <el-button
          type="primary"
          icon="el-icon-search"
          class="filter-item"
          @click="handleFilter"
        >
          搜索
        </el-button>

        <el-button
          type="success"
          icon="el-icon-plus"
          class="filter-item"
          @click="handleCreate"
        >
          添加排班
        </el-button>
      </div>
    </div>

    <!-- 数据表格 - 使用统一data-table类名 -->
    <el-table
      v-loading="listLoading"
      :data="list"
      element-loading-text="数据加载中"
      border
      fit
      highlight-current-row
      class="data-table"
      ref="scheduleTable"
    >
      <el-table-column
        label="ID"
        prop="scheduleId"
        align="center"
        min-width="100"
      />
      <el-table-column
        label="职业ID"
        prop="occupationId"
        align="center"
        min-width="100"
      />
      <el-table-column
        label="教师ID"
        prop="teacherId"
        align="center"
        min-width="100"
      />

      <el-table-column label="日期" align="center" min-width="120">
        <template slot-scope="{ row }">
          <span class="date-cell">{{ formatDate(row.date) }}</span>
        </template>
      </el-table-column>

      <el-table-column label="开始时间" align="center" min-width="120">
        <template slot-scope="{ row }">
          <span class="time-cell">{{ formatTime(row.startTime) }}</span>
        </template>
      </el-table-column>

      <el-table-column label="结束时间" align="center" min-width="120">
        <template slot-scope="{ row }">
          <span class="time-cell">{{ formatTime(row.endTime) }}</span>
        </template>
      </el-table-column>

      <el-table-column
        label="操作"
        align="center"
        min-width="180"
        fixed="right"
      >
        <template slot-scope="{ row }">
          <el-button
            size="mini"
            type="primary"
            icon="el-icon-edit"
            @click="handleUpdate(row)"
          >
            编辑
          </el-button>
          <el-button
            size="mini"
            type="danger"
            icon="el-icon-delete"
            @click="handleDelete(row)"
          >
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页组件 - 使用统一左对齐样式 -->
    <el-pagination
      background
      :current-page.sync="listQuery.page"
      :page-sizes="[10, 20, 50, 100]"
      :page-size.sync="listQuery.limit"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      class="custom-pagination"
    />

    <!-- 添加/编辑对话框组件 -->
    <schedule-dialog
      :visible.sync="dialogVisible"
      :type="dialogType"
      :schedule-data="tempSchedule"
      :rules="rules"
      :submitting="dialogSubmitting"
      @submit="handleDialogSubmit"
      @update:visible="(v) => (dialogVisible = v)"
      @update:submitting="(v) => (dialogSubmitting = v)"
    />
  </div>
</template>

<script>
import { parseTime } from "@/utils";
import {
  getOccupationsSchedules,
  createSchedule,
  updateSchedule,
  deleteSchedule,
} from "@/api/occupationSchedule";
import ScheduleDialog from "@/components/ScheduleDialog.vue";

export default {
  name: "OccupationScheduleList",
  components: {
    ScheduleDialog,
  },
  data() {
    // 时间范围验证规则
    const validateTimeRange = (rule, value, callback) => {
      if (this.tempSchedule.startTime && this.tempSchedule.endTime) {
        const start = this.tempSchedule.startTime;
        const end = this.tempSchedule.endTime;
        if (start >= end) {
          callback(new Error("结束时间必须大于开始时间"));
          return;
        }
      }
      callback();
    };

    return {
      list: [], // 排班列表数据
      listLoading: false, // 加载状态
      total: 0, // 总记录数
      dialogVisible: false, // 对话框显示状态
      dialogSubmitting: false, // 对话框提交状态 - 关键修复
      dialogType: "create", // 对话框类型 create/update
      tempSchedule: this.getDefaultSchedule(), // 临时排班数据
      // 查询参数
      listQuery: {
        page: 1,
        limit: 10,
        teacherId: null,
        date: null,
      },
      // 表单验证规则
      rules: {
        occupationId: [
          { required: true, message: "职业ID不能为空", trigger: "blur" },
          {
            type: "number",
            min: 1,
            message: "职业ID必须大于0",
            trigger: "blur",
          },
        ],
        teacherId: [
          { required: true, message: "教师ID不能为空", trigger: "blur" },
          {
            type: "number",
            min: 1,
            message: "教师ID必须大于0",
            trigger: "blur",
          },
        ],
        date: [{ required: true, message: "日期不能为空", trigger: "change" }],
        startTime: [
          { required: true, message: "开始时间不能为空", trigger: "change" },
        ],
        endTime: [
          { required: true, message: "结束时间不能为空", trigger: "change" },
          { validator: validateTimeRange, trigger: "change" },
        ],
      },
    };
  },
  created() {
    this.fetchData();
  },
  mounted() {
    window.addEventListener("resize", this.doLayout);
  },
  beforeDestroy() {
    window.removeEventListener("resize", this.doLayout);
  },
  methods: {
    // 触发表格重新布局
    doLayout() {
      if (this.$refs.scheduleTable) {
        this.$refs.scheduleTable.doLayout();
      }
    },

    // 获取排班数据
    fetchData() {
      this.listLoading = true;

      // 转换请求参数格式
      const params = {
        pageNum: this.listQuery.page,
        pageSize: this.listQuery.limit,
        ...(this.listQuery.teacherId && {
          teacherId: this.listQuery.teacherId,
        }),
        ...(this.listQuery.date && { date: this.listQuery.date }),
      };

      getOccupationsSchedules(params)
        .then((response) => {
          if (response.code === 20000) {
            this.list = response.data?.page?.records || [];
            this.total = response.data?.page?.total || 0;
          } else {
            this.$message.error(response.message || "获取数据失败");
          }
        })
        .catch((error) => {
          console.error("获取排班失败:", error);
          this.$message.error(
            "获取数据失败: " + (error.message || "请稍后重试")
          );
        })
        .finally(() => {
          this.listLoading = false;
          this.$nextTick(() => {
            this.doLayout();
          });
        });
    },

    // 处理筛选
    handleFilter() {
      this.listQuery.page = 1;
      this.fetchData();
    },

    // 每页数量改变
    handleSizeChange(val) {
      this.listQuery.limit = val;
      this.fetchData();
    },

    // 当前页码改变
    handleCurrentChange(val) {
      this.listQuery.page = val;
      this.fetchData();
    },

    // 重置临时排班数据
    getDefaultSchedule() {
      return {
        scheduleId: null,
        occupationId: null,
        teacherId: null,
        date: null,
        startTime: null,
        endTime: null,
      };
    },

    // 处理添加按钮
    handleCreate() {
      this.dialogType = "create";
      this.tempSchedule = this.getDefaultSchedule();
      this.dialogVisible = true;
      this.dialogSubmitting = false; // 重置提交状态
    },

    // 处理编辑按钮
    handleUpdate(row) {
      this.dialogType = "update";
      this.tempSchedule = { ...row };
      this.dialogVisible = true;
      this.dialogSubmitting = false; // 重置提交状态
    },

    // 处理对话框提交
    handleDialogSubmit({ type, data }) {
      if (type === "create") {
        this.createData(data);
      } else {
        this.updateData(data);
      }
    },

    // 创建排班
    createData(scheduleData) {
      // 修复：添加对日期格式的处理
      const processedData = {
        ...scheduleData,
        date: scheduleData.date || parseTime(new Date(), "{y}-{m}-{d}"),
      };

      createSchedule(processedData)
        .then((response) => {
          if (response.code === 20000) {
            this.$message.success("创建成功");
            this.dialogVisible = false;
            this.dialogSubmitting = false; // 重置提交状态
            this.fetchData();
          } else if (response.code === 50008) {
            // 处理服务器返回的时间冲突错误
            this.$message.error(response.message || "时间段冲突，请重新选择");
            this.dialogSubmitting = false; // 重置提交状态
          } else {
            this.$message.error(response.message || "创建失败");
            this.dialogSubmitting = false; // 重置提交状态
          }
        })
        .catch((error) => {
          console.error("创建排班失败:", error);
          this.$message.error("创建失败: " + (error.message || "请稍后重试"));
          this.dialogSubmitting = false; // 重置提交状态
        });
    },

    // 更新排班
    updateData(scheduleData) {
      // 确保ID存在
      if (!scheduleData.scheduleId) {
        this.$message.error("无效的排班ID");
        this.dialogSubmitting = false;
        return;
      }

      updateSchedule(scheduleData.scheduleId, scheduleData)
        .then((response) => {
          if (response.code === 20000) {
            this.$message.success("更新成功");
            this.dialogVisible = false;
            this.dialogSubmitting = false; // 重置提交状态
            this.fetchData();
          } else if (response.code === 50008) {
            // 处理服务器返回的时间冲突错误
            this.$message.error(response.message || "时间段冲突，请重新选择");
            this.dialogSubmitting = false; // 重置提交状态
          } else {
            this.$message.error(response.message || "更新失败");
            this.dialogSubmitting = false; // 重置提交状态
          }
        })
        .catch((error) => {
          console.error("更新排班失败:", error);
          this.$message.error("更新失败: " + (error.message || "请稍后重试"));
          this.dialogSubmitting = false; // 重置提交状态
        });
    },

    // 删除排班
    handleDelete(row) {
      this.$confirm(
        `确定删除ID为 ${row.scheduleId} 的排班记录吗？`,
        "删除确认",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      )
        .then(() => {
          this.listLoading = true;
          return deleteSchedule(row.scheduleId);
        })
        .then((response) => {
          if (response.code === 20000) {
            this.$message.success("删除成功");
            // 删除后判断是否需要调整分页
            if (this.list.length === 1 && this.listQuery.page > 1) {
              this.listQuery.page -= 1;
            }
            this.fetchData();
          } else {
            this.$message.error(response.message || "删除失败");
          }
        })
        .catch((error) => {
          // 当取消操作时，不显示错误信息
          if (error !== "cancel") {
            console.error("删除失败:", error);
            this.$message.error("删除失败: " + (error.message || "请稍后重试"));
          }
        })
        .finally(() => {
          this.listLoading = false;
        });
    },

    // 格式化日期
    formatDate(date) {
      if (!date) return "";
      // 如果已经是正确格式，直接返回
      if (date.length === 10) return date;
      return parseTime(date, "{y}-{m}-{d}");
    },

    // 格式化时间
    formatTime(time) {
      if (!time) return "";
      // 如果已经是正确格式，直接返回
      if (time.length === 5) return time;
      // 处理时间格式，例如：09:00:00 -> 09:00
      return time.substring(0, 5);
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

.filter-form {
  display: flex;
  flex-wrap: wrap;
}

/* 表格样式统一 */
.data-table {
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  margin-top: 0;
}

/* 日期和时间单元格样式 */
.date-cell {
  font-weight: 600;
  color: #409eff;
}

.time-cell {
  font-weight: 500;
  color: #606266;
}

/* 操作按钮样式 */
.el-button--mini {
  padding: 5px 10px;
}

/* 分页左对齐 */
.custom-pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-start;
}

/* 解决表格空白问题 */
::v-deep .el-table {
  width: 100% !important;
}

::v-deep .el-table__body-wrapper {
  overflow: auto;
}

::v-deep .el-table__body {
  width: 100% !important;
}
</style>