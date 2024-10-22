<template>
    <p>
        <a-space>
            <!-- <a-button type="primary" @click="handleQuery()">刷新</a-button> -->
            <a-button type="primary" @click="onAdd">新增</a-button>
        </a-space>
    </p>

    <div>
        <a-table :columns="columns" :dataSource="businessData">
            <!-- 使用 v-slot:action 来定义操作列的插槽 -->
            <template v-slot:customRender="{ record, index }">
                <span>
                    <a-button @click="handleEdit(record, index)" type="primary" size="small">编辑</a-button>
                    <a-button @click="handleDelete(record, index)" type="primary" size="small" danger>删除</a-button>
                </span>
            </template>
        </a-table>
    </div>

    <!-- <a-modal v-model:visible="visible" title="创建商家" @ok="handleOk" ok-text="确认" cancel-text="取消">
        <a-form :model="passenger" :label-col="{ span: 4 }" :wrapper-col="{ span: 20 }">
            <a-form-item label="姓名">
                <a-input v-model:value="passenger.name" />
            </a-form-item>
            <a-form-item label="身份证">
                <a-input v-model:value="passenger.idCard" />
            </a-form-item>
            <a-form-item label="旅客类型">
                <a-select v-model:value="passenger.type">
                    <a-select-option :value="1">成人</a-select-option>
                    <a-select-option :value="2">儿童</a-select-option>
                    <a-select-option :value="3">学生</a-select-option>
                </a-select>
            </a-form-item>
        </a-form>
    </a-modal> -->

    <a-modal v-model:visible="visible" title="创建商家" @ok="handleOk" ok-text="确认" cancel-text="取消">
        <a-form :model="passenger" :label-col="{ span: 4 }" :wrapper-col="{ span: 20 }">
            <a-form-item label="姓名">
                <a-input v-model:value="business.name" />
            </a-form-item>
            <a-form-item label="用户名">
                <a-input v-model:value="business.username" />
            </a-form-item>
            <a-form-item label="密码">
                <a-input v-model:value="business.password" />
            </a-form-item>
            <a-form-item label="联系方式">
                <a-input v-model:value="business.phone" />
            </a-form-item>
            <a-form-item label="性别">
                <a-select v-model:value="business.sex">
                    <a-select-option :value="1">男</a-select-option>
                    <a-select-option :value="0">女</a-select-option>
                </a-select>
            </a-form-item>
            <a-form-item label="身份">
                <a-select v-model:value="business.identity">
                    <a-select-option :value="0">管理员</a-select-option>
                    <a-select-option :value="1">商家</a-select-option>
                </a-select>
            </a-form-item>
            <a-form-item label="身份证">
                <a-input v-model:value="business.idNumber" />
            </a-form-item>
        </a-form>
    </a-modal>
</template>
<script>
import { defineComponent, ref, onMounted } from 'vue';
import { notification } from "ant-design-vue";
import { Table, Button } from 'ant-design-vue';
import axios from "axios";
export default defineComponent({
    name: "passenger-view",
    components: {
        'a-table': Table,
        'a-button': Button,
    },

    setup() {
        const PASSENGER_TYPE_ARRAY = window.PASSENGER_TYPE_ARRAY;
        const businessData = ref([]);
        const visible = ref(false);
        const columns = [
            {
                title: '商家ID',
                dataIndex: 'businessId',
                key: 'businessId',
            },
            {
                title: '名称',
                dataIndex: 'name',
                key: 'name',
            },
            {
                title: '用户名',
                dataIndex: 'username',
                key: 'username',
            },
            // 注意：密码通常不会直接展示在表格中，这里仅作为示例  
            {
                title: '密码',
                dataIndex: 'password',
                key: 'password',
                // 可以添加自定义渲染来隐藏或加密显示  
                //customRender: (text) => <span style="color: red;">******</span>, // 示例：隐藏密码  
            },
            {
                title: '电话',
                dataIndex: 'phone',
                key: 'phone',
            },
            {
                title: '性别',
                dataIndex: 'sex',
                key: 'sex',
                render: (text) => (text === 1 ? '男' : '女'),
            },
            {
                title: '身份',
                dataIndex: 'identity',
                key: 'identity',
                render: (text) => (text === 0 ? '管理员' : '商家'),
            },
            {
                title: '状态',
                dataIndex: 'status',
                key: 'status',
                render: (text) => (text === 1 ? '启用' : '禁用'),
            },
            {
                title: '创建时间',
                dataIndex: 'createTime',
                key: 'createTime',
                sorter: (a, b) => new Date(a.createTime) - new Date(b.createTime),
                render: (text) => text.toLocaleString(), // 格式化日期显示  
            },
            {
                title: '更新时间',
                dataIndex: 'updateTime',
                key: 'updateTime',
                render: (text) => text.toLocaleString(), // 格式化日期显示  
            },
            {
                title: '操作',
                dataIndex: '', // 通常不指定 dataIndex，因为这是一个自定义列  
                key: 'action-column-key', // 可以是任何唯一值  
                slots: { customRender: 'customRender' }, // 指定插槽名称（与 v-slot 指令中的名称相匹配）  
            },
        ];
        let business = ref({
            name: undefined,
            username: undefined,
            password: "123456",
            phone: undefined,
            sex: "1",
            identity: "1",
            idNumber: undefined,
        });
        let businessDTO = ref({
            businessId: undefined,
            name: undefined,
            username: undefined,
            password: undefined,
            phone: undefined,
            sex: undefined,
            identity: undefined,
            status: undefined,
            idNumber: undefined,
            type: 1,
        })
        // const passengers = ref([]);
        const onAdd = () => {
            business.value = {};
            visible.value = true;
        };
        const handleOk = () => {
            axios.post("/business/business/save", business.value).then((response) => {
                let data = response.data;
                if (data.code === 1) {
                    notification.success({ description: "保存成功！" });
                    visible.value = false;
                    console.log("刷新数据");
                    fetchData();
                    // handleQuery({
                    //     page: pagination.value.current,
                    //     size: pagination.value.pageSize
                    // });
                } else {
                    notification.error({ description: data.message });
                }
            });
        };
        // 模拟从后端获取数据  
        const fetchData = async () => {
            try {
                const response = await axios.post('/business/business/getList', businessDTO.value); // 替换为您的后端API  
                businessData.value = response.data.data;
                console.log("表格数据", businessData.value)
            } catch (error) {
                console.error('Failed to fetch business data:', error);
            }
        };

        // 编辑操作（示例）  
        const handleEdit = (record) => {
            console.log('Edit:', record);
            // 实现编辑逻辑，如跳转到编辑页面或弹出编辑对话框  
        };

        // 删除操作（示例）  
        const handleDelete = (record) => {
            console.log('Delete:', record);
            // 实现删除逻辑，如发送删除请求到后端  
        };
        onMounted(() => {
            fetchData();
        });
        // 组件挂载时获取数据  
        return {
            PASSENGER_TYPE_ARRAY,
            business,
            visible,
            onAdd,
            handleOk,
            businessData,
            columns,
            handleEdit,
            handleDelete,
        };
    }
});
</script>
<style></style>