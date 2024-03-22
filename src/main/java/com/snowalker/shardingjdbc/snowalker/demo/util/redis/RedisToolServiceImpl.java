package com.snowalker.shardingjdbc.snowalker.demo.util.redis;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * redis服务的实现类。
 */
@SuppressWarnings("unchecked")
public class RedisToolServiceImpl implements RedisToolService {

	private RedisTemplate<Object, Object> redisToolTemplate;

	@SuppressWarnings("rawtypes")
	public RedisToolServiceImpl(RedisTemplate<Object, Object> redisToolTemplate) {
		this.redisToolTemplate = redisToolTemplate;
	}

	@Override
	public Set<String> keys(String pattern) {
		Set<Object> keys = redisToolTemplate.keys(pattern);
		Set<String> list = new HashSet<String>();
		for (Object obj : keys) {
			list.add(obj.toString());
		}
		return list;
	}

	@Override
	public void set(String key , Object obj , int seconds) {
		redisToolTemplate.opsForValue().set(key , obj , seconds , TimeUnit.SECONDS);
	}

	@Override
	public <T> T get(String key) {
		return (T) redisToolTemplate.opsForValue().get(key);
	}

	@Override
	public <T> List<T> mget(Collection<String> keys) {
		Collection<Object> objs = new LinkedList<Object>(keys);
		return (List<T>) redisToolTemplate.opsForValue().multiGet(objs);
	}

	@Override
	public void del(String... key) {
		List<Object> objs = new LinkedList<Object>();
		for (String k : key) {
			objs.add(k);
		}
		redisToolTemplate.delete(objs);
	}

	@Override
	public void hset(String key , String field , Object obj , int seconds) {
		redisToolTemplate.opsForHash().put(key , field , obj);
		redisToolTemplate.expire(key , seconds , TimeUnit.SECONDS);
	}

	@Override
	public void hmset(String key , Map<String, Object> objs , int seconds) {
		redisToolTemplate.opsForHash().putAll(key , objs);
		redisToolTemplate.expire(key , seconds , TimeUnit.SECONDS);
	}

	@Override
	public <T> T hget(String key , String field) {
		return (T) redisToolTemplate.opsForHash().get(key , field);
	}

	@Override
	public <T> List<T> hmget(String key , Collection<String> fields) {
		Collection<Object> hashKeys = new LinkedList<Object>(fields);
		return (List<T>) redisToolTemplate.opsForHash().multiGet(key , hashKeys);
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
		redisToolTemplate.opsForHash().delete(key , objs);
	}

	@Override
	public Long hlen(String key) {
		return redisToolTemplate.opsForHash().size(key);
	}

	@Override
	public void expire(String key , int seconds) {
		redisToolTemplate.expire(key , seconds , TimeUnit.SECONDS);
	}

	@Override
	public Long incr(String key , int num) {

		StringBuilder script = new StringBuilder();
//        script.append(" local stepNum = ARGV[1] ");
		script.append(" local taxIncomeTaskKeyErr = KEYS[1] ");
//        script.append(" local targetNum = redis.call('get',taxIncomeTaskKey) ");
//        script.append(" if targetNum >= stepNum ");
		script.append(" redis.call('incrby',taxIncomeTaskKeyErr,1) ");
//        script.append(" return targetNum");
//        script.append(" else ");
//        script.append(" return '0' ");
		script.append(" return redis.call('GET', taxIncomeTaskKeyErr) ");
//        script.append(" end ");

		DefaultRedisScript<String> longDefaultRedisScript = new DefaultRedisScript<>(script.toString() , String.class);
		String res = redisToolTemplate.execute(longDefaultRedisScript , Collections.singletonList(key));
		if (StringUtils.isBlank(res)) {
			res = "-1";
		}
		return Long.valueOf(res);
	}

