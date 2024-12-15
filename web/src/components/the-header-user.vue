<template>
    <a-layout-header class="header">
      <div class="logo" />
  
      <!-- 菜单部分 -->
      <a-menu theme="dark" mode="horizontal" :style="{ lineHeight: '64px' }">
        <a-menu-item key="/userPage">
          <router-link to="/userPage">
            <coffee-outlined/> &nbsp; 首页
          </router-link>
        </a-menu-item>
      </a-menu>
  
      <!-- 用户信息部分 -->
      <div class="user-info" style="float: right; color: white;">
        <template v-if="user.name">
          您好: {{ user.name }}
          <router-link to="/login" class="logout-link" @click="remove">
            退出登录
          </router-link>
        </template>
        <template v-else>
          <a-button type="link" class="login-button" @click="navigateToLogin">
            请登录
          </a-button>
        </template>
      </div>
    </a-layout-header>
  </template>
  
  <script>
  import { defineComponent } from 'vue';
  import { useStore } from 'vuex';
  import router from '@/router';
  import { Layout, Menu, Button } from 'ant-design-vue';
  
  export default defineComponent({
    name: "TheHeaderUserView",
    components: {
      ALayoutHeader: Layout.Header,
      AMenu: Menu,
      AMenuItem: Menu.Item,
      AButton: Button,
    },
    setup() {
      const store = useStore();
      const user = store.state.user;
  
      const remove = () => {
        store.commit("setUser", {});
        
      };
  
      const navigateToLogin = () => {
        router.push('/userLogin');
      };
  
      return {
        user,
        remove,
        navigateToLogin,
      };
    }
  });
  </script>
  
  <style scoped>
  .header {
    display: flex; /* 使用 flex 布局 */
    justify-content: space-between; /* 左右分配空间 */
    align-items: center; /* 垂直居中 */
  }
  
  .logo {
    /* 你的 logo 样式 */
  }
  
  .user-info {
    display: flex;
    align-items: center; /* 垂直居中 */
    color: white;
  }
  
  .login-button {
    margin-left: 8px;
  }
  </style>
  