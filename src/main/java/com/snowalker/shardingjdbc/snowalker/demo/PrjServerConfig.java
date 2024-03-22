package com.snowalker.shardingjdbc.snowalker.demo;


import com.snowalker.shardingjdbc.snowalker.demo.complex.sharding.strategy.SnoWalkerComplexShardingDB;
import com.snowalker.shardingjdbc.snowalker.demo.util.redis.*;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.IOException;

@Configuration
@EnableTransactionManagement
public class PrjServerConfig {

	private static final Logger log = LoggerFactory.getLogger(SnoWalkerComplexShardingDB.class);

//	@Value("${spring.redis.redisson.config}")
//	private String redissionConfig;

	@Value("${spring.redis.redisson.config.singleServerConfig.address}")
	private String address;
	@Value("${spring.redis.redisson.config.singleServerConfig.password}")
	private String password;
	@Value("${spring.redis.redisson.config.singleServerConfig.database}")
	private int database;
//	@Value("${spring.redis.redisson.config.singleServerConfig.idleConnectionTimeout}")
//	private Integer  idleConnectionTimeout;
//	@Value("${spring.redis.redisson.config.singleServerConfig.connectTimeout}")
//	private Integer  connectTimeout;
	@Value("${spring.redis.redisson.config.singleServerConfig.timeout}")
	private Integer  timeout;
//	@Value("${spring.redis.redisson.config.singleServerConfig.retryAttempts}")
//	private Integer  retryAttempts;
//	@Value("${spring.redis.redisson.config.singleServerConfig.retryInterval}")
//	private Integer  retryInterval;
//	@Value("${spring.redis.redisson.config.singleServerConfig.subscriptionsPerConnection}")
//	private Integer  subscriptionsPerConnection;
//	@Value("${spring.redis.redisson.config.singleServerConfig.subscriptionConnectionMinimumIdleSize}")
//	private Integer  subscriptionConnectionMinimumIdleSize;
//	@Value("${spring.redis.redisson.config.singleServerConfig.subscriptionConnectionPoolSize}")
//	private Integer  subscriptionConnectionPoolSize;
//	@Value("${spring.redis.redisson.config.singleServerConfig.connectionMinimumIdleSize}")
//	private Integer  connectionMinimumIdleSize;
//	@Value("${spring.redis.redisson.config.singleServerConfig.connectionPoolSize}")
//	private Integer  connectionPoolSize;
//	@Value("${spring.redis.redisson.config.codec.class}")
//	private String redissonConfigCodecClass;
//	@Value("${spring.redis.redisson.config.transportMode}")
//	private String transportMode;
//	@Value("${spring.redis.redisson.config.lockWatchdogTimeout}")
//	private String lockWatchdogTimeout;



	@Bean
	public RedisService redisService(RedisTemplate<Object, Object> redisTemplate) {
		return new RedisServiceImpl(redisTemplate);
	}

	@Bean(name = "redisToolServiceDB")
	public RedisToolService redisToolServiceDB(RedisTemplate<Object, Object> redisTemplateDB1) {
		return new RedisToolServiceImpl(redisTemplateDB1);
	}

	@Bean(destroyMethod = "shutdown")
	public RedissonClient redissonClient() throws IOException {
		Config config = new Config();
		((SingleServerConfig)config.useSingleServer().setTimeout(timeout))
				.setAddress(address)
				.setPassword(password)
				.setDatabase(database);
		RedissonClient redissonClient = Redisson.create(config);
		return redissonClient;
	}

//	@Bean(destroyMethod = "shutdown")
//	public RedissonClient redissonClient(){
//		Config config = new Config();
//		config.useClusterServers();
//		SingleServerConfig serverConfig = config.useSingleServer();
//		serverConfig.setAddress(address);
//		serverConfig.setDatabase(database);
//		serverConfig.setPassword(password);
//
//		serverConfig.setTimeout(timeout);
//
//		//加载redisson一些特殊配置
//		serverConfig.setConnectionPoolSize(connectionPoolSize);
//		serverConfig.setConnectionMinimumIdleSize(connectionMinimumIdleSize);
//		serverConfig.setSubscriptionConnectionMinimumIdleSize(subscriptionConnectionMinimumIdleSize);
//		serverConfig.setSubscriptionConnectionPoolSize(subscriptionConnectionPoolSize);
//		serverConfig.setSubscriptionsPerConnection(subscriptionsPerConnection);
//
//		log.info("加载 redisson配置信息 {}", JSONObject.toJSONString(serverConfig));
////		BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(Redisson.class);
////		beanDefinitionBuilder.addConstructorArgValue(config);
////
////		String redissonClientName = RedissonClient.class.getSimpleName().substring(0,1).toLowerCase() + RedissonClient.class.getSimpleName().substring(1);
////		Object redissonClient = configurableApplicationContext.getBean(redissonClientName);
////		log.info("初次放入的redissonClient实现对象：{}", redissonClient.getClass().getName());
////
////		//创建一个Redisson对象
////		BeanDefinitionRegistry beanDefinitionRegistry = (BeanDefinitionRegistry) configurableApplicationContext;
////		beanDefinitionRegistry.registerBeanDefinition(redissonClientName, beanDefinitionBuilder.getBeanDefinition());
////
////		//这里相当于初始化加载使用
////		redissonClient = configurableApplicationContext.getBean(redissonClientName)
//
//
//		return Redisson.create(config);
//	}


	@Bean
	public RedisContextHolder redisContextHolder(RedisService redisService) {
		return new RedisContextHolder(redisService);
	}






}
