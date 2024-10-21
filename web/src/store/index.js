import { createStore } from 'vuex'

// const MEMBER = "MEMBER";

// export default createStore({
//   state: {
//     member: window.SessionStorage.get(MEMBER) || {}
//   },
//   getters: {
//   },
//   mutations: {
//     setMember(state, _member) {
//       state.member = _member;
//       window.SessionStorage.set(MEMBER, _member);
//     },
//     removeMember() {
//       window.SessionStorage.remove(MEMBER);
//     }
//   },
//   actions: {
//   },
//   modules: {
//   }
// })

const BUSINESS = "BUSINESS";

export default createStore({
  state: {
    business: window.SessionStorage.get(BUSINESS) || {}
  },
  getters: {
  },
  mutations: {
    setBusiness(state, _business) {
      state.business = _business;
      window.SessionStorage.set(BUSINESS, _business);
    },
    removeBusiness(){
      window.SessionStorage.remove(BUSINESS);
    }
  },
  actions: {
  },
  modules: {
  }
})

