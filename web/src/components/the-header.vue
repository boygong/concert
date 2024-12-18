<template>
    <a-layout-header class="header">
        <div class="logo" />
        <div style="float: right; color: white;">
            您好:{{ business.name }}
            <router-link to="/login" style="color: white;" @click="remove">
                退出登录
            </router-link>
        </div>
        <a-menu v-model:selectedKeys="selectedKeys" theme="dark" mode="horizontal" :style="{ lineHeight: '64px' }">
            <a-menu-item key="/welcome">
                <router-link to="/welcome">
                    <coffee-outlined /> &nbsp; 欢迎
                </router-link>
            </a-menu-item>
            <a-menu-item :disabled="business.status === 1" key="/passenger">
                <router-link :disabled="business.status === 1" to="/passenger">
                    <user-outlined /> &nbsp; 商家管理
                </router-link>
            </a-menu-item>
            <a-menu-item :disabled="business.status === 1" key="/admin-user">
                <router-link :disabled="business.status === 1" to="/admin-user">
                    <user-outlined /> &nbsp; 用户管理
                </router-link>
            </a-menu-item>
            <a-menu-item key="/concert">
                <router-link to="/concert">
                    <user-outlined /> &nbsp; 演唱会管理
                </router-link>
            </a-menu-item>
            <a-menu-item key="/order">
                <router-link to="/order">
                    <user-outlined /> &nbsp; 订单管理
                </router-link>
            </a-menu-item>
        </a-menu>
    </a-layout-header>
</template>


<script>
import store from '@/store';
import { defineComponent, ref, watch } from 'vue';
import router from '@/router'

export default defineComponent({
    name: "the-header-view",
    setup() {
        let business = store.state.business;
        const selectedKeys = ref([]);
        // 监听当前路由，当当前的路由跳转时，更新the-sider的启动标签
        watch(() => router.currentRoute.value.path, (newValue) => {
            console.log('watch', newValue);
            selectedKeys.value = [];
            selectedKeys.value.push(newValue);
        }, { immediate: true });
        const remove = () => {
            router.push("/login")
            store.commit("setBusiness", {});
        }
        return {
            business,
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