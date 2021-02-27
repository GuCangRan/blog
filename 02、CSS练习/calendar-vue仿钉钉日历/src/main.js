import Vue from "vue";
import App from "./App.vue";
import "normalize.css";
Vue.config.productionTip = false;
import VConsole from "vconsole";
// eslint-disable-next-line no-unused-vars
var vConsole = new VConsole();
new Vue({
  render: h => h(App)
}).$mount("#calendar_weekly");