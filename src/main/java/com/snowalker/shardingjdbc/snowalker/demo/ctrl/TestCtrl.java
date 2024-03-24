package com.snowalker.shardingjdbc.snowalker.demo.ctrl;

import com.snowalker.shardingjdbc.snowalker.demo.reds.delay.enums.ConsumerTypeEnum;
import com.snowalker.shardingjdbc.snowalker.demo.reds.delay.service.RedisDelayQueueService;
import com.snowalker.shardingjdbc.snowalker.demo.reds.enrity.Task;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("test")
public class TestCtrl {

    @Resource
    RedisDelayQueueService redisDelayQueueService;

    @GetMapping("testRedis")
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
}
