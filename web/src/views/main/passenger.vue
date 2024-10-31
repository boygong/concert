<template>
    <p>
        <a-space>
            <!-- <a-button type="primary" @click="handleQuery()">刷新</a-button> -->
            <a-button type="primary" @click="onAdd">新增</a-button>
        </a-space>
    </p>

    <div>
        <a-table :columns="columns" :dataSource="businessData">
            <template v-slot:gender="{ text }">
                {{ text === 1 ? '男' : '女' }}
            </template>
            <template v-slot:identity="{ text }">
                {{ text === 1 ? '商家' : '管理员' }}
            </template>
            <template v-slot:status="{ text }">
                <span :style="{ color: text === 1 ? 'green' : 'red', fontSize: '16px' }">
                    {{ text === 1 ? '● 启用' : '○ 禁用' }}
                </span>
            </template>
            <!-- 使用 v-slot:action 来定义操作列的插槽 -->
            <template v-slot:customRender="{ record }">
                <span>
                    <a-button @click="showModal(record.username)" type="primary" size="small">编辑</a-button>
                    <a-button :type="record.status === 1 ? 'dashed' : 'primary'"
                        @click="handleDelete(record.username, record.status)" size="small">
                        {{ record.status === 1 ? '停用' : '启用' }}
                    </a-button> </span>
            </template>
        </a-table>
    </div>
    <a-modal v-model:visible="visible2" title="商家信息" @ok="handleSave" @cancel="handleCancel">
        <a-form :model="form" :label-col="{ span: 4 }" :wrapper-col="{ span: 20 }">
            <a-form-item label="商家id">
                <a-input v-model:value="form.businessId" />
            </a-form-item>
            <a-form-item label="姓名">
                <a-input v-model:value="form.name" />
            </a-form-item>
            <a-form-item label="用户名">
                <a-input v-model:value="form.username" />
            </a-form-item>
            <a-form-item label="密码">
                <a-input type="password" v-model:value="form.password" />
            </a-form-item>
            <a-form-item label="联系方式">
                <a-input v-model:value="form.phone" />
            </a-form-item>
            <a-form-item label="性别">
                <a-select v-model:value="form.sex">
                    <a-select-option value="1">男</a-select-option>
                    <a-select-option value="0">女</a-select-option>
                </a-select>
            </a-form-item>
            <a-form-item label="身份">
                <a-select v-model:value="form.identity">
                    <a-select-option value="0">管理员</a-select-option>
                    <a-select-option value="1">商家</a-select-option>
                </a-select>
            </a-form-item>
            <a-form-item label="身份证">
                <a-input v-model:value="form.idNumber" />
            </a-form-item>
            <a-form-item label="创建时间">
                <a-input v-model:value="form.createTime" />
            </a-form-item>
        </a-form>
    </a-modal>

    <a-modal v-model:visible="visible" title="创建商家" @ok="handleOk" ok-text="确认" cancel-text="取消">
        <a-form :model="business" :label-col="{ span: 4 }" :wrapper-col="{ span: 20 }">
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
        const visible2 = ref(false);

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
                slots: { customRender: 'gender' },
            },
            {
                title: '身份',
                dataIndex: 'identity',
                key: 'identity',
                slots: { customRender: 'identity' },
            },
            {
                title: '状态',
                dataIndex: 'status',
                key: 'status',
                slots: { customRender: 'status' },
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
                title: '创建操作员',
                dataIndex: 'createUser',
                key: 'createUser',
            },
            {
                title: '更新操作员',
                dataIndex: 'updateUser',
                key: 'updateUser',
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
        let form = ref({
            businessId: undefined,
            name: undefined,
            username: undefined,
            password: undefined,
            phone: undefined,
            sex: undefined,
            identity: undefined,
            idNumber: undefined,
            createTime: undefined
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
            createUser: undefined,
            updateUser: undefined,
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
        //获取商家列表信息
        const fetchData = async () => {
            try {
                const response = await axios.post('/business/business/getList', businessDTO.value);
                businessData.value = response.data.data;
                console.log("表格数据", businessData.value)
            } catch (error) {
                console.error('Failed to fetch business data:', error);
            }
        };

        const showModal = (username) => {
            console.log("回显传参:", username);
            axios.get("/business/business/getOne", {
                params: {
                    username: username
                }
            }).then((response) => {
                let data = response.data;
                if (data.code === 1) {
                    form.value = response.data.data;
                    console.log("表单参数:", form.value)
                    visible2.value = true;
                } else {
                    notification.error({ description: "查询商家信息失败！" });
                }
            }).catch((error) => {
                console.error("请求失败:", error);
                notification.error({ description: "请求失败，请稍后重试！" });
            });
        };

        const handleSave = async () => {
            try {
                axios.put('/business/business/update', form.value).then((response) => {
                    let data = response.data
                    if (data.code == 1) {
                        visible2.value = false;
                        notification.success({ description: "更新商家信息成功！" });
                        fetchData();
                    } else {
                        notification.error({ description: "更新商家信息失败！" });
                    }
                })
            } catch (error) {
                console.error('保存商家信息时发生错误:', error);
            }
        };

        const handleCancel = () => {
            visible.value = false;
            form.value = { id: null, name: '', username: '', password: '', phone: '', sex: null, identity: null, idNumber: '' };
        };

        // 删除操作（示例）  
        const handleDelete = (username, status) => {
            form.value.username = username;
            if (status === 1) {
                form.value.status = 0;
            } else {
                form.value.status = 1;
            }
            console.log("停用请求参数:", form.value);
            try {
                axios.put('/business/business/update', form.value).then((response) => {
                    let data = response.data
                    if (data.code == 1) {
                        visible2.value = false;
                        notification.success({ description: "更改商家状态成功！" });
                        fetchData();
                    } else {
                        notification.error({ description: data.msg });
                    }
                })
            } catch (error) {
                console.error('保存商家信息时发生错误:', error);
            }
        };
        onMounted(() => {
            fetchData();
        });
        // 组件挂载时获取数据  
        return {
            PASSENGER_TYPE_ARRAY,
            business,
            visible,
            visible2,
            form,
            onAdd,
            handleOk,
            businessData,
            columns,
            showModal,
            handleSave,
            handleCancel,
            handleDelete,
        };
    }
});
</script>
<style></style>