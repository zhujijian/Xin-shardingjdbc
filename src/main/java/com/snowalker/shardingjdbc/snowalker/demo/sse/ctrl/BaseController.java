package com.snowalker.shardingjdbc.snowalker.demo.sse.ctrl;

import com.snowalker.shardingjdbc.snowalker.demo.sse.server.ChannelConstant;
import com.snowalker.shardingjdbc.snowalker.demo.sse.server.SseEmitterUTF8;
import com.snowalker.shardingjdbc.snowalker.demo.sse.server.SseServer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;

@RequestMapping(value = "base")
@RestController
public class BaseController {

    @Resource
    private RedisTemplate redisTemplate;



    /**
     * 发布广播消息
     *
     * @param msg
     */
    @GetMapping(value = "/publish/{msg}")
    public void sendMsg(@PathVariable(value = "msg") String msg) {
        redisTemplate.convertAndSend(ChannelConstant.CHANNEL_GLOBAL_NAME, msg);
    }


    /**
     * 接收消息
     *
     * @return
     * @throws IOException
     */
    @GetMapping(path = "/connect/{username}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitterUTF8 connect(@PathVariable(value = "username") String username) throws IOException {
        SseEmitterUTF8 connect = SseServer.connect(username);
        return connect;
    }

}
