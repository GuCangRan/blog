<template>
  <div id="app" @touchmove.prevent>
    <DDCalendar
      ref="picker"
      model="dialog"
      week-start="sunday"
      picker-type="datetime"
      format="YY/MM/DD"
      :default-datetime="defaultDatetime"
      :markDate="markDate"
      @click="dateClick"
      @change="dateChange"
    />
    <p>选择时间：{{ checkedDate }}</p>
    <button @click="today">回到今天</button>
  </div>
</template>

<script>
import DDCalendar from "./components/DDCalendar";

export default {
  name: "App",
  components: {
    DDCalendar
  },
  data() {
    return {
      defaultDatetime: new Date(),
      checkedDate: "",
      markDate: [
        {
          color: "#999",
          date: ["2020/02/25", "2020/03/25", "2020/04/01", "2020/04/25"]
        },
        {
          color: "red",
          date: ["2020/04/05", "2020/04/20", "2020/06/25"]
        },
        {
          color: "blue",
          date: ["2020/04/08", "2020/04/10"]
        }
      ]
    };
  },
  methods: {
    dateChange(date) {
      // 日期改变触发
      console.log(date, "change");
      this.checkedDate = date;
    },
    dateClick(date) {
      // 点击日期时按钮触发
      console.log(date, "click");
      this.checkedDate = date;
    },
    today() {
      this.$refs.picker.today();
    }
  }
};
</script>

<style lang="scss">
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  height: 100vh;
  background: #ddd;
  overflow: hidden;
}
</style>
