import { createApp } from 'vue'
import App from './popup.vue'
import useCore from '../use-core'

createApp(App).use(useCore).mount('#app');
