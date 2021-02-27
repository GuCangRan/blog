<template>
  <div
    class="calendar_body"
    :style="{
      height: `${calendarBodyHeight}px`
    }"
  >
    <div class="calendar_week" ref="weekTitle">
      <div class="calendar_item" v-for="item in calendarWeek" :key="item">
        <p class="calendar_weekday">
          <slot name="week" :week="item">{{ item }}</slot>
        </p>
      </div>
    </div>
    <div
      class="calendar_group"
      :style="{ height: `${calendarGroupHeight}px` }"
      ref="calendar"
      @touchstart="touchStart"
      @touchmove.stop.prevent="touchMove"
      @touchend="touchEnd"
    >
      <div
        class="calendar_week_preview"
        :style="{ height: `${calendarItemHeight}px` }"
        v-show="weekPreviewShow"
      >
        <ul
          :style="{
            transform: `translate3d(${-translateIndex * 100}%, 0, 0)`,
            height: `${calendarItemHeight}px`
          }"
        >
          <li
            class="calendar_week_item"
            v-for="(item, index) in threeWeek"
            :key="index"
            :style="{
              transform: `translate3d(${(index - 1 + translateIndex + touch.x) *
                100}%, 0, 0)`,
              transitionDuration: `${isTouching ? 0 : transitionDuration}s`
            }"
          >
            {{ item }}
          </li>
        </ul>
      </div>
      <ul
        v-show="!weekPreviewShow"
        :style="{
          transform: `translate3d(${-translateIndex * 100}%, 0, 0)`,
          height: isShowWeek
            ? isTouching === 'y'
              ? `${calendarItemHeight * 6}px`
              : `${calendarItemHeight}px`
            : `${calendarItemHeight * 6}px`
        }"
      >
        <li
          class="calendar_group_li"
          v-for="(item, i) in calendarOfMonthShow"
          :key="i"
          :style="{
            transform: `translate3d(${(i -
              1 +
              translateIndex +
              (isTouching ? touch.x : 0)) *
              100}%, ${
              isShowWeek && isTouching !== 'y'
                ? calendarY
                : isTouching
                ? touch.y > calendarY
                  ? touch.y
                  : calendarY
                : 0
            }px, 0)`,
            transitionDuration: `${isTouching ? 0 : transitionDuration}s`
          }"
        >
          <div
            class="calendar_month"
            :style="{ opacity: isShowWeek ? 0 : 0.2 }"
          >
            {{ constant.MONTH[item[20].month] }}
          </div>
          <div
            class="calendar_item"
            ref="calendarItem"
            v-for="(date, j) in item"
            :class="[
              formatDisabledDate(date) &&
                (disabledClassName || 'calendar_item_disable')
            ]"
            :key="i + j"
            @click="clickCalendarDay(date)"
          >
            <div
              class="calendar_day"
              :style="{ 'border-color': markDateColor(date, 'circle') }"
              :class="[
                isToday(date) && (todayClassName || 'calendar_day_today'),
                isCheckedDay(date) &&
                  (checkedDayClassName || 'calendar_day_checked'),
                isNotCurrentMonthDay(date, i) &&
                  (notCurrentMonthDayClassName || 'calendar_day_not'),
                markDateColor(date, 'circle') && 'calendar_mark_circle'
              ]"
            >
              <div class="calendar_solarday">
                {{ isToday(date) ? "今" : date.day }}
              </div>
              <div class="calendar_lunarday">{{ getLunar(date) }}</div>
              <div
                :style="{
                  background:
                    markDateColor(date, 'dot') && isCheckedDay(date)
                      ? '#fff'
                      : markDateColor(date, 'dot')
                }"
                class="calendar_dot"
              ></div>
            </div>
          </div>
        </li>
      </ul>
    </div>
    <div
      class="calendar_arrow"
      ref="arrow"
      @touchstart="touchStart"
      @touchmove.stop.prevent="touchMove"
      @touchend="touchEnd"
      @click.stop.prevent="toggle"
    >
      <img
        :class="{ calendar_arrow_down: isShowWeek }"
        src="./img/arrow-up.svg"
      />
    </div>
  </div>
</template>

