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

        <!-- 是否选座单选框 -->
        <a-row>
            <a-col :span="24">
                <a-radio-group v-model:value="isSelectSeat"  :disabled="concertDetails.isSelected === 1">
                    <a-radio value = '0' >选择座位</a-radio>
                    <a-radio value = '1' >不选择座位</a-radio>
                </a-radio-group>
            </a-col>
            购买：<a-input-number v-model:value="seatNum" :min="1" :max="10" :disabled="isSelectSeat.value == 0" />张
        </a-row>

        <!-- 座位选择区域 -->
        <div class="seat-selection">
            <h2>座位选择</h2>
            <div class="seat-map">
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
        <a-button type="primary" @click="createOrder" class="submit-button">
            创建订单
        </a-button>

        <!-- 重置按钮 -->
        <a-button @click="resetSelection" class="reset-button">
            重置选择
        </a-button>
        <a-button @click="console2">
            调试
        </a-button>

        <!-- 备注输入框 -->
        <div class="remark-section">
            <label for="remark">备注：</label>
            <input type="text" v-model="remark" id="remark" placeholder="输入备注信息">
        </div>
    </div>
</template>

<script>
import { ref, onMounted, defineComponent } from 'vue';
import { useRoute } from 'vue-router';
import axios from 'axios';
import { message, notification } from 'ant-design-vue';
import store from '@/store';

export default defineComponent({
    name: "CreateOrderPage",
    setup() {
        const concertDetails = ref({});
        const seatList = ref([]);
        const selectedSeats = ref([]);
        const selectedSeatIds = ref([]);
        const seatRows = ref([]);
        const seatCols = ref([]);
        const totalPrice = ref(0);
        const remark = ref("");
        const isSelectSeat = ref(0);  // 默认选择座位
        const seatNum = ref(0);  // 选择座位数

        const route = useRoute();
        const concertId = route.params.concertId;

        const fetchConcertDetails = async () => {
            try {
                const response = await axios.get(`/concert/concert/getById?concertId=${concertId}`);
                const data = response.data.data;
                concertDetails.value = data;
                seatList.value = data.seatList;

                const rows = Math.max(...seatList.value.map(seat => seat.seatRow));
                const cols = Math.max(...seatList.value.map(seat => seat.seatCol));

                seatRows.value = Array.from({ length: rows }, (_, i) => i + 1);
                seatCols.value = Array.from({ length: cols }, (_, i) => i + 1);

                // 打印确认
                console.log("Concert Details:", concertDetails.value);
            } catch (error) {
                console.error('获取演唱会信息失败:', error);
                message.error(`获取演唱会信息失败: ${error.message || '未知错误'}`);
            }
        };

        // 在组件加载时获取演唱会数据
        onMounted(fetchConcertDetails);

        const getSeatClass = (row, col) => {
            const seat = seatList.value.find(seat => seat.seatRow === row && seat.seatCol === col);
            if (!seat) return "";

            const isSelectable = concertDetails.value.isSelected === 0;

            switch (seat.seatStatus) {
                case 0:
                    return isSelectable ? (selectedSeatIds.value.includes(seat.seatId) ? "seat selected" : "seat available") : "seat available";
                case 1:
                    return "seat disabled";
                case 2:
                    return "seat repair";
                case 3:
                case 5:
                case 6:
                    return "seat sold";
                default:
                    return "seat";
            }
        };

        const getSeatLabel = (row, col) => {
            return `${row}${String.fromCharCode(64 + col)}`;
        };

        const selectSeat = (row, col) => {
            if (concertDetails.value.isSelected === 1) {
                message.warning("该演唱会无法选择座位！");
                return;
            }

            const seat = seatList.value.find(seat => seat.seatRow === row && seat.seatCol === col);
            if (!seat || seat.seatStatus !== 0) {
                message.warning("该座位不可选！");
                return;
            }

            const seatLabel = getSeatLabel(row, col);
            const seatId = seat.seatId;

            const index = selectedSeatIds.value.indexOf(seatId);
            if (index === -1) {
                selectedSeatIds.value.push(seatId);
                selectedSeats.value.push(seatLabel);
            } else {
                selectedSeatIds.value.splice(index, 1);
                selectedSeats.value.splice(selectedSeats.value.indexOf(seatLabel), 1);
            }

            updateTotalPrice();
        };

        const updateTotalPrice = () => {
            totalPrice.value = selectedSeatIds.value.reduce((total, seatId) => {
                const seat = seatList.value.find(s => s.seatId === seatId);
                if (seat) {
                    total += seat.price || concertDetails.value.lowPrice;
                }
                return total;
            }, 0);
        };

        const createOrder = async () => {
            let num = 0;
            console.log(isSelectSeat.value==1);
            
            if(isSelectSeat.value==1){
                num = seatNum.value;
            }else{
                num = selectedSeatIds.value.length;
            }
            const orderData = {
                userId: store.state.user.userId,
                concertId: concertDetails.value.concertId,
                isSelected: isSelectSeat.value,
                seatNum: num,
                seatIdList:selectedSeatIds.value,
                remark: remark.value
            };

            if (concertDetails.value.isSelected === 0 && selectedSeatIds.value.length > 0) {
                orderData.selectedSeatIds = selectedSeatIds.value;
            }

            try {
                const response = await axios.post("/order/order/createOrder", orderData);
                const data = response.data;
                if (response.data.code === 1) {
                    message.success("订单创建成功！");
                } else {
                    notification.error({message:"订单创建失败:"+data.msg});
                }
            } catch (error) {
                message.error(`创建订单失败: ${error.message}`);
            }
        };

        const resetSelection = () => {
            selectedSeats.value = [];
            selectedSeatIds.value = [];
            totalPrice.value = 0;
        };

        const console2=()=>{
            console.log(isSelectSeat.value);
            console.log(concertDetails.value.isSelected);
        }

        return {
            concertDetails,
            seatList,
            selectedSeats,
            selectedSeatIds,
            seatRows,
            seatCols,
            totalPrice,
            remark,
            isSelectSeat,
            seatNum,
            getSeatClass,
            getSeatLabel,
            selectSeat,
            updateTotalPrice,
            createOrder,
            resetSelection,
            console2
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
    transition: background-color 0.3s;
}

.seat.available {
    background-color: green;
    color: white;
}

.seat.disabled {
    background-color: gray;
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
    border: 2px solid blue;
}

</style>