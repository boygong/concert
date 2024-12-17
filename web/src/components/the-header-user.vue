<template>
  <a-layout-header class="header">
      <div class="logo" />
      <div style="float: right; color: white;">
          您好:{{ user.name }}
          <router-link to="/login" style="color: white;" @click="remove">
              退出登录
          </router-link>
      </div>
      <a-menu v-model:selectedKeys="selectedKeys" theme="dark" mode="horizontal" :style="{ lineHeight: '64px' }">
          <a-menu-item key="/userPage">
              <router-link to="/userPage">
                  <coffee-outlined/> &nbsp; 首页
              </router-link>
          </a-menu-item>
          <a-menu-item key="/userOrder">
              <router-link to="/userOrder">
                  <coffee-outlined/> &nbsp; 订单
              </router-link>
          </a-menu-item>
      </a-menu>
  </a-layout-header>
</template>


<script>
import store from '@/store';
import { defineComponent, ref} from 'vue';
import router from '@/router'

export default defineComponent({
  name: "the-header-view",
  setup() {
      let user = store.state.user;
      const selectedKeys = ref([]);
      // 监听当前路由，当当前的路由跳转时，更新the-sider的启动标签
      const remove =()=>{
          router.push("/userlogin")
          store.commit("setUser",{});
      }
      return {
          user,
          selectedKeys,
          remove
      };
  },
});
</script>

<style scoped>
.ant-row-rtl #components-layout-demo-top-side-2 .logo {
  float: right;
  margin: 16px 0 16px 24px;
}
</style>