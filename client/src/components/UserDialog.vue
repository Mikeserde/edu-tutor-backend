<template>
  <el-dialog
    :title="dialogType === 'create' ? '新建用户' : '编辑用户'"
    :visible.sync="visible"
    width="500px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <el-form ref="form" :model="form" :rules="rules" label-width="100px">
      <el-form-item label="用户名" prop="username">
        <el-input
          v-model="form.username"
          :disabled="dialogType === 'edit'"
          placeholder="请输入用户名"
        />
      </el-form-item>

      <el-form-item v-if="dialogType === 'create'" label="密码" prop="password">
        <el-input
          v-model="form.password"
          type="password"
          placeholder="请输入密码"
          show-password
        />
      </el-form-item>

      <el-form-item v-if="dialogType === 'edit'" label="重置密码">
        <el-input
          v-model="form.newPassword"
          type="password"
          placeholder="输入新密码（留空则不修改）"
          show-password
        />
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleSubmit">确认</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: "UserDialog",
  props: {
    visible: {
      type: Boolean,
      default: false,
    },
    dialogType: {
      type: String,
      default: "create", // 'create' 或 'edit'
    },
    userData: {
      type: Object,
      default: () => ({}),
    },
  },
  data() {
    return {
      form: {
        username: "",
        password: "",
        newPassword: "",
      },
      rules: {
        username: [
          { required: true, message: "请输入用户名", trigger: "blur" },
          {
            min: 2,
            max: 20,
            message: "长度在 2 到 20 个字符",
            trigger: "blur",
          },
        ],
        password: [
          { required: true, message: "请输入密码", trigger: "blur" },
          { min: 6, message: "密码长度至少 6 个字符", trigger: "blur" },
        ],
      },
    };
  },
  watch: {
    visible(val) {
      if (val) {
        this.resetForm();
        if (this.dialogType === "edit") {
          this.form.username = this.userData.username;
        }
      }
    },
  },
  methods: {
    resetForm() {
      this.form = {
        username: "",
        password: "",
        newPassword: "",
      };
    },
    handleClose() {
      this.$emit("update:visible", false);
    },
    handleSubmit() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          if (this.dialogType === "create") {
            this.createUser();
          } else {
            this.updateUser();
          }
        }
      });
    },
    createUser() {
      this.$emit("create", {
        username: this.form.username,
        password: this.form.password,
      });
      this.handleClose();
    },
    updateUser() {
      const updateData = {
        username: this.form.username,
        newPassword: this.form.newPassword,
      };
      this.$emit("update", updateData);
      this.handleClose();
    },
  },
};
</script>

<style scoped>
.dialog-footer {
  text-align: right;
}
</style>