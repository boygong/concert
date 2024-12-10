<template>
    <div>
        <h1>订单管理页面</h1>


        <!-- 模糊查询表单 -->
        <a-form layout="inline" @submit.prevent="onSearch">
            <a-form-item label="订单号">
                <a-input v-model:value="filters.orderId" placeholder="请输入订单号" />
            </a-form-item>
            <a-form-item label="用户编号">
                <a-input v-model:value="filters.userId" placeholder="请输入用户编号" />
            </a-form-item>
            <a-form-item label="演唱会编号">
                <a-input v-model:value="filters.concertId" placeholder="请输入演唱会编号" />
            </a-form-item>
            <a-form-item label="用户名">
                <a-input v-model:value="filters.userName" placeholder="请输入用户名" />
            </a-form-item>
            <a-form-item label="手机号">
                <a-input v-model:value="filters.phone" placeholder="请输入手机号" />
            </a-form-item>
            <a-form-item label="支付状态">
                <a-select v-model:value="filters.payStatus" placeholder="选择支付状态">
                    <a-select-option value="0">未支付</a-select-option>
                    <a-select-option value="1">已支付</a-select-option>
                    <a-select-option value="2">退款</a-select-option>
                </a-select>
            </a-form-item>
            <a-form-item label="订单状态">
                <a-select v-model:value="filters.orderStatus" placeholder="选择订单状态">
                    <a-select-option value="0">待确认</a-select-option>
                    <a-select-option value="1">已确认</a-select-option>
                    <a-select-option value="2">已付款</a-select-option>
                    <a-select-option value="3">已取票</a-select-option>
                    <a-select-option value="4">已取消</a-select-option>
                    <a-select-option value="5">已拒绝</a-select-option>
                </a-select>
            </a-form-item>
            <a-form-item label="金额范围">
                <a-input v-model:value="filters.lowAmount" placeholder="最低金额" style="width: 120px" />
                <span> - </span>
                <a-input v-model:value="filters.highAmount" placeholder="最高金额" style="width: 120px" />
            </a-form-item>
            <a-form-item label="时间范围">
                <a-date-picker v-model:value="filters.startTime" placeholder="开始时间" style="width: 200px" />
                <span> - </span>
                <a-date-picker v-model:value="filters.endTime" placeholder="结束时间" style="width: 200px" />
            </a-form-item>
            <a-form-item>
                <a-button type="primary" @click="onSearch">查询</a-button>
                <a-button @click="resetFilters" style="margin-left: 8px">重置</a-button>
            </a-form-item>
        </a-form>

        <!-- 订单表格 -->
        <a-table :columns="columns" :data-source="orders" :pagination="false" row-key="orderId"
            style="margin-top: 20px">
            <template v-slot:action="{ record }">
                <a-button @click="viewDetail(record.orderId)" type="link">查看明细</a-button>
            </template>
        </a-table>

        <!-- 订单详情弹框 -->
        <!-- 订单详情弹框 -->
        <a-modal v-model:visible="detailVisible" title="订单详情" @cancel="closeDetail" @ok="closeDetail" ok-text="确认"
            cancel-text="取消">
            <a-form :model="orderDetail" :label-col="{ span: 4 }" :wrapper-col="{ span: 20 }">
                <a-form-item label="订单号">
                    <a-input v-model:value="orderDetail.orderId" disabled />
                </a-form-item>
                <a-form-item label="用户名">
                    <a-input v-model:value="orderDetail.userName" disabled />
                </a-form-item>
                <a-form-item label="手机号">
                    <a-input v-model:value="orderDetail.phone" disabled />
                </a-form-item>
                <a-form-item label="支付状态">
                    <a-select v-model:value="orderDetail.payStatus" disabled>
                        <a-select-option :value="0">未支付</a-select-option>
                        <a-select-option :value="1">已支付</a-select-option>
                        <a-select-option :value="2">退款</a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item label="支付方式">
                    <a-select v-model:value="orderDetail.payMethod" disabled>
                        <a-select-option :value="0">线上支付</a-select-option>
                        <a-select-option :value="1">线下支付</a-select-option>
                    </a-select>
                </a-form-item>
                <a-form-item label="金额">
                    <a-input v-model:value="orderDetail.amount" disabled />
                </a-form-item>
                <a-form-item label="备注">
                    <a-input v-model:value="orderDetail.remark" disabled />
                </a-form-item>
                <a-form-item label="订单状态">
                    <a-select v-model:value="orderDetail.orderStatus" disabled>
                        <a-select-option :value="0">待确认</a-select-option>
                        <a-select-option :value="1">已确认</a-select-option>
                        <a-select-option :value="2">已付款</a-select-option>
                        <a-select-option :value="3">已取票</a-select-option>
                        <a-select-option :value="4">已取消</a-select-option>
                        <a-select-option :value="5">已拒绝</a-select-option>
                    </a-select>
                </a-form-item>
            </a-form>
            <!-- 展示订单明细 -->
            <a-table :columns="detailColumns" :data-source="orderDetail.orderDetails" row-key="orderDetailId"
                style="margin-top: 20px" :pagination="false">
                <a-table-column title="座位号" dataIndex="row" key="row" />
                <a-table-column title="座位列" dataIndex="col" key="col" />
                <a-table-column title="价格" dataIndex="amount" key="amount" />
            </a-table>
        </a-modal>

        <!-- 分页组件 -->
        <a-pagination :current="pagination.current" :page-size="pagination.pageSize" :total="pagination.total"
            :show-total="showTotal" @change="onPageChange" style="margin-top: 20px; text-align: right" />
    </div>
