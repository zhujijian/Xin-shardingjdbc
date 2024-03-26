package com.snowalker.shardingjdbc.snowalker.demo.sse.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SseServer {
    /**
     * 存储用户的连接
     */
    public static Map<String, SseEmitterUTF8> sseMap = new ConcurrentHashMap<>();

    /**
     * 建立连接
     *
     * @param username
     * @throws IOException
     */
    public static SseEmitterUTF8 connect(String username) throws IOException {
        if (!sseMap.containsKey(username)) {
            //设置超时时间(和token有效期一致，超时后不再推送消息)，0表示不过期。默认30秒，超过时间未完成会抛出异常：AsyncRequestTimeoutException
            SseEmitterUTF8 sseEmitter = new SseEmitterUTF8(0L);
            sseEmitter.send(String.format("%s号用户，连接成功！", username));
            sseEmitter.onCompletion(() -> sseMap.remove(username));
            sseEmitter.onTimeout(() -> sseMap.remove(username));
            sseEmitter.onError(throwable -> sseMap.remove(username));
            sseMap.put(username, sseEmitter);
            return sseEmitter;
        } else {
            SseEmitterUTF8 sseEmitterUTF8 = sseMap.get(username);
            sseEmitterUTF8.send(String.format("%s，用户连接成功！", username));
            return sseEmitterUTF8;
        }
    }

    /**
     * 发送消息
     *
     * @param message
     */
    public static synchronized void sendMessage(String message) {
        List<String> removeList = new ArrayList<>();
        for (Map.Entry<String, SseEmitterUTF8> entry : sseMap.entrySet()) {
            String username = entry.getKey();
            try {
                SseEmitterUTF8 sseEmitterUTF8 = entry.getValue();
                sseEmitterUTF8.onCompletion(() -> sseMap.remove(username));
                sseEmitterUTF8.onTimeout(() -> sseMap.remove(username));
                sseEmitterUTF8.onError(throwable -> sseMap.remove(username));
                sseEmitterUTF8.send(message);
            } catch (IOException e) {
                //发送不成功，将该用户加入移除列表
                removeList.add(username);
            }
        }
        //移除连接异常的用户
        removeList.forEach(item -> sseMap.remove(item));
    }
}