	@Override
	public Long decr(String key , int num) {
		StringBuilder script = new StringBuilder();
//        script.append(" local stepNum = tonumber(ARGV[1]) ");
		script.append(" local taxIncomeTaskKey = KEYS[1] ");
		script.append(" local targetNum = redis.call('GET', taxIncomeTaskKey) ");
		script.append(" if tonumber(targetNum) < 1 then ");
//        script.append(" then redis.call('decrby',targetNum, 1) ");
		script.append(" return 0 ");
		script.append(" else redis.call('decrby',taxIncomeTaskKey, 1) ");
		script.append(" return redis.call('GET', taxIncomeTaskKey) ");
		script.append(" end ");

		DefaultRedisScript<String> longDefaultRedisScript = new DefaultRedisScript<>(script.toString() , String.class);
		String res = redisToolTemplate.execute(longDefaultRedisScript , Collections.singletonList(key));
		if (StringUtils.isBlank(res)) {
			res = "-1";
		}
		return Long.valueOf(res);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<String> numberCount(String k1 , String k2 , int num) {
		StringBuilder script = new StringBuilder();
		script.append(" local numStep = ARGV[1] ");
		script.append(" local taxIncomeTaskKey = KEYS[1] ");
		script.append(" local taxIncomeTaskKeyErr = KEYS[2] ");
		script.append(" local targetNum = redis.call('GET', taxIncomeTaskKey) ");
		script.append(" local targetNumErr = redis.call('GET', taxIncomeTaskKeyErr) ");
		script.append(" if numStep ");
		script.append(" then numStep = tonumber(numStep) ");
		script.append(" else numStep = 0 end ");
		script.append(" redis.call('incrby',taxIncomeTaskKeyErr, numStep) ");
		script.append(" if tonumber(targetNum) < numStep then ");
		script.append(" return {0, redis.call('GET', taxIncomeTaskKeyErr)} ");
		script.append(" else redis.call('decrby',taxIncomeTaskKey, numStep) ");
		script.append(" return {redis.call('GET', taxIncomeTaskKey), redis.call('GET', taxIncomeTaskKeyErr)} ");
		script.append(" end ");

		DefaultRedisScript<List> longDefaultRedisScript = new DefaultRedisScript<>(script.toString() , List.class);
		List<String> res = redisToolTemplate.execute(longDefaultRedisScript , Arrays.asList(k1 , k2) , num);

		return res;
	}


	@Override
	public <T> T execute(DefaultRedisScript<T> redisScript , List<Object> keyList , Object... args) {
		return redisToolTemplate.execute(redisScript , keyList , args);
	}




	/**
	 * 获取zset 带分数
	 *
	 * @param key
	 * @return
	 */
	@Override
	public Set<ZSetOperations.TypedTuple<Object>> zRangeWithScores(String key , int start , int end) {
		return redisToolTemplate.opsForZSet().rangeWithScores(key , start , end);
	}

	@Override
	public Set<Object> zRange(String key , int start , int end) {
		return redisToolTemplate.opsForZSet().range(key , start , end);
	}

	@Override
	public Long zCard(String key) {
		return  redisToolTemplate.opsForZSet().zCard(key);
	}


	@Override
	public Long hincr(String key , String filed , Integer num) {
		if (null == num) {
			return redisToolTemplate.opsForHash().increment(key , filed , 1);
		}
		return redisToolTemplate.opsForHash().increment(key , filed , num);
	}



	@Override
	public Boolean zadd(String key , String menber , double zsetScore) {
		return redisToolTemplate.opsForZSet().add(key,menber,Double.valueOf(zsetScore));
	}

	/**
	 * zrank
	 * @param key
	 * @param member
	 * @return
	 */
	@Override
	public Long zrank(String key , String member) {
		return redisToolTemplate.opsForZSet().rank(key,member);
	}

	/**
	 * 查询价格
	 * @param s
	 * @param account
	 * @return
	 */
	@Override
	public Double zscore(String key , String account) {
		Double score = redisToolTemplate.opsForZSet().score(key , account);
		return score;
	}


}