</template>



<script>
import { defineComponent, ref, h } from "vue";
import { message, notification } from "ant-design-vue";
import axios from "axios";

export default defineComponent({
    name: "OrderManagement",
    setup() {
        // 查询条件
        const filters = ref({
            orderId: "",
            userId: "",
            concertId: "",
            userName: "",
            phone: "",
            startTime: null,
            endTime: null,
            payStatus: null,
            orderStatus: null,
            lowAmount: "",
            highAmount: "",
        });

        const orderDetail = ref({
            orderId: "",
            userName: "",
            phone: "",
            payStatus: 0,
            payMethod: 0,
            amount: 0,
            remark: "",
            orderStatus: 0,
            orderDetails: [],
        });
        const detailVisible = ref(false);

        // 查看订单详情
        const viewDetail = async (orderId) => {
            console.log(orderId)
            try {
                const response = await axios.get("/order/order/detail", {
                    params: { orderId }
                });

                if (response.data.code === 1) {
                    console.log("查询明细成功");
                    orderDetail.value = response.data.data;
                    detailVisible.value = true;
                } else {
                    notification.error({ message: response.data.msg });
                }
            } catch (error) {
                message.error("加载订单详情失败！");
            }
        };

        // 关闭弹窗
        const closeDetail = () => {
            detailVisible.value = false;
        };

        // 显示总数
        const showTotal = (total) => `总共 ${total} 条`;

        // 表格数据
        const orders = ref([]);

        // 分页信息
        const pagination = ref({
            current: 1,
            pageSize: 4,
            total: 0,
        });

        // 表格列定义
        const columns = [
            {
                title: "订单号",
                dataIndex: "orderId",
                key: "orderId",
            },
            {
                title: "用户编号",
                dataIndex: "userId",
                key: "userId",
            },
            {
                title: "演唱会编号",
                dataIndex: "concertId",
                key: "concertId",
            },
            {
                title: "用户名",
                dataIndex: "userName",
                key: "userName",
            },
            {
                title: "手机号",
                dataIndex: "phone",
                key: "phone",
            },
            {
                title: "座位数",
                dataIndex: "detailNum",
                key: "detailNum",
            },
            {
                title: "金额",
                dataIndex: "amount",
                key: "amount",
            },
            {
                title: "支付状态",
                dataIndex: "payStatus",
                key: "payStatus",
                customRender: ({ text }) => {
                    switch (text) {
                        case 0:
                            return "未支付";
                        case 1:
                            return "已支付";
                        case 2:
                            return "退款";
                        default:
                            return "未知状态";
                    }
                },
            },
            {
                title: "订单状态",
                dataIndex: "orderStatus",
                key: "orderStatus",
                customRender: ({ text }) => {
                    const statusMap = {
                        0: { text: "待确认", color: "orange" },
                        1: { text: "已确认", color: "blue" },
                        2: { text: "已付款", color: "green" },
                        3: { text: "已取票", color: "purple" },
                        4: { text: "已取消", color: "red" },
                        5: { text: "已拒绝", color: "gray" },
                    };

                    const status = statusMap[text] || { text: "未知状态", color: "default" };

                    console.log(status);  // 调试输出，确保 color 正确传递

                    // 使用 h 函数来渲染 a-tag
                    return h('a-tag', { color: status.color }, status.text);
                },
            },
            {
                title: "演出时间",
                dataIndex: "beginTime",
                key: "beginTime",
            },
            {
                title: "创建时间",
                dataIndex: "createTime",
                key: "createTime",
            },
            {
                title: "取消原因",
                dataIndex: "cancelReason",
                key: "cancelReason",
            },
            {
                title: "拒绝原因",
                dataIndex: "rejectionReason",
                key: "rejectionReason",
            },
            {
                title: "订单撤销时间",
                dataIndex: "cancelTime",
                key: "cancelTime",
            },
            {
                title: "修改时间",
                dataIndex: "updateTime",
                key: "updateTime",
            },
            {
                title: "备注",
                dataIndex: "remark",
                key: "remark",
            },
            {
                title: "操作",
                key: "action",
                customRender: ({ record }) => {
                    return h('a', { onClick: () => viewDetail(record.orderId) }, '查看明细');
                },
            },
        ];

        const detailColumns = [
            { title: "座位编码", dataIndex: "seatId", key: "seatId" },
            { title: "座位行", dataIndex: "row", key: "row" },
            { title: "座位列", dataIndex: "col", key: "col" },
            { title: "价格", dataIndex: "amount", key: "amount" },
        ];

        // 查询订单
        const fetchOrders = async () => {
            try {
                const params = {
                    ...filters.value,
                    page: pagination.value.current,
                    size: pagination.value.pageSize,
                };
                // 调试输出查询的参数
                console.log(params);

                const response = await axios.post("/order/order/pageQuery", params);
                if (response.data.code === 1) {
                    orders.value = response.data.data.records;
                    pagination.value.total = response.data.data.total;
                } else {
                    notification.error(response.data.msg);
                }
            } catch (error) {
                message.error("加载订单数据失败！");
            }
        };


        // 查询按钮触发
        const onSearch = () => {
            pagination.value.current = 1;
            fetchOrders();
        };

        // 重置按钮触发
        const resetFilters = () => {
            filters.value = {
                orderId: "",
                userId: "",
                concertId: "",
                userName: "",
                phone: "",
                startTime: null,
                endTime: null,
                payStatus: null,
                orderStatus: null,
                lowAmount: "",
                highAmount: "",
            };
            onSearch();
        };

        // 分页切换
        const onPageChange = (page) => {
            pagination.value.current = page;
            fetchOrders();
        };

        // 初始化加载
        fetchOrders();

        return {
            showTotal,
            filters,
            orders,
            pagination,
            columns,
            onSearch,
            resetFilters,
            onPageChange,
            orderDetail,
            detailVisible,
            detailColumns,
            viewDetail,
            closeDetail,
        };
    },
});
</script>

<style scoped>
/* 自定义样式 */
</style>