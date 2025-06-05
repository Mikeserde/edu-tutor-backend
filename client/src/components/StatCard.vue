<template>
  <el-card
    shadow="hover"
    class="stat-card"
    :style="{ borderLeft: `4px solid ${color}` }"
  >
    <div class="stat-content">
      <div class="stat-icon" :style="{ background: color }">
        <i :class="icon" style="font-size: 24px; color: white"></i>
      </div>
      <div class="stat-info">
        <div class="stat-title">{{ title }}</div>
        <div class="stat-value">{{ formatValue(value) }}{{ unit }}</div>
        <div v-if="subValue" class="stat-sub">{{ subValue }}</div>
      </div>
    </div>
  </el-card>
</template>

<script>
export default {
  name: "StatCard",
  props: {
    title: {
      type: String,
      required: true,
    },
    value: {
      type: [Number, String],
      required: true,
    },
    icon: {
      type: String,
      default: "el-icon-data-line",
    },
    color: {
      type: String,
      default: "#1890ff",
    },
    unit: {
      type: String,
      default: "",
    },
    subValue: {
      type: String,
      default: "",
    },
  },
  methods: {
    formatValue(val) {
      if (typeof val === "number") {
        return val.toLocaleString();
      }
      return val;
    },
  },
};
</script>

<style scoped>
.stat-card {
  border-radius: 6px;
  margin-bottom: 15px;
}

.stat-content {
  display: flex;
  align-items: center;
}

.stat-icon {
  width: 50px;
  height: 50px;
  border-radius: 10px;
  display: flex;
  justify-content: center;
  align-items: center;
  margin-right: 15px;
}

.stat-info {
  flex: 1;
}

.stat-title {
  font-size: 14px;
  color: #606266;
  margin-bottom: 5px;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
}

.stat-sub {
  font-size: 12px;
  color: #909399;
  margin-top: 2px;
}
</style>