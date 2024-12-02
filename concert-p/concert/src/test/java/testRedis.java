import com.gong.concert.common.util.RedisLockUtil;
import com.gong.concert.concert.ConcertApplication;
import com.gong.concert.concert.entity.Seat;
import com.gong.concert.concert.mapper.ConcertMapper;
import com.gong.concert.concert.mapper.SeatMapper;
import jakarta.annotation.Resource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

/**
 * @Author ToastFish
 * @Time 2024/12/2
 */
@SpringBootTest(classes = ConcertApplication.class)
public class testRedis {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @Autowired
    private SeatMapper seatMapper;
    @Autowired
    private RedisLockUtil redisLockUtil;


    @Test
    public void testRedisConnection() {
        redisTemplate.opsForValue().set("test", "Hello Redis!");
        String value = redisTemplate.opsForValue().get("test");
        System.out.println(value);  // 应该输出 "Hello Redis!"
    }

    @Test
    public void testTemplate(){
        // 加锁操作：每次获取座位锁
        boolean lockAcquired = redisLockUtil.lock("seat_lock_" + "1223", 30, 10, 2); // 锁超时时间30秒，最大等待10秒，重试间隔2秒
        System.out.println(lockAcquired);
    }

    @Test
    public void testMapper(){
        List<Seat> byNum = seatMapper.getByNum("1856608493096800256", 2);
        for (Seat seat : byNum) {
            System.out.println(seat);
        }
    }

}
