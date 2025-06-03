<template>
  <el-dialog
    :title="dialogType === 'create' ? '添加排班' : '编辑排班'"
    :visible.sync="dialogVisible"
    width="650px"
    @close="handleClose"
  >
    <el-form
      ref="scheduleForm"
      :model="tempSchedule"
      :rules="rules"
      label-width="120px"
      label-position="right"
    >
      <el-form-item label="职业ID" prop="occupationId">
        <el-input-number
          v-model="tempSchedule.occupationId"
          :min="1"
          placeholder="请输入职业ID"
          controls-position="right"
          style="width: 100%"
        />
      </el-form-item>
      <el-form-item label="教师ID" prop="teacherId">
        <el-input-number
          v-model="tempSchedule.teacherId"
          :min="1"
          placeholder="请输入教师ID"
          controls-position="right"
          style="width: 100%"
        />
      </el-form-item>
      <el-form-item label="日期" prop="date">
        <el-date-picker
          v-model="tempSchedule.date"
          type="date"
          placeholder="选择日期"
          format="yyyy-MM-dd"
          value-format="yyyy-MM-dd"
          style="width: 100%"
        />
      </el-form-item>
      <el-form-item label="开始时间" prop="startTime">
        <el-time-select
          v-model="tempSchedule.startTime"
          :picker-options="{
            start: '06:00',
            step: '00:15',
            end: '22:00',
          }"
          placeholder="开始时间"
          style="width: 100%"
        />
      </el-form-item>
      <el-form-item label="结束时间" prop="endTime">
        <el-time-select
          v-model="tempSchedule.endTime"
          :picker-options="{
            start: '06:00',
            step: '00:15',
            end: '22:00',
            minTime: tempSchedule.startTime,
          }"
          placeholder="结束时间"
          style="width: 100%"
        />
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="close" :disabled="submitting">取消</el-button>
      <el-button type="primary" :loading="submitting" @click="handleSubmit">
        确认
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { parseTime } from "@/utils";

export default {
  name: "ScheduleDialog",
  props: {
    visible: Boolean,
    type: String,
    scheduleData: {
      type: Object,
      default: () => ({}),
    },
    rules: {
      type: Object,
      default: () => ({}),
    },
    submitting: Boolean, // 添加此属性同步提交状态
  },
  data() {
    return {
      dialogVisible: this.visible,
      dialogType: this.type,
      tempSchedule: this.getDefaultSchedule(),
    };
  },
  watch: {
    visible(newVal) {
      this.dialogVisible = newVal;
    },
    type(newVal) {
      this.dialogType = newVal;
    },
    scheduleData: {
      immediate: true,
      deep: true,
      handler(newVal) {
        this.tempSchedule = { ...this.getDefaultSchedule(), ...newVal };

        // 如果日期存在，格式化日期
        if (this.tempSchedule.date) {
          this.tempSchedule.date = parseTime(
            this.tempSchedule.date,
            "{y}-{m}-{d}"
          );
        }
      },
    },
  },
  methods: {
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

    handleClose() {
      this.$emit("update:visible", false);
      this.$emit("update:submitting", false); // 重置提交状态
    },

    close() {
      this.dialogVisible = false;
      this.$emit("update:visible", false);
      this.$emit("update:submitting", false); // 重置提交状态
    },

    handleSubmit() {
      this.$refs.scheduleForm.validate((valid) => {
        if (valid) {
          // 通知父组件开始提交
          this.$emit("update:submitting", true);

          this.$emit("submit", {
            type: this.dialogType,
            data: { ...this.tempSchedule },
          });
        }
      });
    },
  },
};
</script>