<template>
  <a-row class="login">
    <a-col :span="8" :offset="8" class="login-main">
      <h1 style="text-align: center"><rocket-two-tone />&nbsp;演唱会售票系统</h1>
      <a-form :model="loginForm" name="basic" autocomplete="off">
        <a-form-item label="" name="username" :rules="[{ required: true, message: '请输入用户名!' }]">
          <a-input v-model:value="loginForm.username" placeholder="用户名" />
        </a-form-item>

        <a-form-item label="" name="password" :rules="[{ required: true, message: '密码' }]">
          <a-input v-model:value="loginForm.password" placeholder="密码" type="password"/>
        </a-form-item>

        <a-form-item>
          <a-button type="primary" block @click="login">登录</a-button>
        </a-form-item>

      </a-form>
    </a-col>
  </a-row>
</template>

<script>
import { defineComponent, reactive } from 'vue';
import axios from 'axios';
import { notification } from 'ant-design-vue';
import { useRouter } from 'vue-router'
import store from '@/store';

export default defineComponent({
  name: "login-view",
  setup() {
    const router = useRouter();

    const loginForm = reactive({
      username: 'admin',
      password: 'admin',
    });

    const login = () => {
      axios.post("/business/business/login", loginForm).then((response) => {
        let data = response.data;
        console.log(data)
        if (data.code == 1) {
          notification.success({ description: '登录成功！' });
          // 登录成功，跳到控台主页
          router.push("/welcome");
          console.log(data.data)
          store.commit("setBusiness",data.data);
        } else {
          notification.error({ description: data.msg });
        }
      })
    };

    return {
      loginForm,
      login
    };
  },
});
</script>

<style>
.login-main h1 {
  font-size: 25px;
  font-weight: bold;
}

.login-main {
  margin-top: 100px;
  padding: 30px 30px 20px;
  border: 2px solid grey;
  border-radius: 10px;
  background-color: #fcfcfc;
}
</style>
