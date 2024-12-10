<template>
    <div>
        <h1>订单管理页面</h1>

        <!-- 模糊查询表单 -->
        <a-form layout="inline" @submit.prevent="onSearch">
            <!-- 表单项代码省略，保持不变 -->
        </a-form>

        <!-- 订单表格 -->
        <a-table :columns="columns" :data-source="orders" :pagination="false" row-key="orderId" style="margin-top: 20px">
            <template v-slot:action="{ record }">
                <a-button @click="viewDetail(record.orderId)" type="link">查看明细</a-button>
            </template>
        </a-table>

        <!-- 订单详情弹框 -->
        <a-modal v-model:visible="detailVisible" title="订单详情" @cancel="closeDetail" @ok="closeDetail">
            <p><strong>订单号:</strong> {{ orderDetail.orderId }}</p>
            <p><strong>用户名:</strong> {{ orderDetail.userName }}</p>
            <p><strong>手机号:</strong> {{ orderDetail.phone }}</p>
            <p><strong>支付状态:</strong> {{ getPayStatus(orderDetail.payStatus) }}</p>
            <p><strong>支付方式:</strong> {{ getPayMethod(orderDetail.payMethod) }}</p>
            <p><strong>金额:</strong> {{ orderDetail.amount }}</p>
            <p><strong>备注:</strong> {{ orderDetail.remark }}</p>

            <!-- 明细列表 -->
            <a-list
                v-if="orderDetail.orderDetails"
                :data-source="orderDetail.orderDetails"
                bordered
                renderItem="detail">
                <template v-slot:item="{ item }">
                    <a-list-item>
                        <p>座位号: {{ item.row }} 排 {{ item.col }} 列</p>
                        <p>金额: {{ item.amount }}</p>
                    </a-list-item>
                </template>
            </a-list>
        </a-modal>

        <!-- 分页组件 -->
        <a-pagination :current="pagination.current" :page-size="pagination.pageSize" :total="pagination.total" :show-total="showTotal" @change="onPageChange" style="margin-top: 20px; text-align: right" />
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
        };
    },
});
</script>

<style scoped>
/* 自定义样式 */
</style>