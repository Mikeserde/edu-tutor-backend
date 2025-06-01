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
      label-width="100px"
      :rules="rules"
      @submit.native.prevent="submitForm"
    >
      <el-form-item label="姓名" prop="name">
        <el-input v-model.trim="formData.name" placeholder="请输入学生姓名" />
      </el-form-item>

      <el-form-item label="性别" prop="gender">
        <el-radio-group v-model="formData.gender">
          <el-radio label="男">男</el-radio>
          <el-radio label="女">女</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="联系电话" prop="contactPhone">
        <el-input
          v-model.trim="formData.contactPhone"
          placeholder="请输入联系电话"
        />
      </el-form-item>

      <el-form-item label="地址" prop="address">
        <el-input
          v-model.trim="formData.address"
          type="textarea"
          :rows="2"
          placeholder="请输入学生地址"
          maxlength="100"
          show-word-limit
        />
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
import { createStudent, updateStudent } from "@/api/student";

export default {
  name: "StudentDialog",
  props: {
    visible: {
      type: Boolean,
      required: true,
    },
    studentData: {
      type: Object,
      default: () => ({}),
    },
    dialogType: {
      type: String,
      validator: (value) => ["create", "edit"].includes(value),
      default: "create",
    },
  },

  data() {
    // 默认表单数据
    const defaultForm = {
      studentId: null,
      name: "",
      gender: "男",
      contactPhone: "",
      address: "",
    };

    return {
      dialogTypeMap: {
        create: "新建学生",
        edit: "编辑学生",
      },
      formData: { ...defaultForm },
      rules: {
        name: [
          { required: true, message: "请输入学生姓名", trigger: "blur" },
          {
            min: 2,
            max: 50,
            message: "姓名长度在2到50个字符",
            trigger: "blur",
          },
        ],
        gender: [{ required: true, message: "请选择性别", trigger: "change" }],
        contactPhone: [
          { required: true, message: "请输入联系电话", trigger: "blur" },
          { max: 20, message: "联系电话不能超过20个字符", trigger: "blur" },
          // 简单的电话格式验证 (支持固话和手机)
          {
            pattern: /^1\d{10}$|^0\d{2,3}-?\d{7,8}$/,
            message: "请输入有效的联系电话",
            trigger: "blur",
          },
        ],
        address: [
          { required: true, message: "请输入学生地址", trigger: "blur" },
          { max: 100, message: "地址不能超过100个字符", trigger: "blur" },
        ],
      },
      submitting: false,
    };
  },

  watch: {
    // 当学生数据变化时更新表单
    studentData: {
      handler(newVal) {
        this.formData = { ...this.formData, ...newVal };
      },
      immediate: true,
      deep: true,
    },

    // 当弹窗显示状态变化时通知父组件
    visible(newVal) {
      this.$emit("update:visible", newVal);
    },
  },

  methods: {
    // 重置表单
    resetForm() {
      if (this.$refs.form) {
        this.$refs.form.resetFields();
      }
      this.submitting = false;
      // 重置为初始值
      this.formData = { ...this.defaultForm };
    },

    // 提交表单
    submitForm() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.submitting = true;

          // 准备提交的数据 (从表单数据复制)
          const submitData = { ...this.formData };

          const api =
            this.dialogType === "create" ? createStudent : updateStudent;

          api(submitData)
            .then((response) => {
              if (response.code === 20000) {
                this.$message.success(
                  `${this.dialogTypeMap[this.dialogType]}成功`
                );
                this.$emit("refresh");
                this.visible = false;
              } else {
                this.$message.error(
                  response.message ||
                    `${this.dialogTypeMap[this.dialogType]}失败`
                );
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
            });
        }
      });
    },
  },
};
</script>

<style scoped>
.el-form-item {
  margin-bottom: 20px;
}
</style>