<template>
    <a-page-header title="演唱会与音乐会展示页面" sub-title="最新活动信息">
      <template #breadcrumb>
        <a-breadcrumb>
          <a-breadcrumb-item>首页</a-breadcrumb-item>
          <a-breadcrumb-item>活动展示</a-breadcrumb-item>
        </a-breadcrumb>
      </template>
    </a-page-header>
  
    <div class="concert-list">
      <h2>演唱会</h2>
      <a-row :gutter="[20, 20]">
        <a-col :span="6" v-for="concert in concerts" :key="concert.concertId">
          <a-card class="concert-card" :hoverable="true" :title="concert.name">
            <template #cover>
              <img :src="concert.photo" alt="cover" class="concert-image" />
            </template>
            <p>时间：{{ concert.beginTime }}</p>
            <p>最低价格：¥{{ concert.lowPrice }}</p>
            <p class="concert-description">描述：{{ concert.describe }}</p>
            <p>余票：{{ concert.currentNum +"/" + concert.seatNum}}张</p>
            <a-button type="primary" block>购买门票</a-button>
          </a-card>
        </a-col>
      </a-row>
      <div class="pagination">
        <a-pagination
          :current="pagination.current"
          :page-size="pagination.pageSize"
          :total="pagination.total"
          @change="fetchConcerts"
        />
      </div>
    </div>
  
    <div class="music-list">
      <h2>音乐会</h2>
      <a-row :gutter="[20, 20]">
        <a-col :span="6" v-for="music in musics" :key="music.concertId">
          <a-card class="concert-card" :hoverable="true" :title="music.name">
            <template #cover>
              <img :src="music.photo" alt="cover" class="concert-image" />
            </template>
            <p>时间：{{ music.beginTime }}</p>
            <p>最低价格：¥{{ music.lowPrice }}</p>
            <p class="concert-description">描述：{{ music.describe }}</p>
            <p>余票：{{ music.currentNum +"/" + music.seatNum}}张</p>
            <a-button type="primary" block>购买门票</a-button>
          </a-card>
        </a-col>
      </a-row>
      <div class="pagination">
        <a-pagination
          :current="musicPagination.current"
          :page-size="musicPagination.pageSize"
          :total="musicPagination.total"
          @change="fetchMusics"
        />
      </div>
    </div>
  </template>
  
  <script>
  import { defineComponent, reactive, onMounted } from 'vue';
  import axios from 'axios';
  
  export default defineComponent({
    name: 'ConcertDisplayPage',
  
    setup() {
      const concerts = reactive([]);
      const musics = reactive([]);
      const pagination = reactive({
        current: 1,
        pageSize: 4,
        total: 0,
      });
      const musicPagination = reactive({
        current: 1,
        pageSize: 4,
        total: 0,
      });
  
      const fetchConcerts = async (page = 1) => {
        try {
          const response = await axios.post('concert/concert/pageQueryUser', {
            page,
            size: pagination.pageSize,
            concertId: '',
            theaterId: '',
            name: '',
            highFee: null,
            lowFee: null,
            status: null,
            location: '',
            describe: '',
            type: 0,
            player: '',
            beginTime: null,
            endTime: null,
            isSelected: null,
          });
  
          const { data } = response.data;
          concerts.splice(0, concerts.length, ...data.records);
          pagination.total = data.total;
          pagination.current = page;
        } catch (error) {
          console.error('Failed to fetch concerts:', error);
        }
      };
  
      const fetchMusics = async (page = 1) => {
        try {
          const response = await axios.post('concert/concert/pageQueryUser', {
            page,
            size: musicPagination.pageSize,
            concertId: '',
            theaterId: '',
            name: '',
            highFee: null,
            lowFee: null,
            status: null,
            location: '',
            describe: '',
            type: 1,
            player: '',
            beginTime: null,
            endTime: null,
            isSelected: null,
          });
  
          const { data } = response.data;
          musics.splice(0, musics.length, ...data.records);
          musicPagination.total = data.total;
          musicPagination.current = page;
        } catch (error) {
          console.error('Failed to fetch musics:', error);
        }
      };
  
      onMounted(() => {
        fetchConcerts();
        fetchMusics();
      });
  
      return { concerts, musics, pagination, musicPagination, fetchConcerts, fetchMusics };
    },
  });
  </script>
  
  <style scoped>
  .concert-list, .music-list {
    padding: 20px;
  }
  
  .pagination {
    margin-top: 40px;
    text-align: center;
  }
  
  .concert-card {
    height: 500px;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    overflow: hidden;
  }
  
  .concert-image {
    height: 150px;
    object-fit: cover;
    width: 100%;
  }
  
  .concert-description {
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
  </style>
  