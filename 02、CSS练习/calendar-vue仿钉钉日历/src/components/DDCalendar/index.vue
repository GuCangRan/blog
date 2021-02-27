<template>
  <div class="calendar_content">
    <Calendar
      ref="calendar"
      :show="true"
      v-bind="{ ...$props, ...$attrs }"
      @height="heightChange"
      :default-date="defaultDatetime"
      @touchstart="touchStart"
      @touchmove="touchMove"
      @touchend="touchEnd"
      @slidechange="slideChange"
      @change="dateChange"
      @click="dateClick"
    >
    </Calendar>
  </div>
</template>

<script>
import Calendar from "./Calendar.vue";
import { formatDate } from "./utils";

export default {
  props: {
    // 是否显示日历组件
    visible: {
      type: Boolean,
      default: false
    },
    pickerType: {
      // 选择器类型 datetime：日期+时间   date：日期   time：时间
      type: String,
      default: "datetime"
    },
    showTodayButton: {
      // 是否显示返回今日按钮
      type: Boolean,
      default: true
    },
    defaultDatetime: {
      // 默认时间
      type: Date,
      default() {
        return new Date();
      }
    },
    format: null, // 确认选择之后，返回的日期格式
    // 日期下面的标记
    markDate: {
      type: Array,
      default: () => []
    },
    // 禁用的日期
    disabledDate: {
      type: Function,
      default: () => {
        return false;
      }
    }
  },
  components: {
    Calendar
  },
  name: "DDCalendar",
  data() {
    return {
      checkedDate: {
        year: new Date().getFullYear(),
        month: new Date().getMonth(),
        day: new Date().getDate(),
        hours: new Date().getHours(),
        minutes: new Date().getMinutes()
      }, // 被选中的日期
      isShowCalendar: false, // 是否显示日历选择控件
      calendarBodyHeight: 0, // 日历内容的高度
      firstTimes: true // 第一次触发
    };
  },
  mounted() {
    if (this.model === "inline") {
      this.isShowDatetimePicker = true;
    }
  },
  watch: {
    defaultDatetime(val) {
      if (!(val instanceof Date)) {
        throw new Error(
          "The calendar component's defaultDate must be date type!"
        );
      }
    },
    pickerType: {
      handler(val) {
        if (val === "time") {
          this.showTime();
        }
      },
      immediate: true
    },
    checkedDate: {
      handler() {
        let date = new Date(
          `${this.checkedDate.year}/${this.checkedDate.month + 1}/${
            this.checkedDate.day
          } ${this.checkedDate.hours}:${this.checkedDate.minutes}`
        );
        if (this.format) {
          date = formatDate(date, this.format);
        }
        this.$emit("change", date);
      },
      deep: true
    },
    visible: {
      handler(val) {
        this.isShowCalendar = val;
      },
      immediate: true
    }
  },
  computed: {
    // 是否显示日期控件
    isShowDatetimePicker: {
      get() {
        return this.visible;
      },
      set(val) {
        this.$emit("update:visible", val);
      }
    },
    // 日历组件的高度
    calendarContentHeight() {
      return this.calendarBodyHeight;
    }
  },
  methods: {
    // 判断是否有插槽
    hasSlot(slotName) {
      return !!this.$scopedSlots[slotName];
    },
    today() {
      if (this.disabledDate(new Date())) return;

      this.$refs.calendar.today();
    },
    dateChange(date) {
      date.hours = this.checkedDate.hours;
      date.minutes = this.checkedDate.minutes;
      this.checkedDate = date;
    },
    dateClick(date) {
      date.hours = this.checkedDate.hours;
      date.minutes = this.checkedDate.minutes;
      this.checkedDate = date;

      let fDate = new Date(
        `${this.checkedDate.year}/${this.checkedDate.month + 1}/${
          this.checkedDate.day
        } ${this.checkedDate.hours}:${this.checkedDate.minutes}`
      );
      if (this.format) {
        fDate = formatDate(fDate, this.format, this.lang);
      }
      this.$emit("click", fDate);
    },
    timeChange(date) {
      date.year = this.checkedDate.year;
      date.month = this.checkedDate.month;
      date.day = this.checkedDate.day;
      this.checkedDate = date;
    },
    // 确认选择时间
    confirm() {
      let date = new Date(
        `${this.checkedDate.year}/${this.checkedDate.month + 1}/${
          this.checkedDate.day
        } ${this.checkedDate.hours}:${this.checkedDate.minutes}`
      );
      if (this.format) {
        date = formatDate(date, this.format, this.lang);
      }
      this.$emit("confirm", date);
      if (this.model === "dialog") {
        this.close();
      }
    },
    show() {
      this.isShowDatetimePicker = true;
    },
    close() {
      this.isShowDatetimePicker = false;
    },
    // 小于10，在前面补0
    fillNumber(val) {
      return val > 9 ? val : "0" + val;
    },
    formatDate(time, format) {
      return formatDate(time, format, this.lang);
    },
    // 显示日历控件
    showCalendar() {
      this.isShowCalendar = true;
    },
    // 显示时间选择控件
    showTime() {
      this.isShowCalendar = false;
    },
    // 高度变化
    heightChange(height) {
      if (!this.firstTimes && this.model === "dialog") return;

      this.calendarBodyHeight = height;
      this.firstTimes = false;
    },
    // 监听手指开始滑动事件
    touchStart(event) {
      this.$emit("touchstart", event);
    },
    // 监听手指开始滑动事件
    touchMove(event) {
      this.$emit("touchmove", event);
    },
    // 监听手指开始滑动事件
    touchEnd(event) {
      this.$emit("touchend", event);
    },
    // 滑动方向改变
    slideChange(direction) {
      this.$emit("slidechange", direction);
    }
  }
};
</script>

<style lang="scss" scoped>
@import "./common.scss";
.calendar_content {
  width: 100%;
  display: flex;
  flex-wrap: wrap;
  background: white;
  overflow: hidden;
}
.calendar_title {
  position: absolute;
  width: 100%;
  left: 0;
  top: 0;
  background: #f4f4f4;
  border-bottom: 1px solid #f4f4f4;
  display: flex;
  align-items: center;
  justify-content: space-between;
  z-index: 1;
}
.calendar_title_date {
  color: #898989;
  background: white;
  padding: px2vw(30) px2vw(15);
}
.calendar_title_date_active {
  color: #4c4c4c;
  font-weight: bold;
}
.calendar_title_date_time {
  margin-left: px2vw(20);
}
.calendar_confirm {
  color: #1c71fb;
  margin-right: px2vw(34);
}
.today_disable {
  color: #c0c4cc;
}
</style>
