<template>
    <div style="padding: 20px;">
        <h2>演唱会管理界面</h2>

        <!-- 搜索表单 -->
        <a-form layout="inline" style="margin-bottom: 20px;">
            <a-form-item label="演唱会ID">
                <a-input v-model:value="filters.concertId" placeholder="请输入演唱会ID" />
            </a-form-item>
            <a-form-item label="演唱会名称">
                <a-input v-model:value="filters.name" placeholder="请输入演唱会名称" />
            </a-form-item>
            <a-form-item label="演出人员">
                <a-input v-model:value="filters.player" placeholder="请输入演出人员" />
            </a-form-item>
            <a-form-item label="所在地">
                <a-input v-model:value="filters.location" placeholder="请输入所在地" />
            </a-form-item>
            <a-form-item label="票价区间">
                <a-input-number v-model:value="filters.lowFee" placeholder="最低票价" style="width: 140px" :min="0"
                    :max="filters.highFee" addon-before="￥" />
                <span style="margin: 0 10px;">至</span>
                <a-input-number v-model:value="filters.highFee" placeholder="最高票价" style="width: 140px"
                    :min="filters.lowFee" 
                    :max="undefined"
                    addon-before="￥"
                    />
            </a-form-item>

            <a-form-item label="状态">
                <a-select v-model:value="filters.status" placeholder="请选择状态" style="width: 150px;">
                    <a-select-option value="">全部</a-select-option>
                    <a-select-option value="-1">待审核</a-select-option>
                    <a-select-option value="0">待售</a-select-option>
                    <a-select-option value="1">售卖中</a-select-option>
                    <a-select-option value="2">停售</a-select-option>
                    <a-select-option value="3">售罄</a-select-option>
                </a-select>
            </a-form-item>
            <a-form-item label="是否可选座">
                <a-select v-model:value="filters.isSelected" placeholder="请选择" style="width: 150px;">
                    <a-select-option value="">全部</a-select-option>
                    <a-select-option value="0">可选座位</a-select-option>
                    <a-select-option value="1">不可选</a-select-option>
                    <a-select-option value="2">可选等级座位</a-select-option>
                </a-select>
            </a-form-item>
            <a-form-item label="演出类型">
                <a-select v-model:value="filters.type" placeholder="请选择演出类型" style="width: 150px;">
                    <a-select-option value="">全部</a-select-option>
                    <a-select-option value="0">演唱会</a-select-option>
                    <a-select-option value="1">音乐会</a-select-option>
                </a-select>
            </a-form-item>
            <a-form-item label="演出时间">
                <a-range-picker v-model:value="filters.timeRange" format="YYYY-MM-DD hh:mm:ss" placeholder="请选择时间范围" />
            </a-form-item>
            <a-form-item>
                <a-button type="primary" @click="fetchConcerts">搜索</a-button>
                <a-button style="margin-left: 10px;" @click="resetFilters">重置</a-button>
            </a-form-item>
        </a-form>

        <!-- 演唱会数据表格 -->
        <a-table :dataSource="concerts" :loading="loading" :pagination="pagination" :rowKey="'concertId'"
            @change="onTableChange">
            <!-- 表格列定义 -->
            <a-table-column title="演唱会ID" dataIndex="concertId" />
            <a-table-column title="名称" dataIndex="name" />
            <a-table-column title="演出人员" dataIndex="player" />
            <a-table-column title="所在地" dataIndex="location" />
            <a-table-column title="图片" v-slot="{ record }">
                <img :src="record.photo" alt="演唱会图片" style="width: 60px; height: 60px; object-fit: cover;" />
            </a-table-column>
            <a-table-column title="状态" v-slot="{ record }">
                <span>{{ formatStatus(record.status) }}</span>
            </a-table-column>
            <a-table-column title="最低票价" dataIndex="lowPrice" />
            <a-table-column title="演出开始时间" v-slot="{ record }">
                <span>{{ formatDate(record.beginTime) }}</span>
            </a-table-column>
            <a-table-column title="描述" dataIndex="describe" />
            <a-table-column title="创建人" dataIndex="createUser" />
            <a-table-column title="操作">
                <template #default="{}">
                    <a-button type="link">查看</a-button>
                    <a-button type="link">编辑</a-button>
                </template>
            </a-table-column>
        </a-table>
    </div>
</template>

<script>
import { defineComponent, reactive, ref } from 'vue';
import axios from 'axios';

export default defineComponent({
    name: 'ConcertManagement',
    setup() {
        const filters = reactive({
            concertId: undefined,
            theaterId: undefined,
            name: undefined,
            player: undefined,
            highFee: undefined,
            lowFee: undefined,
            location: undefined,
            status: undefined,
            describe: undefined,
            isSelected: undefined,
            type: 0,
            timeRange: []
        });
        const concerts = ref([]);
        const loading = ref(false);
        const pagination = reactive({
            current: 1,
            pageSize: 2,
            total: 0
        });

        const fetchConcerts = async () => {
            loading.value = true;
            try {
                const response = await axios.post('/concert/concert/pageQuery', {
                    page: pagination.current,
                    size: pagination.pageSize,
                    concertId: filters.concertId,
                    name: filters.name,
                    player: filters.player,
                    location: filters.location,
                    status: filters.status,
                    isSelected: filters.isSelected,
                    type: filters.type,
                    beginTime: filters.timeRange.length ? filters.timeRange[0].format('YYYY-MM-DDTHH:mm:ss') : '',
                    endTime: filters.timeRange.length ? filters.timeRange[1].format('YYYY-MM-DDTHH:mm:ss') : '',
                    lowFee: filters.lowFee || undefined,  // 传递最低票价
                    highFee: filters.highFee || undefined  // 传递最高票价
                });
                if (response.data.code === 1) {
                    concerts.value = response.data.data.records;
                    // if(pagination.current != 1){
                    //     pagination.current = 1;
                    // }
                    pagination.total = response.data.data.total;
                } else {
                    console.error('获取数据失败', response.data.msg);
                }
            } catch (error) {
                console.error('请求失败', error);
            } finally {
                loading.value = false;
            }
        };

        const resetFilters = () => {
            Object.keys(filters).forEach((key) => {
                if (Array.isArray(filters[key])) {
                    filters[key] = [];
                } else {
                    filters[key] = '';
                }
            });
            fetchConcerts();
        };

        const onTableChange = (paginationData) => {
            pagination.current = paginationData.current;
            pagination.pageSize = paginationData.pageSize;
            fetchConcerts();
        };

        const formatStatus = (status) => {
            const statusMap = {
                '-1': '待审核',
                0: '待售',
                1: '售卖中',
                2: '停售',
                3: '售罄'
            };
            return statusMap[status] || '未知状态';
        };

        const formatDate = (date) => {
            if (!date) return '暂无';
            return new Date(date).toLocaleString();
        };

        // 初始化加载
        fetchConcerts();

        return {
            filters,
            concerts,
            loading,
            pagination,
            fetchConcerts,
            resetFilters,
            onTableChange,
            formatStatus,
            formatDate
        };
    }
});
</script>

<style scoped>
/* Add your custom styles here */
</style>