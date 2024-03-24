package com.snowalker.shardingjdbc.snowalker.demo;

import com.snowalker.shardingjdbc.snowalker.demo.complex.sharding.constant.DbAndTableEnum;
import com.snowalker.shardingjdbc.snowalker.demo.complex.sharding.entity.OrderNewInfoEntity;
import com.snowalker.shardingjdbc.snowalker.demo.complex.sharding.sequence.KeyGenerator;
import com.snowalker.shardingjdbc.snowalker.demo.complex.sharding.service.OrderNewSerivce;
import com.snowalker.shardingjdbc.snowalker.demo.entity.OrderInfo;
import com.snowalker.shardingjdbc.snowalker.demo.reds.delay.enums.ConsumerTypeEnum;
import com.snowalker.shardingjdbc.snowalker.demo.reds.delay.service.RedisDelayQueueService;
import com.snowalker.shardingjdbc.snowalker.demo.reds.enrity.Task;
import com.snowalker.shardingjdbc.snowalker.demo.service.OrderService;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SnowalkerShardingjdbcDemoApplicationTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(SnowalkerShardingjdbcDemoApplicationTests.class);

    @Resource(name = "orderService")
    OrderService orderService;
    @Resource
    RedisDelayQueueService redisDelayQueueService;


    @Test
    public void testRedis(){
        Task task = new Task();
        String str = UUID.randomUUID().toString();
        String s = str.replaceAll("-", "");
        System.out.println(s);
        Date date = DateUtils.addMilliseconds(new Date(), 10);
        task.setBody(s);
        task.setTaskId(s);
        task.setTopic(ConsumerTypeEnum.BIDSPECSTART.name());
        task.setRetry(1);
        task.setDelay(date.getTime());
        redisDelayQueueService.addTask(task);
    }

    @Test
    public void testInsertOrderInfo() {
        for (int i = 0; i < 1000; i++) {
            long userId = i + 1;
//            long orderId = KeyGenerator.getKey();
            long orderId = i + 1;
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setUserName("snowalker");
            orderInfo.setUserId(userId);
            orderInfo.setOrderId(orderId);
            int result = orderService.addOrder(orderInfo);
            if (1 == result) {
                LOGGER.info("入库成功,orderInfo={}", orderInfo);
            } else {
                LOGGER.info("入库失败,orderInfo={}", orderInfo);
            }
        }
    }

    /**
     * 默认规则跨片归并
     */
    @Test
    public void testQueryList() {
        List<OrderInfo> list = new ArrayList<>();
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderId(2l);
        orderInfo.setUserId(2l);
        list = orderService.queryOrderInfoList(orderInfo);
        LOGGER.info(list.toString());
    }


    @Test
    public void testQueryById() {
        OrderInfo queryParam = new OrderInfo();
        queryParam.setUserId(8l);
        queryParam.setOrderId(8l);
        OrderInfo queryResult = orderService.queryOrderInfoByOrderId(queryParam);
        if (queryResult != null) {
            LOGGER.info("查询结果:orderInfo={}", queryResult);
        } else {
            LOGGER.info("查无此记录");
        }
    }

    @Autowired
    KeyGenerator keyGenerator;

    /**
     * 测试分布式主键生成
     */
    @Test
    public void testGenerateId() {
        for (int i = 0; i < 1000; i++) {
            // 支付宝或者微信uid
            String outId = "1232132131241241243123" + i;
            LOGGER.info("获取id开始");
            String innerUserId = keyGenerator.generateKey(DbAndTableEnum.T_USER, outId);
            LOGGER.info("外部id={},innerUserId={}", outId, innerUserId);
            String orderId = keyGenerator.generateKey(DbAndTableEnum.T_NEW_ORDER, innerUserId);
            LOGGER.info("外部id={},innerUserId={},orderId={}", outId, innerUserId, orderId);
        }

    }


    @Autowired
    OrderNewSerivce orderNewSerivce;

    /**
     * 测试新的订单入库
     */
    @Test
    public void testNewOrderInsert() {
        // 支付宝或者微信uid
        for (int i = 0; i < 1000; i++) {
            String outId = "1232132131241241243126" + i;
            LOGGER.info("获取id开始");
            String innerUserId = keyGenerator.generateKey(DbAndTableEnum.T_USER, outId);
            LOGGER.info("外部id={},内部用户={}", outId, innerUserId);
            String orderId = keyGenerator.generateKey(DbAndTableEnum.T_NEW_ORDER, innerUserId);
            LOGGER.info("外部id={},内部用户={},订单={}", outId, innerUserId, orderId);
            OrderNewInfoEntity orderInfo = new OrderNewInfoEntity();
            orderInfo.setUserName("snowalker");
            orderInfo.setUserId(innerUserId);
            orderInfo.setOrderId(orderId);
            orderNewSerivce.addOrder(orderInfo);
        }

    }

    /**
     * 测试订单明细查询
     */
    @Test
    public void testQueryNewOrderById() {
//        String orderId = "OD030001012403160040094480801540";
//        String userId = "UD030002012403160040094470800647";
        OrderNewInfoEntity orderInfo = new OrderNewInfoEntity();
//        orderInfo.setOrderId(orderId);

//        orderInfo.setUserId(userId);
//        List<String> orderIds = new ArrayList<>();
//        orderIds.add("OD030001012403160040090740801534");
//        orderIds.add("OD030001012403160040092960801535");
//        orderIds.add("OD020000012403160040090660802430");

        List<String> userIds = new ArrayList<>();
        userIds.add("UD020002012403160040090720800473");
        userIds.add("UD010000012403160040092950800833");
        userIds.add("UD020001012403160040090640803630");
        userIds.add("UD030002012403160040094470800647");
        orderInfo.setUserIds(userIds);
//        orderInfo.setOrderIds(orderIds);
//        orderInfo.setUserName("lisi");
        System.out.println(orderNewSerivce.queryOrderInfoList(orderInfo));
    }

    /**
     * 测试订单列表查询
     */
    @Test
    public void testQueryNewOrderList() {
        String userId = "UD030001011903261549424973200007";
        OrderNewInfoEntity orderInfo = new OrderNewInfoEntity();
        orderInfo.setUserId(userId);
        List<OrderNewInfoEntity> list = new ArrayList<>();
        list = orderNewSerivce.queryOrderInfoList(orderInfo);
        System.out.println(list);
    }
}
