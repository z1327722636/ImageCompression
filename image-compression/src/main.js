import Vue from 'vue'
import Antd from 'ant-design-vue'
import 'ant-design-vue/dist/antd.css'
import App from './App.vue'
import router from './router'
import FileUpload from './components/FileUpload.vue'
import store from './store'

Vue.config.productionTip = false
Vue.use(Antd)
Vue.component('FileUpload', FileUpload)
new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
