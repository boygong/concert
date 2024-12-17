import { createRouter, createWebHistory } from 'vue-router'
import login from '@/views/login.vue'
import userlogin from '@/views/user/userLogin.vue'
import store from '@/store'
import { notification } from 'ant-design-vue'

const routes = [
  
  {
    path:'/login',
    name:'login',
    meta: { userType: 'guest' },  // 设置 meta 标识当前是登录页面
    component:login
  },
  {
    path:'/userlogin',
    name:'userlogin',
    meta: { userType: 'guest' },  // 设置 meta 标识当前是登录页面
    component:userlogin
  },
  // {
  //   path:'/userPage',
  //   name:'userPage',
  //   // meta:{userType:'user',loginRequire:true},
  //   component:()=>import('@/views/user/userPage.vue')
  // },
  {
    path: '/user',
    component: () => import('@/views/userMain.vue'),
    children: [
      {
        path: '',  // 空路径，这样当访问 `/user` 时会跳转到 `userPage`
        redirect: 'userPage'  // 自动跳转到 userPage
      },
      {
        path: '/userPage',  // 相对路径
        component: () => import('@/views/user/userPage.vue'),
        meta: { userType: 'guest' }
      },{
        path:'/userOrder',
        component: () => import('@/views/user/userOrder.vue'),
        meta: { userType: 'user' ,loginRequire: true}
      },{
        path:'/createOrder/:concertId',
        name:'createOrder',
        component: () => import('@/views/user/createOrder.vue'),
        meta: { userType: 'user' ,loginRequire: true}
      },{
        path:'/confirmOrder/:orderData',
        name:'confirmOrder',
        component: () => import('@/views/user/confirmOrder.vue'),
        meta: { userType: 'user' ,loginRequire: true}
      }
    ]
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
      meta:{userType:'business'}
    },{
      path:'passenger',
      component:()=>import('@/views/main/passenger.vue'),
      meta:{userType:'business'}
    },{
      path:'admin-user',
      component:()=>import('@/views/main/admin-user.vue'),
      meta:{userType:'business'}
    },{
      path:'concert',
      component:()=>import('@/views/main/concert.vue'),
      meta:{userType:'business'}
    },{
      path:'order',
      component:()=>import('@/views/main/order.vue'),
      meta:{userType:'business'}
    }]
  },{
    path:'',
    redirect: '/welcome'
  },
  // {
  //   path:'/user',
  //   children:[{
  //     path:'login',
  //     component:()=>import('@/vi'),
  //     meta:{userType:'business'}
  //   }]
  // }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

// 路由登录拦截
router.beforeEach((to, from, next) => {
  // 获取当前路由的meta信息
  const userType = to.meta?.userType;  // 获取meta中的userType
  if (userType) {
    store.commit('setUserType', userType);  // 存储当前的用户类型
  }

  // 校验是否需要登录
  if (to.matched.some(item => item.meta.loginRequire)) {
    const _business = store.state.business;
    const _user = store.state.user;
    console.log("当前用户身份:",userType)
    if(userType === 'business'){
      if (!_business.token) {
        console.log("未登录或登录超时");
        notification.error({ description: "未登录或登录超时" });
        next('/login');
      } else {
        next();
      }
    }else if(userType === 'user'){
      if (!_user.token) {
        console.log("未登录或登录超时");
        notification.error({ description: "未登录或登录超时" });
        next('/userLogin');
      } else {
        next();
      }
    }
   
  } else {
    next();
  }
});
export default router
