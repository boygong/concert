<template>
    <div class="create-order-page">
        <!-- 仅在 concertDetails 已经加载后才渲染 -->
        <a-row v-if="concertDetails">
            <a-col :span="24" class="concert-header">
                <h1>{{ concertDetails.name }}</h1>
                <img :src="concertDetails.photo" alt="Concert Photo" class="concert-image" />
                <p>时间：{{ concertDetails.beginTime }}</p>
                <p>最低票价：¥{{ concertDetails.lowPrice }}</p>
                <p>{{ concertDetails.describe }}</p>
            </a-col>
        </a-row>

        <!-- 其他内容 -->
        <div class="seat-selection">
            <h2>座位选择</h2>
            <div class="seat-map">
                <!-- 动态生成座位图 -->
                <div class="seat-row" v-for="row in seatRows" :key="row">
                    <div v-for="col in seatCols" :key="col" :class="getSeatClass(row, col)" class="seat"
                        @click="selectSeat(row, col)">
                        <span>{{ getSeatLabel(row, col) }}</span>
                    </div>
                </div>
            </div>
        </div>

        <!-- 选择的座位和价格信息 -->
        <div class="selected-info">
            <h3>已选择座位</h3>
            <div>
                <p v-if="selectedSeats.length > 0">选择的座位：{{ selectedSeats.join(', ') }}</p>
                <p v-if="selectedSeats.length > 0">总价：¥{{ totalPrice }}</p>
                <p v-else>尚未选择座位。</p>
            </div>
        </div>

        <!-- 创建订单按钮 -->
        <a-button type="primary" :disabled="selectedSeats.length === 0" @click="createOrder" class="submit-button">
            创建订单
        </a-button>

        <!-- 重置按钮 -->
        <a-button @click="resetSelection" class="reset-button">
            重置选择
        </a-button>
    </div>
</template>

<script>
import { ref, onMounted, defineComponent } from 'vue';
import { useRoute } from 'vue-router';
import axios from 'axios';
import { message } from 'ant-design-vue';
import store from '@/store';

