<template>
    <div style="padding: 20px;">
        <h2>演唱会管理界面</h2>

        <!-- 新增演唱会按钮 -->
        <a-button type="primary" @click="openAddConcertModal">新增演唱会</a-button>

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
                    :min="filters.lowFee" :max="undefined" addon-before="￥" />
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
                <template #default="{ record }">
                    <!-- 编辑按钮 -->
                    <a-button type="link" @click="openEditConcert(record.concertId)">编辑</a-button>

                    <!-- 停售按钮 -->
                    <a-button v-if="record.status === 1 || record.status === 3" type="primary" danger
                        @click="stopConcert(record.concertId)" :disabled="record.status === 2">
                        停售
                    </a-button>

                    <!-- 起售按钮 -->
                    <a-button v-if="record.status === 0 || record.status === 2" type="primary"
                        @click="startConcert(record.concertId)" :disabled="record.status !== 2">
                        起售
                    </a-button>

                    <!-- 审核中按钮 -->
                    <a-button v-if="record.status === -1" type="default" disabled>
                        审核中
                    </a-button>
                </template>
            </a-table-column>
        </a-table>

        <!-- 新增演唱会 Modal -->
        <a-modal v-model:visible="isModalVisible" title="新增演唱会" @ok="addConcert" @cancel="closeModal">
            <a-form>
                <a-form-item label="演唱会名称">
                    <a-input v-model:value="newConcert.name" placeholder="请输入演唱会名称" />
                </a-form-item>
                <a-form-item label="演出人员">
                    <a-input v-model:value="newConcert.player" placeholder="请输入演出人员" />
                </a-form-item>
                <a-form-item label="最低票价">
                    <a-input-number v-model:value="newConcert.lowPrice" placeholder="请输入最低票价" style="width: 100%"
                        :min="0" />
                </a-form-item>
                <a-form-item label="描述">
                    <a-input v-model:value="newConcert.describe" placeholder="请输入演唱会描述" />
                </a-form-item>
                <a-form-item label="演唱会类型">
                    <a-select v-model:value="newConcert.type" style="width: 100%" placeholder="请选择演唱会类型">
                        <a-select-option value="0">演唱会</a-select-option>
                        <a-select-option value="1">音乐会</a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item label="可选座位">
                    <a-select v-model:value="newConcert.isSelected" style="width: 100%" placeholder="请选择是否可选座位">
                        <a-select-option value="0">可选座位</a-select-option>
                        <a-select-option value="1">不可选</a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item label="开始时间">
                    <a-date-picker v-model:value="newConcert.beginTime" style="width: 100%" placeholder="请选择演出开始时间"
                        show-time format="YYYY-MM-DD HH:mm:ss" />
                </a-form-item>
                <a-form-item label="上传图片">
                    <a-upload :showUploadList="false" :before-upload="handleFileUpload">
                        <a-button icon="upload">上传图片</a-button>
                    </a-upload>
                    <div v-if="newConcert.photo">
                        <img :src="newConcert.photo" alt="演唱会图片"
                            style="width: 100px; height: 100px; object-fit: cover; margin-top: 10px;" />
                    </div>
                </a-form-item>
            </a-form>
        </a-modal>
    </div>

    <a-drawer v-model:visible="isDrawerVisible" title="编辑演唱会" :width="800" :destroy-on-close="true">
        <EditConcert :concertId="currentConcertId" @closeDrawer="isDrawerVisible = false" />
    </a-drawer>

</template>

<script>
import { defineComponent, reactive, ref } from 'vue';
import axios from 'axios';
import { notification } from 'ant-design-vue';
import EditConcert from '@/components/EditConcert.vue';

