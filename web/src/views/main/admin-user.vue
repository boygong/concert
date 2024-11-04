<template>
    <div style="padding: 20px;">
      <h2>管理员用户界面</h2>
  
      <div style="margin-bottom: 20px;">
        <a-input v-model:value="filters.userId" placeholder="用户ID" style="width: 150px; margin-right: 10px;" />
        <a-input v-model:value="filters.name" placeholder="姓名" style="width: 150px; margin-right: 10px;" />
        <a-input v-model:value="filters.username" placeholder="用户名" style="width: 150px; margin-right: 10px;" />
        <a-input v-model:value="filters.idNumber" placeholder="身份证号" style="width: 150px; margin-right: 10px;" />
        <a-select v-model:value="filters.sex" style="width: 100px; margin-right: 10px;">
          <a-select-option value="">性别</a-select-option>
          <a-select-option value="0">女</a-select-option>
          <a-select-option value="1">男</a-select-option>
        </a-select>
        <a-input v-model="filters.phone" placeholder="手机号码" style="width: 150px; margin-right: 10px;" />
        <a-button type="primary" @click="fetchUsers">搜索</a-button>
      </div>
  
      <div v-if="loading" style="text-align: center;">加载中...</div>
      <div v-else>
        <a-table v-if="users.length > 0" :dataSource="users" :loading="loading" :pagination="false" rowKey="userId">
          <a-table-column title="序号" v-slot="{ index }">
            <span>{{ (page - 1) * size + index + 1 }}</span>
          </a-table-column>
          <a-table-column title="用户ID" dataIndex="userId" />
          <a-table-column title="用户名" dataIndex="username" />
          <a-table-column title="姓名" dataIndex="name" />
          <a-table-column title="性别" v-slot="{ record }">
            <span>{{ formatSex(record?.sex) }}</span>
          </a-table-column>
          <a-table-column title="邮箱" dataIndex="email" />
          <a-table-column title="手机号" dataIndex="phone" />
          <a-table-column title="身份证号" dataIndex="idNumber" />
          <a-table-column title="创建时间" v-slot="{ record }">
            <span>{{ formatDate(record?.createTime) }}</span>
          </a-table-column>
        </a-table>
  
        <div style="display: flex; justify-content: space-between; align-items: center; margin-top: 20px;">
          <div>
            <a-button @click="prevPage" :disabled="page === 1">上一页</a-button>
            <span>第 {{ page }} 页 / 共 {{ totalPages }} 页</span>
            <a-button @click="nextPage" :disabled="page >= totalPages">下一页</a-button>
            <a-input-number v-model="inputPage" @keyup.enter="goToPage(inputPage)" min="1" :max="totalPages" placeholder="跳转页码" style="width: 100px; margin-left: 10px;" />
            <a-button @click="goToPage(inputPage)">跳转</a-button>
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  import { defineComponent, ref, computed, reactive } from 'vue';
  import axios from 'axios';
  
  export default defineComponent({
    name: 'admin-user',
    setup() {
      const users = ref([]);
      const loading = ref(false);
      const page = ref(1);
      const size = ref(10);
      const total = ref(0);
      const inputPage = ref(page.value);
  
      // 将 filters 定义为响应式对象
      const filters = reactive({
        userId: '',
        name: '',
        username: '',
        idNumber: '',
        sex: '',
        phone: ''
      });
  
      const fetchUsers = async () => {
        loading.value = true;
        console.log("输入框参数:", filters);
        try {
          const response = await axios.post('/users/users/pageQuery', {
            userId: filters.userId,
            name: filters.name,
            username: filters.username,
            idNumber: filters.idNumber,
            sex: filters.sex,
            createTime: "",
            phone: filters.phone,
            page: page.value,
            size: size.value
          });
  
          if (response.data.code === 1) {
            users.value = response.data.data.records;
            total.value = response.data.data.total;
          } else {
            console.error(response.data.msg);
          }
        } catch (error) {
          console.error('请求失败:', error);
        } finally {
          loading.value = false;
        }
      };
  
      const nextPage = () => {
        if (page.value < totalPages.value) {
          page.value++;
          fetchUsers();
        }
      };
  
      const prevPage = () => {
        if (page.value > 1) {
          page.value--;
          fetchUsers();
        }
      };
  
      const goToPage = (targetPage) => {
        const pageNumber = parseInt(targetPage);
        if (pageNumber > 0 && pageNumber <= totalPages.value) {
          page.value = pageNumber;
          inputPage.value = pageNumber; // 更新输入框的值
          fetchUsers();
        }
      };
  
      const totalPages = computed(() => Math.ceil(total.value / size.value));
  
      const formatSex = (sex) => (sex === 0 ? '女' : '男');
  
      const formatDate = (dateString) => {
        const date = new Date(dateString);
        return date.toLocaleString(); // 格式化为本地时间字符串
      };
  
      // 初始加载用户数据
      fetchUsers();
  
      return {
        users,
        loading,
        page,
        size,
        total,
        totalPages,
        nextPage,
        prevPage,
        goToPage,
        formatSex,
        formatDate,
        inputPage,
        filters,
        fetchUsers
      };
    }
  });
  </script>
  
  <style scoped>

</style>
  