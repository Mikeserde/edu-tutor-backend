<template>
  <div class="app-container">
    <!-- 工具栏 -->
    <div class="filter-container">
      <el-button type="primary" icon="el-icon-plus" @click="handleCreate">
        新建职业类型
      </el-button>

      <el-input
        v-model="listQuery.name"
        placeholder="搜索职业类型名称"
        class="filter-item"
        style="width: 250px; margin-left: 15px"
        @keyup.enter.native="handleFilter"
      />
    </div>

    <!-- 职业类型数据表格 -->
    <el-table
      v-loading="listLoading"
      :data="formattedList"
      element-loading-text="数据加载中"
      border
      fit
      highlight-current-row
      style="width: 100%; margin-top: 20px"
      class="occupation-type-table"
      ref="occupationTypeTable"
    >
      <!-- ID列 -->
      <el-table-column
        prop="occupationTypeId"
        align="center"
        label="ID"
        width="90"
        sortable
      />

      <!-- 名称列 -->
      <el-table-column
        prop="name"
        label="职业类型名称"
        align="center"
        min-width="200"
        show-overflow-tooltip
      >
        <template slot-scope="scope">
          <span class="name-cell">{{ scope.row.name }}</span>
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

    <!-- 职业类型编辑/新建弹窗 -->
    <occupation-type-dialog
      :visible.sync="dialogVisible"
      :occupation-type-data="currentOccupationType"
      :dialog-type="dialogType"
      @refresh="handleRefresh"
    />
  </div>
</template>

<script>
import { getOccupationTypes, deleteOccupationType } from "@/api/occupationType";
import OccupationTypeDialog from "@/components/OccupationTypeDialog";

export default {
  name: "OccupationTypeList",
  components: { OccupationTypeDialog },
  data() {
    return {
      rawList: [], // 原始数据
      total: 0, // 总记录数
      totalPages: 1, // 总页数
      listLoading: true, // 加载状态
      listQuery: {
        page: 1, // 当前页码
        limit: 10, // 每页条数
        name: null, // 搜索名称
      },
      dialogVisible: false, // 弹窗显示状态
      dialogType: "create", // 弹窗类型
      currentOccupationType: {}, // 当前操作的职业类型数据
    };
  },
  computed: {
    // 确保数据格式正确
    formattedList() {
      return this.rawList.map((type) => ({
        occupationTypeId: type.occupationTypeId,
        name: type.name,
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
    // 刷新数据
    handleRefresh() {
      this.dialogVisible = false;
      this.fetchData();
    },

    // 触发表格重新布局
    doLayout() {
      this.$refs.occupationTypeTable.doLayout();
    },

    // 获取职业类型列表数据
    fetchData() {
      this.listLoading = true;

      // 构造参数（与后端Controller匹配）
      const params = {
        pageNum: this.listQuery.page,
        pageSize: this.listQuery.limit,
        name: this.listQuery.name,
      };

      getOccupationTypes(params)
        .then((response) => {
          if (response.code === 20000) {
            const pageData = response.data.page;

            // 确保总页数至少为1（即使无数据也显示1页）
            this.totalPages = Math.max(
              1,
              Math.ceil(pageData.total / this.listQuery.limit)
            );
            this.total = pageData.total;

            // 智能页码修正：
            if (this.listQuery.page > this.totalPages && this.totalPages > 0) {
              // 跳转到最后一页
              this.listQuery.page = this.totalPages;
              // 重载数据（触发页码修正）
              this.$nextTick(() => {
                this.fetchData();
              });
              return; // 跳过后续处理
            }

            // 正常处理数据
            this.rawList = [...pageData.records];
            this.$nextTick(this.doLayout);
          } else {
            this.$message.error(response.message || "获取职业类型列表失败");
          }
          this.listLoading = false;
        })
        .catch((error) => {
          this.listLoading = false;
          console.error("获取职业类型列表失败:", error);
          this.$message.error("获取职业类型列表失败");
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

    // 打开新建职业类型弹窗
    handleCreate() {
      this.dialogType = "create";
      this.currentOccupationType = {};
      this.dialogVisible = true;
    },

    // 打开编辑职业类型弹窗
    handleEdit(row) {
      this.dialogType = "edit";
      this.currentOccupationType = { ...row };
      this.dialogVisible = true;
    },

    // 删除职业类型处理
    handleDelete(row) {
      this.$confirm(`确定删除职业类型 "${row.name}" 吗？`, "删除确认", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          return deleteOccupationType(row.occupationTypeId);
        })
        .then((response) => {
          if (response.code === 20000) {
            this.$message.success("删除成功");

            // 智能页码处理：
            if (this.rawList.length === 1 && this.totalPages > 1) {
              // 安全回退一页（不会小于1）
              this.listQuery.page = Math.max(1, this.listQuery.page - 1);
            }

            // 重新加载数据（里面包含智能页码修正）
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

.occupation-type-table {
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

/* 职业类型名称样式 */
.name-cell {
  font-weight: 500;
  color: #333;
}
</style>