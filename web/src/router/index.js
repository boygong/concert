import { createRouter, createWebHistory } from 'vue-router'
import login from '@/views/login.vue'
import store from '@/store'
import { notification } from 'ant-design-vue'

const routes = [
  
  {
    path:'/login',
    name:'login',
    component:login
  },
  
  {
    path:'/',
    component: () => import('../views/main.vue'),
    meta:{
      loginRequire: true
    },
    children:[{
      path:'welcome',
      component:()=>import('@/views/main/welcome.vue'),
    },{
      path:'passenger',
      component:()=>import('@/views/main/passenger.vue')
    },{
      path:'admin-user',
      component:()=>import('@/views/main/admin-user.vue')
    },{
      path:'concert',
      component:()=>import('@/views/main/concert.vue')
    }]
  },{
    path:'',
    redirect: '/welcome'
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

// 路由登录拦截
router.beforeEach((to, from, next) => {
  // 要不要对meta.loginRequire属性做监控拦截
  if (to.matched.some(function (item) {
    console.log(item, "是否需要登录校验：", item.meta.loginRequire || false);
    return item.meta.loginRequire
  })) {
    const _business = store.state.business;
    console.log("页面登录校验开始：", _business);
    if (!_business.token) {
      console.log("用户未登录或登录超时！");
      notification.error({ description: "未登录或登录超时" });
      next('/login');
    } else {
      next();
    }
  } else {
    next();
  }
});
export default router
