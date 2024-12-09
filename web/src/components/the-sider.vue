<template>
    <a-layout-sider width="200" style="background: #fff">
        <a-menu v-model:selectedKeys="selectedKeys2" v-model:openKeys="openKeys" mode="inline"
            :style="{ height: '100%', borderRight: 0 }">
            <a-menu-item key="/welcome">
                <router-link to="/welcome">
                    <coffee-outlined /> &nbsp; 欢迎
                </router-link>
            </a-menu-item>
            <a-menu-item key="/passenger">
                <router-link to="/passenger">
                    <user-outlined /> &nbsp; 商家管理
                </router-link>
            </a-menu-item>
            <a-menu-item key="/admin-user">
                <router-link to="/admin-user">
                    <user-outlined/> &nbsp; 用户管理
                </router-link>
            </a-menu-item>
            <a-menu-item key="/concert">
                <router-link to="/concert">
                    <user-outlined/> &nbsp; 演唱会管理
                </router-link>
            </a-menu-item>
            <a-menu-item key="/order">
                <router-link to="/order">
                    <user-outlined/> &nbsp; 订单管理
                </router-link>
            </a-menu-item>
        </a-menu>
    </a-layout-sider>
</template>
<script>
import { defineComponent, ref, watch } from 'vue';
import router from "@/router";


export default defineComponent({
    name: "the-sider-view",
    setup() {
        const selectedKeys = ref([]);
        // 监听当前路由，当当前的路由跳转时，更新the-header的启动标签

        watch(() => router.currentRoute.value.path, (newValue) => {
            console.log('watch', newValue);
            selectedKeys.value = [];
            selectedKeys.value.push(newValue);
        }, { immediate: true });
        return {
            selectedKeys
        };
    },
});
</script>
<style scoped>
#components-layout-demo-top-side-2 .logo {
    float: left;
    width: 120px;
    height: 31px;
    margin: 16px 24px 16px 0;
    background: rgba(255, 255, 255, 0.3);
}

.ant-row-rtl #components-layout-demo-top-side-2 .logo {
    float: right;
    margin: 16px 0 16px 24px;
}

.site-layout-background {
    background: #fff;
}
</style>