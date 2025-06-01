<template>
  <el-dialog
    :title="dialogTypeMap[dialogType]"
    :visible.sync="visible"
    width="500px"
    @close="resetForm"
  >
    <el-form
      ref="form"
      :model="formData"
      label-width="120px"
      :rules="rules"
      @submit.native.prevent="submitForm"
    >
      <el-form-item label="职业类型名称" prop="name">
        <el-input
          v-model.trim="formData.name"
          placeholder="请输入职业类型名称"
          maxlength="50"
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
import {
  createOccupationType,
  updateOccupationType,
} from "@/api/occupationType";

export default {
  name: "OccupationTypeDialog",
  props: {
    visible: {
      type: Boolean,
      required: true,
    },
    occupationTypeData: {
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
      occupationTypeId: null,
      name: "",
    };

    return {
      dialogTypeMap: {
        create: "新建职业类型",
        edit: "编辑职业类型",
      },
      formData: { ...defaultForm },
      rules: {
        name: [
          { required: true, message: "请输入职业类型名称", trigger: "blur" },
          {
            min: 2,
            max: 50,
            message: "名称长度在2到50个字符",
            trigger: "blur",
          },
        ],
      },
      submitting: false,
    };
  },

  watch: {
    // 当职业类型数据变化时更新表单
    occupationTypeData: {
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

          // 准备提交的数据
          const submitData = { ...this.formData };

          const api =
            this.dialogType === "create"
              ? createOccupationType
              : updateOccupationType;

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