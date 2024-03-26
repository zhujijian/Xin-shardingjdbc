package com.snowalker.shardingjdbc.snowalker.demo.sse.server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class RedisMessageSubscriber implements MessageListener {



    @Resource
    private RedisMessageListenerContainer redisMessageListenerContainer;

    /**
     * 订阅消息：将订阅者添加到指定的频道
     */
    @PostConstruct
    public void subscribeToChannel() {
        //广播消息
        redisMessageListenerContainer.addMessageListener(this, new ChannelTopic(ChannelConstant.CHANNEL_GLOBAL_NAME));
    }

    @Override
    public void onMessage(Message message, byte[] bytes) {
        String channel = new String(message.getChannel(), StandardCharsets.UTF_8);
        String messageBody = new String(message.getBody(), StandardCharsets.UTF_8);
        log.info("Received message: " + messageBody + " from channel: " + channel);
        SseServer.sendMessage(messageBody);
    }
}

