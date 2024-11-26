<template>
    <div style="padding: 20px;">
        <h3>演唱会信息</h3>
        <a-form layout="vertical">
            <a-form-item label="演唱会名称">
                <a-input v-model:value="concert.name" disabled />
            </a-form-item>
            <a-form-item label="演出人员">
                <a-input v-model:value="concert.player" disabled />
            </a-form-item>
            <a-form-item label="最低票价">
                <a-input-number v-model:value="concert.lowPrice" disabled :formatter="formatterCurrency"
                    style="width: 100%" />
            </a-form-item>
            <a-form-item label="描述">
                <a-textarea v-model:value="concert.describe" rows="3" disabled />
            </a-form-item>
            <a-form-item label="开始时间">
                <a-input v-model:value="concert.beginTime" format="YYYY-MM-DD HH:mm:ss" style="width: 100%;"
                    placeholder="请选择时间" />
            </a-form-item>
            <a-form-item label="图片">
                <img :src="concert.photo" alt="演唱会图片"
                    style="width: 100%; max-width: 300px; height: auto; max-height: 500px; object-fit: contain; margin: 0 auto; display: block;" />
            </a-form-item>

        </a-form>

        <h3>座位信息</h3>
        <div v-for="(row, rowIndex) in seatRows" :key="rowIndex" style="display: flex; gap: 10px; margin-bottom: 10px;">
            <div v-for="seat in row" :key="seat.seatId" :style="getSeatStyle(seat.seatStatus)"
                style="width: 50px; height: 50px; text-align: center; line-height: 50px; border-radius: 5px; font-size: 12px;">
                {{ seat.seatRow }}-{{ seat.seatCol }}
            </div>
        </div>
        <div style="margin-top: 20px;">
            <p><span style="display: inline-block; width: 20px; height: 20px; background: green;"></span> 启用</p>
            <p><span style="display: inline-block; width: 20px; height: 20px; background: gray;"></span> 停用</p>
            <p><span style="display: inline-block; width: 20px; height: 20px; background: orange;"></span> 维修</p>
            <p><span style="display: inline-block; width: 20px; height: 20px; background: red;"></span> 售罄</p>
        </div>

        <a-button type="primary" style="margin-top: 20px;" @click="closeDrawer">返回</a-button>
    </div>
</template>

<script>
import { defineComponent, ref, watch } from 'vue';
import axios from 'axios';
import dayjs from 'dayjs';
import 'dayjs/locale/zh-cn'; // 引入中文语言包

dayjs.locale('zh-cn'); // 设置中文语言环境

export default defineComponent({
    name: 'EditConcert',
    props: {
        concertId: {
            type: String,
            required: true,
        },
    },
    emits: ['closeDrawer'],
    setup(props, { emit }) {
        const concert = ref({});
        const seatRows = ref([]);
        const formatterCurrency = (value) => `￥${value}`; // 格式化价格

        // 获取演唱会详情
        const fetchConcertDetails = async () => {
            try {
                const response = await axios.get('/concert/concert/getById', {
                    params: { concertId: props.concertId },
                });
                if (response.data.code === 1) {
                    concert.value = response.data.data;

                    // 将座位按行分组
                    seatRows.value = groupSeatsByRow(response.data.data.seatList);
                } else {
                    console.error('获取演唱会详情失败:', response.data.msg);
                }
            } catch (error) {
                console.error('请求失败:', error);
            }
        };

        // 根据座位状态获取样式
        const getSeatStyle = (status) => {
            const colors = {
                0: 'green',
                1: 'gray',
                2: 'orange',
                3: 'red',
                5: 'yellow',
                6: 'blue',
            };
            return {
                backgroundColor: colors[status] || 'white',
                border: '1px solid #ccc',
            };
        };

        // 按行分组座位信息
        const groupSeatsByRow = (seatList) => {
            const grouped = {};
            seatList.forEach((seat) => {
                if (!grouped[seat.seatRow]) {
                    grouped[seat.seatRow] = [];
                }
                grouped[seat.seatRow].push(seat);
            });

            // 转换为数组并按行号排序
            return Object.keys(grouped)
                .sort((a, b) => a - b)
                .map((key) => grouped[key].sort((a, b) => a.seatCol - b.seatCol));
        };

        const closeDrawer = () => emit('closeDrawer');

        watch(
            () => props.concertId,
            () => {
                if (props.concertId) {
                    fetchConcertDetails();
                }
            },
            { immediate: true }
        );

        return {
            concert,
            seatRows,
            formatterCurrency,
            getSeatStyle,
            closeDrawer,
        };
    },
});
</script>

<style scoped>
/* 可根据需求添加样式 */
</style>
