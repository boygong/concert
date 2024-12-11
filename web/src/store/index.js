import { createStore } from 'vuex'

const BUSINESS = "BUSINESS";

const USER = "USER";

export default createStore({
  state: {
    business: window.SessionStorage.get(BUSINESS) || {},
    user: window.SessionStorage.get(USER) || {}
  },
  getters: {
    currentUserType: (state) => state.userType,  // 获取当前的用户类型
    businessToken: (state) => state.business.token  // 获取商家的 token
    // 可以根据需要添加一些计算属性
  },
  mutations: {
    setBusiness(state, _business) {
      state.business = _business;
      window.SessionStorage.set(BUSINESS, _business);  // 将商家信息存储到 SessionStorage
    },
    removeBusiness(state) {
      state.business = {};
      window.SessionStorage.remove(BUSINESS);  // 移除商家信息
    },
    setUser(state, _user) {
      state.user = _user;
      window.SessionStorage.set(USER, _user);  // 将用户信息存储到 SessionStorage
    },
    removeUser(state) {
      state.user = {};
      window.SessionStorage.remove(USER);  // 移除用户信息
    }
  },
  actions: {
    // 如果需要异步操作，可以在此定义，例如登录请求
  },
  modules: {
    // 如果有其他模块，也可以在此定义
  }
})