export default defineComponent({
    name: "CreateOrderPage",
    setup() {
        const concertDetails = ref(null);
        const seatList = ref([]);
        const selectedSeats = ref([]);  // 存储选择的座位标签
        const selectedSeatIds = ref([]);  // 存储选择的 seatId
        const seatRows = ref([]);
        const seatCols = ref([]);
        const totalPrice = ref(0);

        // 获取当前路由参数
        const route = useRoute();
        const concertId = route.params.concertId;

        const fetchConcertDetails = async () => {
            try {
                const response = await axios.get(`/concert/concert/getById?concertId=${concertId}`);
                console.log('API 响应:', response);

                const data = response.data.data;
                concertDetails.value = data;
                seatList.value = data.seatList;

                const rows = Math.max(...seatList.value.map(seat => seat.seatRow));
                const cols = Math.max(...seatList.value.map(seat => seat.seatCol));

                seatRows.value = Array.from({ length: rows }, (_, i) => i + 1);
                seatCols.value = Array.from({ length: cols }, (_, i) => i + 1);
            } catch (error) {
                console.error('获取演唱会信息失败:', error);
                message.error(`获取演唱会信息失败: ${error.message || '未知错误'}`);
            }
        };

        const getSeatClass = (row, col) => {
            const seat = seatList.value.find(seat => seat.seatRow === row && seat.seatCol === col);
            if (!seat) return "";

            // 如果座位被选中，应用紫色背景
            const isSelected = selectedSeatIds.value.includes(seat.seatId);
            if (isSelected) {
                return "seat selected";  // 已选座位为紫色
            }

            switch (seat.seatStatus) {
                case 0:
                    return "seat available"; // 启用
                case 1:
                    return "seat disabled";  // 停用
                case 2:
                    return "seat repair";    // 维修
                case 3:
                case 5:
                case 6:
                    return "seat sold";      // 已售/待支付/停售
                default:
                    return "seat";
            }
        };

        const getSeatLabel = (row, col) => {
            return `${row}${String.fromCharCode(64 + col)}`;
        };

        const selectSeat = (row, col) => {
            const seat = seatList.value.find(seat => seat.seatRow === row && seat.seatCol === col);
            if (!seat || seat.seatStatus !== 0) {
                message.warning("该座位不可选！");
                return;
            }

            const seatLabel = getSeatLabel(row, col);
            const seatId = seat.seatId;

            // 如果座位已经选中，取消选择
            const index = selectedSeatIds.value.indexOf(seatId);
            if (index === -1) {
                selectedSeatIds.value.push(seatId);
                selectedSeats.value.push(seatLabel);
            } else {
                selectedSeatIds.value.splice(index, 1);
                selectedSeats.value.splice(selectedSeats.value.indexOf(seatLabel), 1);
            }
            console.log("选择的座位:",selectedSeatIds.value);
            // 重新计算总价
            updateTotalPrice();
        };

        const updateTotalPrice = () => {
            totalPrice.value = selectedSeatIds.value.reduce((total, seatId) => {
                const seat = seatList.value.find(s => s.seatId === seatId);
                if (seat) {
                    total += seat.price || concertDetails.value.lowPrice; // 如果座位有价格字段，使用它，否则使用最低票价
                }
                return total;
            }, 0);
        };

       let user = store.state.user;

        const createOrder = async () => {
            if (selectedSeatIds.value.length === 0) {
                message.error("请选择至少一个座位！");
                return;
            }
            console.log(user.userId)
            try {
                const response = await axios.post("/order/order/createOrder", {
                    userId: user.userId,
                    concertId: concertDetails.value.concertId,
                    isSelected:1,
                    seatNum:selectedSeatIds.value.length,
                    seatIdList: selectedSeatIds.value,  // 使用 seatIdList 创建订单
                });

                if (response.data.code === 1) {
                    message.success("订单创建成功！");
                } else {
                    message.error("订单创建失败！");
                }
            } catch (error) {
                message.error("网络错误，订单创建失败！");
            }
        };

        const resetSelection = () => {
            selectedSeats.value = [];
            selectedSeatIds.value = [];
            totalPrice.value = 0;
        };

        onMounted(fetchConcertDetails);

        return {
            concertDetails,
            seatList,
            selectedSeats,
            selectedSeatIds,
            seatRows,
            seatCols,
            totalPrice,
            getSeatClass,
            getSeatLabel,
            selectSeat,
            createOrder,
            resetSelection,
            user
        };
    },
});
</script>

<style scoped>
.create-order-page {
    padding: 20px;
}

.concert-header {
    text-align: center;
}

.concert-image {
    max-width: 100%;
    margin-top: 10px;
}

.seat-selection {
    margin-top: 20px;
}

.seat-map {
    display: flex;
    flex-direction: column;
    align-items: center;
}

.seat-row {
    display: flex;
    margin-bottom: 10px;
}

.seat {
    width: 40px;
    height: 40px;
    margin: 5px;
    display: flex;
    justify-content: center;
    align-items: center;
    border-radius: 4px;
    cursor: pointer;
    font-size: 12px;
}

.seat.available {
    background-color: green;
    color: white;
}

.seat.disabled {
    background-color: grey;
    color: white;
}

.seat.repair {
    background-color: yellow;
    color: black;
}

.seat.sold {
    background-color: red;
    color: white;
}

.seat.selected {
    background-color: purple;
    color: white;
    border: 2px solid blue; /* 紫色已选座位 */
}

.submit-button {
    margin-top: 20px;
}

.reset-button {
    margin-top: 10px;
    background-color: #f0f0f0;
    border: 1px solid #d9d9d9;
    color: #595959;
}
.selected-info {
    margin-top: 20px;
    font-size: 16px;
}
</style>