export default defineComponent({
    name: 'ConcertManagement',
    components: {
        EditConcert
    },
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
            type: undefined,
            timeRange: []
        });
        const currentConcertId = ref(null);
        const isDrawerVisible = ref(false);
        const concerts = ref([]);
        const loading = ref(false);
        const pagination = reactive({
            current: 1,
            pageSize: 5,
            total: 0
        });
        /***新增演唱会 */
        const isModalVisible = ref(false);
        const newConcert = reactive({
            theaterId: '1',
            name: '',
            photo: '',
            lowPrice: undefined,
            location: null,
            player: '',
            describe: '',
            beginTime: null,
            type: undefined,
            isSelected: undefined
        });

        const uploadUrl = 'users/common/upload';

        // 打开新增演唱会的 Modal
        const openAddConcertModal = () => {
            isModalVisible.value = true;
        };

        // 关闭新增演唱会的 Modal
        const closeModal = () => {
            isModalVisible.value = false;
            resetNewConcert();
        };


        const openEditConcert = (concertId) => {
            currentConcertId.value = concertId; // 设置当前编辑的演唱会 ID
            isDrawerVisible.value = true; // 显示右侧抽屉
        };

        // 重置新增演唱会表单
        const resetNewConcert = () => {
            newConcert.name = undefined;
            newConcert.player = undefined;
            newConcert.lowPrice = 0;
            newConcert.photo = '';
            newConcert.describe = undefined;
            newConcert.beginTime = undefined;
        };


        // 拦截文件上传，使用 Axios 请求
        const handleFileUpload = (file) => {
            const formData = new FormData();
            formData.append('file', file);

            axios.post('/users/common/upload', formData, {
                headers: {
                    'Content-Type': 'multipart/form-data',
                },
            })
                .then(response => {
                    if (response.data.code === 1) { // 假设 code 为 1 表示成功
                        newConcert.photo = response.data.data; // 后端返回的图片地址
                        notification.success('图片上传成功');
                    } else {
                        notification.error('图片上传失败', response.data.msg);
                    }
                })
                .catch(error => {
                    console.error(error);
                    notification.error('图片上传出错');
                });

            // 返回 false 阻止默认上传行为
            return false;
        };
        // 上传前验证文件类型
        const beforeUpload = (file) => {
            const isImage = file.type.startsWith('image/');
            if (!isImage) {
                alert('只能上传图片文件');
            }
            return isImage;
        };

        // 提交新增演唱会
        const addConcert = async () => {
            try {
                const response = await axios.post('/concert/concert/save', newConcert);
                if (response.data.code === 1) {
                    notification.success({ message: '新增成功' });
                    fetchConcerts();
                    closeModal();
                } else {
                    notification.error({ message: '新增失败', description: response.data.msg });
                }
            } catch (error) {
                notification.error({ message: '新增失败', description: error.message });
            }
        };

        // const formatStatus = (status) => ['待审核', '待售', '售卖中', '停售', '售罄'][status] || '未知状态';

        // const formatDate = (date) => date ? new Date(date).toLocaleString() : '暂无';


        /***新增演唱会代码结束 */

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

        // 启动演唱会
        const startConcert = async (concertId) => {
            try {
                const response = await axios.put('/concert/concert/start', null, {
                    params: { concertId }
                });
                if (response.data.code === 1) {
                    notification.success({ message: '演唱会已起售' });
                    fetchConcerts();
                } else {
                    notification.error({ message: response.data.msg });
                }
            } catch (error) {
                notification.error({ message: '起售失败', description: error.message });
            }
        };

        // 停售演唱会
        const stopConcert = async (concertId) => {
            try {
                const response = await axios.put('/concert/concert/stop', null, {
                    params: { concertId }
                });

                if (response.data.code === 1) {
                    notification.success({ message: '演唱会已停售' });
                    fetchConcerts();
                } else {
                    notification.error({ message: response.data.msg });
                }
            } catch (error) {
                notification.error({ message: '停售失败', description: error.message });
            }
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
            formatDate,
            isModalVisible,
            newConcert,
            openAddConcertModal,
            closeModal,
            beforeUpload,
            addConcert,
            uploadUrl,
            handleFileUpload,
            openEditConcert,
            currentConcertId,
            isDrawerVisible,
            startConcert,
            stopConcert
        };
    }
});
</script>

<style scoped>
/* Add your custom styles here */
</style>