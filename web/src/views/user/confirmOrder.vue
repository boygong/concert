<template>
    <a-card class="confirm-order-page" title="订单确认" bordered>
        <!-- Order Information Display Section -->
        <a-row gutter={16} style="margin-bottom: 20px;">
            <a-col span="12">
                <span class="order-label">订单ID：</span>
                <span>{{ orderData.orderId }}</span>
            </a-col>
            <a-col span="12">
                <span class="order-label">演唱会名称：</span>
                <span>{{ orderData.concertName }}</span>
            </a-col>
            <a-col span="12">
                <span class="order-label">用户名：</span>
                <span>{{ orderData.userName }}</span>
            </a-col>
            <a-col span="12">
                <span class="order-label">总金额：</span>
                <span>¥{{ orderData.amount }}</span>
            </a-col>
            <a-col span="12">
                <span class="order-label">演唱会时间：</span>
                <span>{{ orderData.beginTime }}</span>
            </a-col>
            <a-col span="12">
                <span class="order-label">创建时间：</span>
                <span>{{ orderData.createTime }}</span>
            </a-col>
            <a-col span="24" style="text-align: center; margin-top: 20px;">
                <a-image :src="orderData.image" alt="Concert Image"
                    style="max-width: 100%; max-height: 300px; object-fit: cover;" />
            </a-col>
        </a-row>

        <a-divider style="margin-top: 30px;" />

        <!-- Order Details Display -->
        <h2 style="color: #1890ff; margin-bottom: 16px;">订单明细</h2>
        <a-list v-if="orderData.orderDetails && orderData.orderDetails.length > 0" :dataSource="orderData.orderDetails"
            bordered itemLayout="vertical" style="background-color: #fafafa;">
            <a-list-item v-for="item in orderData.orderDetails" :key="item.orderDetailId"
                style="border-radius: 8px; margin-bottom: 16px; padding: 16px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);">
                <a-row gutter={16}>
                    <a-col span="8">
                        <span class="detail-label">座位编号：</span>
                        <span class="detail-value">{{ item.seatId }}</span>
                    </a-col>
                    <a-col span="8">
                        <span class="detail-label">座位：</span>
                        <span class="detail-value">{{ item.row }}排{{ String.fromCharCode(64 + item.col)
                            }}座（{{ item.col }}列）</span>
                    </a-col>
                    <a-col span="8">
                        <span class="detail-label">单价：</span>
                        <span class="detail-value">¥{{ item.amount }}</span>
                    </a-col>
                </a-row>
            </a-list-item>
        </a-list>

        <!-- Payment Method Selection -->
        <div style="margin-top: 20px;">
            <h3>选择支付方式</h3>
            <a-radio-group v-model:value="payMethod" style="display: flex; justify-content: space-around;">
                <a-radio value='0'>
                    <img src="https://img.icons8.com/?size=100&id=38288&format=png&color=000000" alt="WeChat"
                        style="width: 30px; height: 30px; margin-right: 8px;" />
                    微信
                </a-radio>
                <a-radio value='1'>
                    <img src="https://img.icons8.com/?size=100&id=EUkNwgVEs4yA&format=png&color=000000" alt="Alipay"
                        style="width: 30px; height: 30px; margin-right: 8px;" />
                    支付宝
                </a-radio>
                <a-radio value='2'>
                    <img src="https://img.icons8.com/?size=100&id=22126&format=png&color=000000" alt="银行卡"
                        style="width: 30px; height: 30px; margin-right: 8px;" />
                    银行卡
                </a-radio>
            </a-radio-group>
        </div>

        <!-- Confirm Order Button -->
        <a-button type="primary" style="width: 100%; margin-top: 30px;" @click="confirmOrder">
            确认订单
        </a-button>
    </a-card>
</template>

<script>
import { defineComponent, onMounted, ref } from 'vue';
import { useRoute } from 'vue-router';
import axios from 'axios';
import { notification } from 'ant-design-vue';
import router from '@/router';



export default defineComponent({
    name: 'ConfirmOrderPage',
    setup() {
        const route = useRoute();
        const orderData = ref({});
        const payMethod = ref(0);

        onMounted(() => {
            // 从路由参数或传递的数据中获取订单数据
            if (route.params.orderData) {
                orderData.value = JSON.parse(route.params.orderData);
            }
            console.log(orderData.value.orderDetails);
        });

        const console2 = () => {
            console.log(orderData.value.orderDetails);
            console.log(orderData.value.orderDetails.length);
        }
        const handleItemClick = (item) => {
            console.log('Item clicked:', item);
        }

        const confirmOrder = async () => {
            // 构造请求参数
            const params = {
                orderId: orderData.value.orderId,
                payMethod: payMethod.value,
                amount: orderData.value.amount.toFixed(2),
            };

            try {
                // 调用确认订单接口
                const response =await axios.put('/order/order/confirmOrder', params);
                console.log(response.data);
                // 判断接口返回结果
                if (response.data.code === 1) {
                    // 如果确认成功，跳转到主页
                    router.push('/userPage');
                } else {
                    // 否则，显示错误信息
                    notification.error({
                        message: '错误',
                        description: response.data.msg || '支付失败，请重试。',
                    });
                }
            } catch (error) {
                // 网络或其他错误处理
                notification.error({
                    message: '请求失败',
                    description: '请检查网络连接或稍后再试。',
                });
            }
        }

    return {
        orderData,
        payMethod,
        console2,
        handleItemClick,
        confirmOrder
    };
}});
</script>

<style scoped>
.confirm-order-page {
    padding: 30px;
    background-color: #fff;
}

.order-label {
    font-weight: bold;
    color: #555;
}

a-image {
    margin-top: 16px;
    border-radius: 8px;
}

h2 {
    color: #1890ff;
    font-size: 20px;
}

a-divider {
    border-top: 2px solid #1890ff;
}

.detail-label {
    font-weight: bold;
    color: #555;
}

.detail-value {
    color: #333;
}

a-list-item {
    background-color: #fff;
    border-radius: 8px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    margin-bottom: 16px;
    padding: 16px;
}

a-list-item:hover {
    box-shadow: 0 6px 12px rgba(0, 0, 0, 0.15);
}

a-alert {
    width: 100%;
}
</style>
