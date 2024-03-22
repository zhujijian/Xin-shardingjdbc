package com.snowalker.shardingjdbc.snowalker.demo.util.redis;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * redis服务的实现类。
 */
@SuppressWarnings("unchecked")
@Slf4j
public class RedisServiceImpl implements RedisService {

	private RedisTemplate<Object, Object> redisTemplate;

	public RedisServiceImpl(RedisTemplate<Object, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;

		GenericFastJsonRedisSerializer genericFastJsonRedisSerializer = new GenericFastJsonRedisSerializer();
		// 设置值（value）的序列化采用genericFastJsonRedisSerializer
		redisTemplate.setValueSerializer(genericFastJsonRedisSerializer);
		redisTemplate.setHashValueSerializer(genericFastJsonRedisSerializer);

		// 设置键（key）的序列化采用StringRedisSerializer。
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setHashKeySerializer(new StringRedisSerializer());


		redisTemplate.afterPropertiesSet();
	}

	@Override
	public Set<String> keys(String pattern) {
		Set<Object> keys = redisTemplate.keys(pattern);
		Set<String> list = new HashSet<String>();
		for (Object obj : keys) {
			list.add(obj.toString());
		}
		return list;
	}

	@Override
	public void set(String key , Object obj , int seconds) {
		redisTemplate.opsForValue().set(key , obj , seconds , TimeUnit.SECONDS);
	}

	@Override
	public void set(String key , Object obj , int seconds, TimeUnit timeUnit) {
		redisTemplate.opsForValue().set(key , obj , seconds , timeUnit);
	}


	@Override
	public <T> T get(String key) {
		return (T) redisTemplate.opsForValue().get(key);
	}

	@Override
	public <T> List<T> mget(Collection<String> keys) {
		Collection<Object> objs = new LinkedList<Object>(keys);
		return (List<T>) redisTemplate.opsForValue().multiGet(objs);
	}

	@Override
	public void del(String... key) {
		List<Object> objs = new LinkedList<Object>();
		for (String k : key) {
			objs.add(k);
		}
		redisTemplate.delete(objs);
	}

	@Override
	public void hset(String key , String field , Object obj , int seconds) {
		redisTemplate.opsForHash().put(key , field , obj);
		if(seconds != -99){
			redisTemplate.expire(key , seconds , TimeUnit.SECONDS);
		}
	}

	@Override
	public void hmset(String key , Map<String, Object> objs , int seconds) {
		redisTemplate.opsForHash().putAll(key , objs);
		redisTemplate.expire(key , seconds , TimeUnit.SECONDS);
	}

	@Override
	public <T> T hget(String key , String field) {
		return (T) redisTemplate.opsForHash().get(key , field);
	}

	@Override
	public <T> List<T> hmget(String key , Collection<String> fields) {
		Collection<Object> hashKeys = new LinkedList<Object>(fields);
		return (List<T>) redisTemplate.opsForHash().multiGet(key , hashKeys);
	}

	@Override
	public void hdel(String key , String... fields) {
		Object[] objs = null;
		if (fields != null) {
			objs = new Object[fields.length];
			for (int i = 0; i < fields.length; i++) {
				objs[i] = fields[i];
			}
		}
		redisTemplate.opsForHash().delete(key , objs);
	}

	@Override
	public <T> List<T> lrange(String key , int start , int end) {
		return (List<T>) redisTemplate.opsForList().range(key , start , end);
	}





	@Override
	public Long hlen(String key) {
		return redisTemplate.opsForHash().size(key);
	}

	@Override
	public void expire(String key , int seconds) {
		redisTemplate.expire(key , seconds , TimeUnit.SECONDS);
	}


	@Override
	public <T> T execute(DefaultRedisScript<T> redisScript , List<Object> keyList , Object... args) {
		return redisTemplate.execute(redisScript , keyList , args);
	}

	@Override
	public boolean exists(String key){
		return  redisTemplate.hasKey(key);
	}

	//=============================== sort set =================================
	/**
	 * 添加指定元素到有序集合中
	 * @param key
	 * @param score
	 * @param value
	 * @return
	 */
	@Override
	public boolean sortSetAdd(String key, String value, double score, Long time, TimeUnit unit) {
		Boolean add = redisTemplate.opsForZSet().add(key, value, score);
		long members = redisTemplate.opsForZSet().zCard(key);
		if (0 == members && null != time) {
			redisTemplate.expire(key, time, unit);
		}
		return 	add;
	}

