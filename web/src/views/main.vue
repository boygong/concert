<template>
  <a-layout>
    <the-header-view></the-header-view>
    <a-layout>
      <the-sider-view></the-sider-view>
        <a-layout-content :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }">
          <router-view></router-view>
        </a-layout-content>
      </a-layout> 
    </a-layout>
</template>
<script>
import { defineComponent } from 'vue';
import TheHeaderView from "@/components/the-header.vue";
import TheSiderView from "@/components/the-sider.vue";
// import axios from 'axios';
// import { notification } from 'ant-design-vue';
export default defineComponent({
  components: {
    TheHeaderView,
    TheSiderView
  },
  setup() {
    // const count = ref(0);
    // axios.get("/member/member/count").then((response) => {
    //     var data = response.data;

    //     if (data.success) {
    //       count.value = data.content;
    //     } else {
    //       notification.error({ description: data.message });
    //     }
    // }).catch(error=>{
    //   console.log(error);
    // });
    // return {
    //   count
    // }
  }

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