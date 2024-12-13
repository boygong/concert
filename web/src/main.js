import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import Antd, { notification } from 'ant-design-vue';
import 'ant-design-vue/dist/reset.css';
import * as Icons from '@ant-design/icons-vue';
import axios from 'axios';
const app = createApp(App);
app.use(store).use(router).use(Antd).mount('#app')

//全局使用图标
const icons = Icons;
for (const i in icons) {
    app.component(i, icons[i])
}

// main.js
axios.interceptors.request.use(function (config) {
    console.log('请求参数:', config);
    const userType = store.state.userType;  // 从 Vuex 获取当前的用户类型
    let token = '';

    // 根据用户类型选择 token
    if (userType === 'business') {
        token = store.state.business.token;  // 获取商家的 token
    } else if (userType === 'user') {
        token = store.state.user.token;  // 获取普通用户的 token
    }

    if (token) {
        config.headers.token = token;  // 将 token 添加到请求头
    }

    return config;
}, error => {
    return Promise.reject(error);
});

axios.interceptors.response.use(function (response) {
    console.log('返回结果：', response);
    return response;
}, error => {
    console.log('返回错误', error);
    const _response = error.response;
    const status = _response.status;
    if (status === 401) {
        //判断状态码是401 跳转到登录页
        console.log("未登录或登录超时，跳转到登录页");
        store.commit("setBusiness", {});
        notification.error({ description: "未登录或登录超时" });
        router.push('/login');
    }
});

axios.defaults.baseURL = process.env.VUE_APP_SERVER; //每次请求添加默认前缀
console.log('环境：', process.env.NODE_ENV);
console.log('服务端：', process.env.VUE_APP_SERVER);