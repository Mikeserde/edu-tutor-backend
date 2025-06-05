
<template>
  <div class="dashboard-container">
    <div class="welcome-container">
      <h1 class="welcome-title">欢迎使用家教后台管理系统</h1>
      <div class="user-greeting">
        <span class="welcome-text">您好，</span>
        <span class="username">{{ name }}</span>
        <span class="welcome-text">！</span>
      </div>
    </div>
  </div>
</template>

<script>
import { mapGetters } from "vuex";

export default {
  name: "home",
  computed: {
    ...mapGetters(["name"]),
  },
};
</script>

<style lang="scss" scoped>
.dashboard-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh; /* 全屏高度 */
  margin: 0; /* 移除原有边距 */
  background-color: #f5f7fa; /* 添加浅色背景 */
}

.welcome-container {
  text-align: center;
  padding: 30px 50px;
  background: white;
  border-radius: 10px;
  box-shadow: 0 6px 18px rgba(0, 0, 0, 0.1);
}

.welcome-title {
  font-size: 3rem; /* 48px */
  font-weight: 700;
  margin-bottom: 30px;
  color: #409eff; /* 主色调 */
  letter-spacing: 2px; /* 字间距 */
}

.user-greeting {
  font-size: 2.2rem; /* 35px */
  margin-top: 15px;
}

.username {
  color: #e6a23c; /* 强调色 */
  font-weight: bold;
}

.welcome-text {
  color: #606266; /* 辅助文字颜色 */
}
</style>