	/**
	 * 有序集合中对指定成员的分数加上增量 increment
	 * @param key
	 * @param value
	 * @param i
	 * @return
	 */
	@Override
	public double sortSetZincrby(String key, String value, double i) {
		//返回新增元素后的分数
		return redisTemplate.opsForZSet().incrementScore(key, value, i);
	}

	/**
	 * 向zset结构中指定元素的分数加上增量 i，新增key设置有效期
	 * @param key
	 * @param value
	 * @param i
	 * @param time
	 * @param unit
	 * @return
	 */
	@Override
	public double sortSetZincrby(String key, String value, double i, Long time, TimeUnit unit) {
		long members = redisTemplate.opsForZSet().zCard(key);
		//返回新增元素后的分数
		double sorce = redisTemplate.opsForZSet().incrementScore(key, value, i);
		//新增key则设置有效期
		if (0 == members && null != time) {
			redisTemplate.expire(key, time, unit);
		}
		return sorce;
	}

	/**
	 * 获得有序集合指定范围元素 (从大到小)
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	@Override
	public Set<Object> sortSetReverseRange(String key, int start, int end) {
		return redisTemplate.opsForZSet().reverseRange(key, start, end);
	}

	/**
	 * 将有序集合oZset和oZsetList的交集合并为nZset
	 *
	 * @param oZset
	 * @param oZsetList
	 * @param nZset
	 */
	@Override
	public Long sortSetIntersection(String oZset, List<String> oZsetList,
									String nZset) {
		Long size = redisTemplate.opsForZSet().intersectAndStore(oZset, Collections.singleton(oZsetList), nZset,
				RedisZSetCommands.Aggregate.SUM, null);
		return size;
	}

	/**
	 * 将有序集合oZset和oZsetList的并集合并为nZset,每个集合的权重相同
	 *
	 * @param oZset
	 * @param oZsetList
	 * @param nZset
	 */
	@Override
	public Long sortSetUnionAndStore(String oZset, List<String> oZsetList,
									 String nZset) {
		int[] weights = new int[oZsetList.size() + 1];
		for (int i = 0; i <= oZsetList.size(); ++i) {
			weights[i] = 1;
		}
		Long size = redisTemplate.opsForZSet().unionAndStore(oZset, Collections.singleton(oZsetList), nZset,
				RedisZSetCommands.Aggregate.SUM, RedisZSetCommands.Weights.of(weights));
		return size;
	}

	/**
	 * 从zset结构中移除元素
	 */
	@Override
	public Long sortSetRemove(String key, Object... values) {
		return redisTemplate.opsForZSet().remove(key, values);
	}

	@Override
	public Long rPush(String redisKey, String val, long time, TimeUnit timeUnit) {
		Long num = redisTemplate.opsForList().rightPush(redisKey, val);
		if (null == num) {
			return 0l;
		}
		if (num > 0 && time > 0){
			Boolean res = redisTemplate.expire(redisKey, time, timeUnit);
		}
		return num;
	}

	@Override
	public Boolean setNx(String key , Object obj , long time, TimeUnit timeUnit) {
		Boolean aBoolean = redisTemplate.opsForValue().setIfAbsent(key, obj, time, timeUnit);
		if (aBoolean) {
			return true;
		}
		return false;

	}

	/** 解锁 */
	@Override
	public Boolean unlock(String key, String value) {
		try {
			String currentValue = (String) redisTemplate.opsForValue().get(key);
			if (!StringUtils.isEmpty(currentValue) && currentValue.equals(value)) {
				Boolean delete = redisTemplate.opsForValue().getOperations().delete(key);
				return delete;
			}
		} catch (Exception e) {
			log.error("RedisLock unlock error ", e);
		}
		return false;
	}

	@Override
	public Long getExpireTime(String key) {
		// 获取key的过期时间，返回的时间已秒为单位，如果该值没有设置过期时间，就返回-1，不存在值返回-2
		Long expire = redisTemplate.opsForValue().getOperations().getExpire(key);
		return expire;
	}


}
