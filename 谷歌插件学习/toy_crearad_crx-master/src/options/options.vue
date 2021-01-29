<template>
  <div class="top"></div>
  <div class="container">
    <div class="tabs-wap">
      <a-tabs @change="callback">
        <a-tab-pane key="1" tab="设置">
          <LabelItem label="添加网址">
            <Setting/>
          </LabelItem>
        </a-tab-pane>
        <a-tab-pane key="3" tab="关于"> 
          <LabelItem>

          </LabelItem>
        </a-tab-pane>
      </a-tabs>
    </div>
  </div>
</template>

<script>
import LabelItem from './LabelItem';
import Setting from './Setting';
import { Tabs } from "ant-design-vue";
export default {
  components: {
    "a-tabs": Tabs,
    "a-tab-pane": Tabs.TabPane,
    LabelItem,
    Setting
  },
  data() {
    return {};
  },
  methods: {
    callback(key) {
      console.log(key);
    },
  },
};
</script>
<style>
#app {
  height: 100%;
  background-color: #fafafa;
}
.container {
  position: relative;
  top: 198px;
  width: 800px;
  margin: 0 auto;
}
.top {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 249px;
  background-color: #2196f3;
  overflow: hidden;
  -webkit-transition: all 0.45s cubic-bezier(0.23, 1, 0.32, 1) 0ms;
  transition: all 0.45s cubic-bezier(0.23, 1, 0.32, 1) 0ms;
}
.tabs-wap {
  position: absolute;
  width: 800px;
}
.tabs-wap .ant-tabs-bar {
  margin-bottom: 0;
}
.tabs-wap .ant-tabs-nav-container {
  background-color: rgb(100, 181, 246);
  transition: all 1000ms cubic-bezier(0.23, 1, 0.32, 1) 0ms;
}
.tabs-wap .ant-tabs-nav-container {
  font-size: 18px;
  color: rgba(255, 255, 255, 0.7);
}
.tabs-wap .ant-tabs-ink-bar {
  height: 5px;
  background-color: #ff9f43;
}
.tabs-wap .ant-tabs-tab-active {
  color: #fff;
}
.tabs-wap .ant-tabs-nav .ant-tabs-tab:hover  {
  color: #fff;
}
</style>
