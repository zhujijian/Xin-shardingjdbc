package com.snowalker.shardingjdbc.snowalker.demo.reds.delay.factory;

import com.snowalker.shardingjdbc.snowalker.demo.reds.delay.annotations.ConsumerType;
import com.snowalker.shardingjdbc.snowalker.demo.reds.delay.enums.ConsumerTypeEnum;
import com.snowalker.shardingjdbc.snowalker.demo.reds.delay.service.ConsumerService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ConsumerFactory implements ApplicationContextAware {
    /**
     * ApplicationContextAware接口实现了 setApplicationContext 方法
     * PostConstruct注解跟afterPropertiesSet在此时可以被替代
     * 但是afterPropertiesSet是接口调用 效率好一点 PostConstruct是反射
     * 构造方法>PostConstruct>afterPropertiesSet>init-method
     * Constructor > @Autowired >@PostConstruct > InitializingBean > init-method
     */
    private ApplicationContext applicationContext;//spring容器上下文 继承ApplicationContextAware接口后注入

    private final static ConcurrentHashMap<String, ConsumerService> CONSUMERSMAP = new ConcurrentHashMap<>();


    @PostConstruct
    private void initData() {
        Map<String, Object> consumerTypeMap = applicationContext.getBeansWithAnnotation(ConsumerType.class);
        for (Map.Entry entry : consumerTypeMap.entrySet()) {
            Object consumerService = entry.getValue();
            if (consumerService instanceof ConsumerService) {
                loadHolder((ConsumerService) consumerService);
            } else {
                throw new RuntimeException(String.format("注解使用异常,类:%s使用注解:%s错误！", entry.getKey(), consumerService.getClass().getAnnotation(ConsumerType.class)));
            }
        }
    }


    private void loadHolder(ConsumerService consumerService) {
        ConsumerType annotation = consumerService.getClass().getAnnotation(ConsumerType.class);
        ConsumerTypeEnum consumerTypeEnum = annotation.value();
        if(consumerTypeEnum==null)
            throw new RuntimeException(String.format("注解使用异常,类:%s使用注解:%s错误,缺少ConsumerTypeEnum属性值！", consumerService.getClass().getSimpleName(), consumerService.getClass().getAnnotation(ConsumerType.class)));
        CONSUMERSMAP.put(consumerTypeEnum.name() , consumerService);
    }

    public static ConsumerService getConsumers(@NonNull ConsumerTypeEnum consumerTypeEnum ) {
        return CONSUMERSMAP.get(consumerTypeEnum.name());
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext=applicationContext;
    }
}