<script>
import { formatDate, lunar } from "./utils";
import constant from "./constant";
export default {
  name: "Calendar",
  props: {
    // 每月第一天的 className
    firstDayOfMonthClassName: {
      type: String,
      default: ""
    },
    // 当天日期的 className
    todayClassName: {
      type: String,
      default: ""
    },
    // 日期被选中时的 className
    checkedDayClassName: {
      type: String,
      default: ""
    },
    // 不是当前展示月份日期的 className(例如日历前面几天与后面几天灰色部分)
    notCurrentMonthDayClassName: {
      type: String,
      default: ""
    },
    // 日期被禁用时的 className
    disabledClassName: {
      type: String,
      default: ""
    },
    // 滑动的时候，是否触发改变日期
    scrollChangeDate: {
      type: Boolean,
      default: true
    },
    // 禁用周视图
    disabledWeekView: {
      type: Boolean,
      default: false
    },
    defaultDate: {
      type: Date,
      default() {
        return new Date();
      }
    },
    show: {
      type: Boolean,
      default: false
    },
    weekStart: {
      type: String,
      default: "Sunday"
    },
    // 日期下面的标记
    markDate: {
      type: Array,
      default: () => []
    },
    // 日期标记类型
    markType: {
      type: String,
      default: "dot"
    },
    // 禁用的日期
    disabledDate: {
      type: Function,
      default: () => {
        return false;
      }
    }
  },
  computed: {
    constant() {
      return constant;
    },
    lunar() {
      return lunar;
    }
  },
  data() {
    return {
      currentChangeIsScroll: false, // 改变当前日期的方式是否为滑动事件
      yearOfCurrentShow: new Date().getFullYear(), // 当前日历展示的年份
      monthOfCurrentShow: new Date().getMonth(), // 当前日历展示的月份
      yearOfToday: new Date().getFullYear(), // 今天所在的年份
      monthOfToday: new Date().getMonth(), // 今天所在的月份
      dayOfToday: new Date().getDate(), // 今天所在的日期
      weekArray: [
        "sunday",
        "monday",
        "tuesday",
        "wednesday",
        "thursday",
        "friday",
        "saturday"
      ], // 星期数组
      calendarWeek: ["日", "一", "二", "三", "四", "五", "六"], // 日历对应的星期
      calendarOfMonth: [], // 月份对应的日历表
      calendarOfMonthShow: [], // 月份对应的日历表
      calendarDaysTotalLength: 42, // 日历表展示的总天数  6行7列
      lastMonthYear: null, // 上个月的年份
      lastMonth: null, // 上个月的月份
      nextMonthYear: null, // 下个月的年份
      nextMonth: null, // 下个月的月份
      checkedDate: {}, // 被选中的日期
      weekStartIndex: 0, // 日历第一天星期名称的index
      translateIndex: 0, // 用于计算上下偏移的距离
      transitionDuration: 0.2, // 动画持续时间
      touch: {
        x: 0,
        y: 0
      }, // 本次touch事件，横向，纵向滑动的距离
      deltaY: 0, //touchY 偏差值
      isTouching: false, // 是否正在滑动
      calendarGroupHeight: 0,
      calendarWeekTitleHeight: 0,
      calendarItemHeight: 0,
      calendarArrowHeight: 0,
      calendarBodyHeight: 0,
      maxBodyHeight: 0,
      minBodyHeight: 0,
      touchStartPositionX: null, // 开始滑动x轴的值
      touchStartPositionY: null, // 开始滑动时y轴的值
      isShowWeek: false, // 当前日历是否以星期方式展示
      calendarY: 0, // 日历相对于Y轴的位置
      selectedDayIndex: 0, // 当前选中的日期，在这一周的第几天
      lastWeek: [], // 上一周的数据
      nextWeek: [], // 下一周的数据
      isLastWeekInCurrentMonth: false, // 上一周的数据是否在本月
      isNextWeekInCurrentMonth: false, // 下一周的数据是否在本月
      markDateColorObj: [], // 所有被标记的日期所对应的颜色
      weekPreviewShow: false, // 滑动时星期文字预览开关
      threeWeek: ["", "", ""], // 滑动时星期文字预览
      currentWeek: [] //当前周
    };
  },
  mounted() {
    this.calendarWeek = constant.WEEK;
    this.weekStartIndex = this.weekArray.indexOf(this.weekStart.toLowerCase());
    this.calendarWeek = [
      ...this.calendarWeek.slice(this.weekStartIndex, this.calendarWeek.length),
      ...this.calendarWeek.slice(0, this.weekStartIndex)
    ];
  },
  watch: {
    markDate: {
      handler(val) {
        val.forEach((item, index) => {
          if (item.color === undefined) {
            let obj = {};
            obj.color = "#1c71fb";
            if (typeof item === "string" || typeof item === "number") {
              item = [item];
            }
            obj.date = item || [];
            val[index] = obj;
          }

          /* val[index].forEach(dateObj => {
            this.$set(this.markDateColorObj, this.formatDate(dateObj.date), dateObj.color)
          }) 待简化 */

          val[index].date = this.dateFormat(val[index].date);
        });

        this.markDateColorObj = [];
        val.forEach(item => {
          item.date.forEach(date => {
            this.$set(this.markDateColorObj, date, item.color);
          });
        });
      },
      deep: true,
      immediate: true
    },
    weekStartIndex() {
      this.calculateCalendarOfThreeMonth(
        this.checkedDate.year,
        this.checkedDate.month
      );
    },
    defaultDate: {
      handler(val) {
        if (!(val instanceof Date)) {
          throw new Error(
            "The calendar component's defaultDate must be date type!"
          );
        }

        this.$set(this.checkedDate, "day", val.getDate());
        this.calculateCalendarOfThreeMonth(val.getFullYear(), val.getMonth());
      },
      immediate: true
    },
    checkedDate: {
      handler(val) {
        this.$emit("change", val);
        this.setCurrentWeek(val);
      },
      deep: true,
      immediate: true
    },
    show: {
      handler(val) {
        if (val) {
          this.calculateCalendarOfThreeMonth(
            this.checkedDate.year,
            this.checkedDate.month
          );
          this.initDom();
        }
      },
      immediate: true
    },
    calendarGroupHeight(val) {
      this.$emit("height", val + this.calendarWeekTitleHeight);
    }
  },
  methods: {
    // 初始化日历dom
    initDom() {
      this.$nextTick(() => {
        this.calendarItemHeight =
          this.$refs.calendarItem && this.$refs.calendarItem[0].offsetHeight;
        this.calendarWeekTitleHeight = this.$refs.weekTitle.offsetHeight;
        this.calendarArrowHeight = this.$refs.arrow.offsetHeight;
        let calendarItemGroup = this.$refs.calendarItem;
        calendarItemGroup.forEach(item => {
          item.style.height = `${this.calendarItemHeight}px`;
        });
        this.calendarGroupHeight = this.calendarItemHeight * 6;
        this.maxBodyHeight =
          this.calendarGroupHeight +
          this.calendarWeekTitleHeight +
          this.calendarArrowHeight;
        this.minBodyHeight =
          this.calendarItemHeight +
          this.calendarWeekTitleHeight +
          this.calendarArrowHeight;
        this.calendarBodyHeight = this.maxBodyHeight;
        this.setCurrentWeek();
        this.showMonth();
      });
    },
    // 今天
    today() {
      this.$set(this.checkedDate, "day", new Date().getDate());

      this.yearOfCurrentShow = new Date().getFullYear(); // 当前日历展示的年份
      this.monthOfCurrentShow = new Date().getMonth(); // 当前日历展示的月份

      this.calculateCalendarOfThreeMonth();

      if (this.isShowWeek) {
        setTimeout(() => {
          this.isTouching = true;
          this.showWeek();
        }, this.transitionDuration * 1000);
      }
    },
    // 计算农历
    getLunar(date) {
      const { year, month, day } = date;
      const { festival, lunarFestival, Term, IDayCn } = lunar.solar2lunar(
        year,
        month + 1,
        day
      );
      return festival || lunarFestival || Term || IDayCn;
    },
    getThreeWeek() {
      function renderWeek(week) {
        return week.length > 0
          ? `${week[0].month + 1}月${week[0].day}日 - ${week[6].month + 1}月${
              week[6].day
            }日`
          : "";
      }
      this.threeWeek = [
        renderWeek(this.lastWeek),
        renderWeek(this.currentWeek),
        renderWeek(this.nextWeek)
      ];
    },
    // 是否为当前月的第一天
    isFirstDayOfMonth(date, i) {
      return date.day === 1 && !this.isNotCurrentMonthDay(date, i);
    },
    // 计算当前展示月份的前后月份日历信息 flag  -1:获取上个月日历信息   0:当月信息或者跨月展示日历信息  1:获取下个月日历信息
    calculateCalendarOfThreeMonth(
      year = new Date().getFullYear(),
      month = new Date().getMonth()
    ) {
      this.lastMonthYear = month === 0 ? year - 1 : year; // 上个月的年份
      this.lastMonth = month === 0 ? 11 : month - 1; // 上个月的月份
      this.nextMonthYear = month === 11 ? year + 1 : year; // 下个月的年份
      this.nextMonth = month === 11 ? 0 : month + 1; // 下个月的月份

      let firstMonth = this.calculateCalendarOfMonth(
        this.lastMonthYear,
        this.lastMonth
      );
      let secondMonth = this.calculateCalendarOfMonth(year, month);
      let thirdMonth = this.calculateCalendarOfMonth(
        this.nextMonthYear,
        this.nextMonth
      );

      this.calendarOfMonth = [];
      this.calendarOfMonth.push(firstMonth, secondMonth, thirdMonth);
      this.calendarOfMonthShow = JSON.parse(
        JSON.stringify(this.calendarOfMonth)
      );

      if (!this.scrollChangeDate && this.currentChangeIsScroll) {
        this.currentChangeIsScroll = false;
        return;
      }

      // 改变日期选择的日期
      let tempDate = {};
      let day = this.checkedDate.day;
      if (day > 30 || (day > 28 && month === 1)) {
        day = this.daysOfMonth(year)[month];
      }
      tempDate = { day: day, year: year, month: month };

      if (this.formatDisabledDate(tempDate)) return;

      this.$set(this.checkedDate, "day", tempDate.day);
      this.$set(this.checkedDate, "year", year);
      this.$set(this.checkedDate, "month", month);
    },
    // 计算每个月的日历
    calculateCalendarOfMonth(
      year = new Date().getFullYear(),
      month = new Date().getMonth()
    ) {
      let calendarOfCurrentMonth = [];

      let lastMonthYear = month === 0 ? year - 1 : year; // 上个月的年份
      let lastMonth = month === 0 ? 11 : month - 1; // 上个月的月份
      let nextMonthYear = month === 11 ? year + 1 : year; // 下个月的年份
      let nextMonth = month === 11 ? 0 : month + 1; // 下个月的月份

      // 如果当月第一天不是指定的开始星期名称，则在前面补齐上个月的日期
      let dayOfWeek = this.getDayOfWeek(year, month);
      let lastMonthDays = this.daysOfMonth(year)[lastMonth]; // 上个月的总天数
      if (dayOfWeek < this.weekStartIndex) {
        dayOfWeek = 7 - this.weekStartIndex + dayOfWeek;
      } else {
        dayOfWeek -= this.weekStartIndex;
      }
      for (let i = 0; i < dayOfWeek; i++) {
        calendarOfCurrentMonth.push({
          year: lastMonthYear,
          month: lastMonth,
          day: lastMonthDays - (dayOfWeek - 1 - i)
        });
      }

      // 当月日期
      for (let i = 0; i < this.daysOfMonth(year)[month]; i++) {
        calendarOfCurrentMonth.push({
          year: year,
          month: month,
          day: i + 1
        });
      }

      // 在日历后面填充下个月的日期，补齐6行7列
      let fillDays =
        this.calendarDaysTotalLength - calendarOfCurrentMonth.length;
      for (let i = 0; i < fillDays; i++) {
        calendarOfCurrentMonth.push({
          year: nextMonthYear,
          month: nextMonth,
          day: i + 1
        });
      }

      return calendarOfCurrentMonth;
    },
    daysOfMonth(year) {
      return [
        31,
        28 + this.isLeap(year),
        31,
        30,
        31,
        30,
        31,
        31,
        30,
        31,
        30,
        31
      ];
    },
    // 判断是否为闰年
    isLeap(year) {
      return year % 4 === 0
        ? year % 100 !== 0
          ? 1
          : year % 400 === 0
          ? 1
          : 0
        : 0;
    },
    // 获取月份某一天是星期几
    getDayOfWeek(
      year = new Date().getFullYear(),
      month = new Date().getMonth(),
      day = 1
    ) {
      let dayOfMonth = new Date(year, month, day); // 获取当月的第day天
      let dayOfWeek = dayOfMonth.getDay(); // 判断第day天是星期几(返回[0-6]中的一个，0代表星期天，1代表星期一)
      return dayOfWeek;
    },
    // 点击日历上的日期
    clickCalendarDay(date) {
	if(pageWin){
		pageWin("20200221");
	}
      if (!date) return;

      if (this.formatDisabledDate(date)) return;

      this.$set(this.checkedDate, "year", date.year);
      this.$set(this.checkedDate, "month", date.month);
      this.$set(this.checkedDate, "day", date.day);

      if (date.month === this.lastMonth && date.year === this.lastMonthYear) {
        this.getLastMonth();
      }
      if (date.month === this.nextMonth && date.year === this.nextMonthYear) {
        this.getNextMonth();
      }

      if (this.isShowWeek) {
        this.showWeek();
      }

      this.$emit("click", this.checkedDate);
    },
    // 计算当前周
    setCurrentWeek(checkedDate = this.checkedDate) {
      let daysArr = [];
      this.calendarOfMonth[1].forEach(item => {
        daysArr.push(item.day);
      });
      let dayIndexOfMonth = daysArr.indexOf(checkedDate.day);
      // 当day为月底的天数时，有可能在daysArr的前面也存在上一个月对应的日期，所以需要取lastIndexOf
      if (checkedDate.day > 15) {
        dayIndexOfMonth = daysArr.lastIndexOf(checkedDate.day);
      }

      // 计算当前日期在第几行
      let indexOfLine = Math.ceil((dayIndexOfMonth + 1) / 7);
      let lastLine = indexOfLine - 1;
      this.calendarY = -(this.calendarItemHeight * lastLine);
      let sliceStart = lastLine * 7;
      let sliceEnd = sliceStart + 7;
      this.currentWeek = this.calendarOfMonth[1].slice(sliceStart, sliceEnd);
    },
    // 该日期是否为今天
    isToday(date) {
      return (
        this.yearOfToday === date.year &&
        this.monthOfToday === date.month &&
        this.dayOfToday === date.day
      );
    },
    // 该日期是否为选中的日期
    isCheckedDay(date) {
      if (this.formatDisabledDate(date)) return false;

      return (
        this.checkedDate.year === date.year &&
        this.checkedDate.month === date.month &&
        this.checkedDate.day === date.day
      );
    },
    // 非本月日期
    isNotCurrentMonthDay(date, index) {
      let dateOfCurrentShow = this.calendarOfMonth[index][15]; // 本月中间的日期一定为本月
      return (
        date.year !== dateOfCurrentShow.year ||
        date.month !== dateOfCurrentShow.month
      );
    },
    // 监听手指开始滑动事件
    touchStart(event) {
      this.$emit("touchstart", event);
      this.touchStartPositionX = event.touches[0].clientX;
      this.touchStartPositionY = event.touches[0].clientY;
      this.touch = {
        x: 0,
        y: this.isShowWeek ? this.calendarY : 0
      };
      this.deltaY = 0;
      this.isTouching = true;
    },
    // 监听手指移动事件
    touchMove(event) {
      this.$emit("touchmove", event);
      let moveX = event.touches[0].clientX - this.touchStartPositionX;
      let moveY = event.touches[0].clientY - this.touchStartPositionY;
      // 确定滑动方向
      if (typeof this.isTouching !== "string") {
        if (Math.abs(moveX) > Math.abs(moveY)) {
          this.isTouching = "x";
          if (this.isShowWeek) {
            this.getThreeWeek();
            this.weekPreviewShow = true;
          }
        } else {
          // 禁用周视图（禁止上下滑动）
          if (this.disabledWeekView) return;
          this.isTouching = "y";
        }
      } else {
        if (this.isTouching === "x") {
          this.touch = {
            x: moveX / this.$refs.calendar.offsetWidth,
            y: 0
          };
        } else {
          let calendarBodyHeight =
            (this.isShowWeek ? this.minBodyHeight : this.maxBodyHeight) + moveY;
          if (calendarBodyHeight > this.maxBodyHeight) {
            calendarBodyHeight = this.maxBodyHeight;
          }
          if (calendarBodyHeight < this.minBodyHeight) {
            calendarBodyHeight = this.minBodyHeight;
          }
          this.calendarBodyHeight = calendarBodyHeight;
          this.calendarGroupHeight = this.calendarItemHeight * 6;
          let y = moveY;
          if (this.isShowWeek) {
            let leftLine = 5 + this.calendarY / this.calendarItemHeight;
            y =
              y > leftLine * this.calendarItemHeight
                ? this.calendarY + y - leftLine * this.calendarItemHeight
                : this.calendarY;
          } else {
            y = y < 0 ? y : 0;
          }
          this.touch = {
            x: 0,
            y
          };
          this.deltaY = moveY;
        }
      }
    },
    // 监听touch结束事件
    touchEnd(e) {
      this.$emit("touchend", e);
      this.isTouching = false;
      if (this.isShowWeek) {
        setTimeout(() => {
          this.weekPreviewShow = false;
        }, 800);
      }
      if (
        Math.abs(this.touch.x / this.$refs.calendar.offsetWidth) >
          Math.abs(this.touch.y) &&
        Math.abs(this.touch.x) > 0.2
      ) {
        this.currentChangeIsScroll = true;
        if (this.touch.x > 0) {
          this.$emit("slidechange", "right");

          this.getLastMonth();
          if (this.isShowWeek) {
            setTimeout(() => {
              this.isTouching = true;
              this.currentChangeIsScroll = true;
              this.getLastWeek();
            }, this.transitionDuration * 1000);
          }
        } else if (this.touch.x < 0) {
          this.$emit("slidechange", "left");

          this.getNextMonth();
          if (this.isShowWeek) {
            setTimeout(() => {
              this.isTouching = true;
              this.currentChangeIsScroll = true;
              this.getNextWeek();
            }, this.transitionDuration * 1000);
          }
        }
      }

      if (
        Math.abs(this.deltaY) >
          Math.abs(this.touch.x / this.$refs.calendar.offsetWidth) &&
        Math.abs(this.deltaY) > 20
      ) {
        if (this.deltaY > 0 && this.isShowWeek) {
          this.showMonth();
        } else if (this.deltaY < 0 && !this.isShowWeek) {
          this.showWeek();
        }
      } else {
        this.touch = {
          x: 0,
          y: 0
        };
      }
    },
    // 日历以月份方式展示
    showMonth() {
      this.$emit("slidechange", "down");
      this.calendarBodyHeight = this.maxBodyHeight;
      this.calendarGroupHeight = this.calendarItemHeight * 6;
      this.isShowWeek = false;
      this.isLastWeekInCurrentMonth = false;
      this.isNextWeekInCurrentMonth = false;

      this.calculateCalendarOfThreeMonth(
        this.checkedDate.year,
        this.checkedDate.month
      );
    },
    // 日历以星期方式展示
    showWeek(checkedDate = this.checkedDate) {
      this.$emit("slidechange", "up");
      this.calendarBodyHeight = this.minBodyHeight;
      let daysArr = [];
      this.calendarOfMonth[1].forEach(item => {
        daysArr.push(item.day);
      });
      let dayIndexOfMonth = daysArr.indexOf(checkedDate.day);
      // 当day为月底的天数时，有可能在daysArr的前面也存在上一个月对应的日期，所以需要取lastIndexOf
      if (checkedDate.day > 15) {
        dayIndexOfMonth = daysArr.lastIndexOf(checkedDate.day);
      }

      // 计算当前日期在第几行
      let indexOfLine = Math.ceil((dayIndexOfMonth + 1) / 7);
      let lastLine = indexOfLine - 1;
      this.calendarY = -(this.calendarItemHeight * lastLine);

      this.isShowWeek = true;
      this.calendarGroupHeight = this.calendarItemHeight;

      let currentWeek = [];
      let sliceStart = lastLine * 7;
      let sliceEnd = sliceStart + 7;
      this.isLastWeekInCurrentMonth = false;
      currentWeek = this.calendarOfMonth[1].slice(sliceStart, sliceEnd);
      for (let i in currentWeek) {
        if (currentWeek[i].day === checkedDate.day) {
          this.selectedDayIndex = i;
        }
      }

      let firstDayOfCurrentWeek = currentWeek[0];
      let lastDayOfCurrentWeek = currentWeek[6];
      if (
        lastDayOfCurrentWeek.day < firstDayOfCurrentWeek.day &&
        lastDayOfCurrentWeek.month === checkedDate.month
      ) {
        this.lastWeek = this.calendarOfMonth[0].slice(21, 28);
      } else {
        if (firstDayOfCurrentWeek.day === 1) {
          this.lastWeek = this.calendarOfMonth[0].slice(28, 35);
        } else {
          this.lastWeek = this.calendarOfMonth[1].slice(
            sliceStart - 7,
            sliceEnd - 7
          );
          if (
            this.lastWeek[this.selectedDayIndex].month === checkedDate.month
          ) {
            this.isLastWeekInCurrentMonth = true;
          }
        }
      }

      this.isNextWeekInCurrentMonth = false;
      if (
        lastDayOfCurrentWeek.day < firstDayOfCurrentWeek.day &&
        lastDayOfCurrentWeek.month !== checkedDate.month
      ) {
        this.nextWeek = this.calendarOfMonth[2].slice(7, 14);
      } else {
        if (
          lastDayOfCurrentWeek.day ===
          this.daysOfMonth(lastDayOfCurrentWeek.year)[
            lastDayOfCurrentWeek.month
          ]
        ) {
          this.nextWeek = this.calendarOfMonth[2].slice(0, 7);
        } else {
          this.nextWeek = this.calendarOfMonth[1].slice(
            sliceStart + 7,
            sliceEnd + 7
          );
          if (
            this.nextWeek[this.selectedDayIndex].month === checkedDate.month
          ) {
            this.isNextWeekInCurrentMonth = true;
          }
        }
      }
      this.calendarOfMonthShow[0].splice(sliceStart, 7, ...this.lastWeek);
      this.calendarOfMonthShow[2].splice(sliceStart, 7, ...this.nextWeek);
    },
    toggle() {
      if (this.isShowWeek) {
        this.showMonth();
      } else {
        this.showWeek();
      }
    },
    // 显示上一周
    getLastWeek() {
      let checkedDate = this.lastWeek[this.selectedDayIndex];
      this.showWeek(checkedDate);

      if (this.formatDisabledDate(checkedDate)) return;

      if (!this.scrollChangeDate && this.currentChangeIsScroll) {
        this.currentChangeIsScroll = false;
        return;
      }

      this.checkedDate = checkedDate;
    },
    // 显示下一周
    getNextWeek() {
      let checkedDate = this.nextWeek[this.selectedDayIndex];
      this.showWeek(checkedDate);

      if (this.formatDisabledDate(checkedDate)) return;

      if (!this.scrollChangeDate && this.currentChangeIsScroll) {
        this.currentChangeIsScroll = false;
        return;
      }

      this.checkedDate = checkedDate;
    },
    // 获取上个月日历
    getLastMonth() {
      this.translateIndex += 1;

      if (!this.isLastWeekInCurrentMonth) {
        this.yearOfCurrentShow = this.lastMonthYear;
        this.monthOfCurrentShow = this.lastMonth;
      }
      this.calculateCalendarOfThreeMonth(
        this.yearOfCurrentShow,
        this.monthOfCurrentShow
      );
    },
    // 获取下个月日历
    getNextMonth() {
      this.translateIndex -= 1;

      if (!this.isNextWeekInCurrentMonth) {
        this.yearOfCurrentShow = this.nextMonthYear;
        this.monthOfCurrentShow = this.nextMonth;
      }
      this.calculateCalendarOfThreeMonth(
        this.yearOfCurrentShow,
        this.monthOfCurrentShow
      );
    },
    // 当前日期是否需要标记
    markDateColor(date, type) {
      if (this.markType.indexOf(type) === -1) return;

      let dateString = `${date.year}/${this.fillNumber(
        date.month + 1
      )}/${this.fillNumber(date.day)}`;
      return this.markDateColorObj[dateString];
    },
    formatDisabledDate(date) {
      let fDate = new Date(`${date.year}/${date.month + 1}/${date.day}`);

      return this.disabledDate(fDate);
    },
    // 小于10，在前面补0
    fillNumber(val) {
      return val > 9 ? val : "0" + val;
    },
    // 日期格式转换
    dateFormat(dateArr) {
      dateArr.forEach((date, index) => {
        dateArr[index] = formatDate(date, "YY/MM/DD");
      });

      return dateArr;
    }
  }
};
</script>

<style lang="scss" scoped>
@import "./calendar.scss";
</style>
