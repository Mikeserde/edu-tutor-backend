<template>
  <el-dialog
    :title="dialogTypeMap[dialogType]"
    :visible.sync="visible"
    width="650px"
    @close="resetForm"
  >
    <el-form
      ref="form"
      :model="formData"
      label-width="120px"
      :rules="rules"
      @submit.native.prevent="submitForm"
    >
      <!-- 学生选择 -->
      <el-form-item label="选择学生" prop="studentId">
        <el-select
          v-model="formData.studentId"
          placeholder="请选择学生"
          filterable
          clearable
          style="width: 100%"
          @change="handleStudentChange"
        >
          <el-option
            v-for="student in studentOptions"
            :key="student.studentId"
            :label="`${student.name} (ID: ${student.studentId})`"
            :value="student.studentId"
          >
            <div class="student-option">
              <span class="student-name">{{ student.name }}</span>
              <span class="student-id">ID: {{ student.studentId }}</span>
            </div>
          </el-option>
        </el-select>

        <div v-if="selectedStudent" class="student-info-card">
          <div class="info-item">
            <span class="label">性别:</span>
            <span class="value">{{ selectedStudent.gender }}</span>
          </div>
          <div class="info-item">
            <span class="label">联系方式:</span>
            <span class="value">{{
              formatPhone(selectedStudent.contactPhone)
            }}</span>
          </div>
        </div>
      </el-form-item>

      <!-- 职业类型选择 -->
      <el-form-item label="选择职业类型" prop="occupationTypeId">
        <el-select
          v-model="formData.occupationTypeId"
          placeholder="请选择职业类型"
          filterable
          clearable
          style="width: 100%"
        >
          <el-option
            v-for="type in occupationTypeOptions"
            :key="type.occupationTypeId"
            :label="type.name"
            :value="type.occupationTypeId"
          />
        </el-select>
      </el-form-item>
    </el-form>

    <span slot="footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" :loading="submitting" @click="submitForm">
        提交
      </el-button>
    </span>
  </el-dialog>
</template>

<script>
import {
  createOccupationRegistration,
  updateOccupationRegistration,
} from "@/api/occupationRegistration";
import { formatPhone } from "@/utils";

export default {
  name: "OccupationRegistrationDialog",
  props: {
    visible: {
      type: Boolean,
      required: true,
    },
    registrationData: {
      type: Object,
      default: () => ({}),
    },
    dialogType: {
      type: String,
      validator: (value) => ["create", "edit"].includes(value),
      default: "create",
    },
    occupationTypeOptions: {
      type: Array,
      default: () => [],
    },
    studentOptions: {
      type: Array,
      default: () => [],
    },
  },

  data() {
    // 默认表单数据
    const defaultForm = {
      occupationId: null,
      studentId: null,
      occupationTypeId: null,
    };

    return {
      dialogTypeMap: {
        create: "新建职业注册",
        edit: "编辑职业注册",
      },
      formData: { ...defaultForm },
      selectedStudent: null,
      rules: {
        studentId: [{ required: true, message: "请选择学生", trigger: "blur" }],
        occupationTypeId: [
          { required: true, message: "请选择职业类型", trigger: "blur" },
        ],
      },
      submitting: false,
    };
  },

  methods: {
    formatPhone,

    // 学生选择变化
    handleStudentChange(studentId) {
      this.selectedStudent =
        this.studentOptions.find((s) => s.studentId === studentId) || null;
    },

    // 重置表单
    resetForm() {
      if (this.$refs.form) {
        this.$refs.form.resetFields();
      }
      this.submitting = false;
      this.selectedStudent = null;
      this.formData = { ...this.defaultForm };
    },

    // 提交表单
    submitForm() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.submitting = true;

          // 准备提交的数据
          const submitData = { ...this.formData };

          // 删除前端添加的额外字段
          delete submitData.studentName;
          delete submitData.occupationTypeName;

          const api =
            this.dialogType === "create"
              ? createOccupationRegistration
              : updateOccupationRegistration;

          api(submitData)
            .then((response) => {
              if (response.code === 20000) {
                this.$message.success(
                  `${this.dialogTypeMap[this.dialogType]}成功`
                );
                this.$emit("refresh");
                this.visible = false;
              } else {
                // 处理唯一性验证错误
                const errorMsg =
                  response.data?.msg ||
                  response.message ||
                  `${this.dialogTypeMap[this.dialogType]}失败`;
                this.$message.error(errorMsg);
              }
            })
            .catch((error) => {
              console.error("操作失败:", error);
              this.$message.error(
                `${this.dialogTypeMap[this.dialogType]}失败: ${
                  error.message || "请联系管理员"
                }`
              );
            })
            .finally(() => {
              this.submitting = false;
              this.visible = false;
            });
        }
      });
    },
  },

  watch: {
    // 当注册数据变化时更新表单
    registrationData: {
      handler(newVal) {
        this.formData = { ...this.formData, ...newVal };

        // 设置选中的学生信息
        if (newVal.studentId) {
          this.handleStudentChange(newVal.studentId);
        }
      },
      immediate: true,
      deep: true,
    },

    // 当弹窗显示状态变化时通知父组件
    visible(newVal) {
      this.$emit("update:visible", newVal);
    },
  },
};
</script>

<style scoped>
.el-form-item {
  margin-bottom: 20px;
}

.student-option {
  display: flex;
  justify-content: space-between;
}
.student-name {
  font-weight: 500;
}
.student-id {
  color: #666;
  font-size: 12px;
}

.student-info-card {
  margin-top: 10px;
  padding: 10px;
  background: #f8f8f8;
  border-radius: 4px;
  border-left: 3px solid #409eff;
}

.info-item {
  display: flex;
  margin-bottom: 5px;
  font-size: 14px;
}
.info-item .label {
  font-weight: 500;
  width: 70px;
  color: #333;
}
.info-item .value {
  color: #666;
}
</style>