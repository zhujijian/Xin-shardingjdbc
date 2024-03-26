package com.snowalker.shardingjdbc.snowalker.demo;


import com.snowalker.shardingjdbc.snowalker.demo.complex.sharding.strategy.SnoWalkerComplexShardingDB;
import com.snowalker.shardingjdbc.snowalker.demo.util.redis.RedisContextHolder;
import com.snowalker.shardingjdbc.snowalker.demo.util.redis.RedisService;
import com.snowalker.shardingjdbc.snowalker.demo.util.redis.RedisServiceImpl;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.redisson.config.TransportMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.IOException;

@Configuration
@EnableTransactionManagement
public class ServerConfig {

	private static final Logger log = LoggerFactory.getLogger(SnoWalkerComplexShardingDB.class);

//	@Value("${spring.redis.redisson.config}")
//	private String redissionConfig;

	@Value("${spring.redis.redisson.config.singleServerConfig.address}")
	private String address;
	@Value("${spring.redis.redisson.config.singleServerConfig.password}")
	private String password;
	@Value("${spring.redis.redisson.config.singleServerConfig.database}")
	private int database;
	@Value("${spring.redis.redisson.config.singleServerConfig.idleConnectionTimeout}")
	private Integer  idleConnectionTimeout;
	@Value("${spring.redis.redisson.config.singleServerConfig.connectTimeout}")
	private Integer  connectTimeout;
	@Value("${spring.redis.redisson.config.singleServerConfig.timeout}")
	private Integer  timeout;
	@Value("${spring.redis.redisson.config.singleServerConfig.retryAttempts}")
	private Integer  retryAttempts;
	@Value("${spring.redis.redisson.config.singleServerConfig.retryInterval}")
	private Integer  retryInterval;
	@Value("${spring.redis.redisson.config.singleServerConfig.subscriptionsPerConnection}")
	private Integer  subscriptionsPerConnection;
	@Value("${spring.redis.redisson.config.singleServerConfig.subscriptionConnectionMinimumIdleSize}")
	private Integer  subscriptionConnectionMinimumIdleSize;
	@Value("${spring.redis.redisson.config.singleServerConfig.subscriptionConnectionPoolSize}")
	private Integer  subscriptionConnectionPoolSize;
	@Value("${spring.redis.redisson.config.singleServerConfig.connectionMinimumIdleSize}")
	private Integer  connectionMinimumIdleSize;
	@Value("${spring.redis.redisson.config.singleServerConfig.connectionPoolSize}")
	private Integer  connectionPoolSize;
	@Value("${spring.redis.redisson.config.lockWatchdogTimeout}")
	private Integer lockWatchdogTimeout;



	@Bean
	public RedisService redisService(RedisTemplate<Object, Object> redisTemplate) {
		return new RedisServiceImpl(redisTemplate);
	}

	@Bean(destroyMethod = "shutdown")
	public RedissonClient redissonClient() throws IOException {
		Config config = new Config();
		((SingleServerConfig)config.useSingleServer().setTimeout(timeout))
				.setAddress(address)
				.setPassword(password)
				.setDatabase(database)
				.setConnectionPoolSize(connectionPoolSize)
				.setConnectionMinimumIdleSize(connectionMinimumIdleSize)
				.setConnectionPoolSize(connectionPoolSize)
				.setSubscriptionConnectionMinimumIdleSize(subscriptionConnectionMinimumIdleSize)
				.setSubscriptionConnectionPoolSize(subscriptionConnectionPoolSize)
				.setSubscriptionsPerConnection(subscriptionsPerConnection)
				.setRetryInterval(retryInterval)
				.setRetryAttempts(retryAttempts)
				.setConnectTimeout(connectTimeout)
				.setIdleConnectionTimeout(idleConnectionTimeout);

		config.setCodec(new JsonJacksonCodec());
		config.setTransportMode(TransportMode.NIO);
		config.setLockWatchdogTimeout(lockWatchdogTimeout);
		RedissonClient redissonClient = Redisson.create(config);
		return redissonClient;
	}

	@Bean
	public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory connectionFactory) {
		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		// 添加其他配置，如线程池大小等
		return container;
	}

	@Bean
	public RedisContextHolder redisContextHolder(RedisService redisService) {
		return new RedisContextHolder(redisService);
	}


}
