<template>
  <el-dialog
    :title="dialogTypeMap[dialogType]"
    :visible.sync="visible"
    width="600px"
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
        <el-input v-model.trim="formData.name" />
      </el-form-item>

      <el-form-item label="性别" prop="gender">
        <el-radio-group v-model="formData.gender">
          <el-radio label="男">男</el-radio>
          <el-radio label="女">女</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="电话" prop="phone">
        <el-input v-model.trim="formData.phone" />
      </el-form-item>

      <el-form-item label="课时费" prop="hourlyFee">
        <el-input-number
          v-model="formData.hourlyFee"
          :min="0.01"
          :precision="2"
          :step="50"
          controls-position="right"
          style="width: 150px"
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
import { createTeacher, updateTeacher } from "@/api/teacher";

export default {
  name: "TeacherDialog",
  props: {
    visible: {
      type: Boolean,
      required: true,
    },
    teacherData: {
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
    const defaultForm = {
      teacherId: null,
      name: "",
      gender: "男", // 默认值
      phone: "", // 改用phone字段
      hourlyFee: 0.01, // 初始值0.01
    };

    return {
      dialogTypeMap: {
        create: "新建教师",
        edit: "编辑教师",
      },
      formData: { ...defaultForm },
      rules: {
        name: [
          { required: true, message: "请输入教师姓名", trigger: "blur" },
          { max: 50, message: "姓名不能超过50个字符", trigger: "blur" },
        ],
        gender: [
          { required: true, message: "请选择性别", trigger: "blur" },
          {
            pattern: /^(男|女)$/,
            message: "性别只能是男或女",
            trigger: "blur",
          },
        ],
        phone: [
          { required: true, message: "请输入联系电话", trigger: "blur" },
          { max: 20, message: "电话不能超过20个字符", trigger: "blur" },
        ],
        hourlyFee: [
          {
            required: true,
            message: "请输入课时费",
            trigger: "blur",
          },
          {
            validator: (rule, value, callback) => {
              if (value < 0.01) {
                callback(new Error("课时费必须大于0.01元"));
              } else {
                callback();
              }
            },
            trigger: "blur",
          },
        ],
      },
      submitting: false,
    };
  },

  watch: {
    // 当教师数据变化时更新表单
    teacherData: {
      handler(newVal) {
        this.formData = {
          ...this.formData,
          ...newVal,
          // 字段名转换
          phone: newVal.phone || newVal.contactPhone || "",
        };

        // 如果后端返回的是整数形式的hourlyFee，转换为数字
        if (typeof this.formData.hourlyFee === "number") {
          this.formData.hourlyFee = Number(this.formData.hourlyFee.toFixed(2));
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

  methods: {
    // 重置表单
    resetForm() {
      this.$refs.form?.resetFields();
      this.submitting = false;
      // 重置为初始值
      this.formData = {
        teacherId: null,
        name: "",
        gender: "男",
        phone: "",
        hourlyFee: 0.01,
      };
    },

    // 提交表单
    submitForm() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.submitting = true;

          // 准备API参数
          const apiData = {
            ...this.formData,
            // 确保删除前端添加的额外字段
            contactPhone: undefined,
          };

          const api =
            this.dialogType === "create" ? createTeacher : updateTeacher;

          api(apiData)
            .then(() => {
              this.$message.success(
                `${this.dialogTypeMap[this.dialogType]}成功`
              );
              this.$emit("refresh");
              this.visible = false;
            })
            .catch((error) => {
              console.error("操作失败:", error);
              this.$message.error(
                `${this.dialogTypeMap[this.dialogType]}失败: ${
                  error.message || "未知错误"
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