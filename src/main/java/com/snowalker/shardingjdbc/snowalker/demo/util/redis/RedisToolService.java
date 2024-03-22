package com.snowalker.shardingjdbc.snowalker.demo.util.redis;

import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 缓存服务接口，利用此接口来访问缓存
 */
public interface RedisToolService {

	//=================缓存键操作======================

	/**
	 * 得到缓存的键值列表
	 *
	 * @param pattern 缓存键匹配模式
	 * @return 匹配的缓存键
	 */
	Set<String> keys(String pattern);

	//=================缓存形式(KEY-VALUE)======================

	/**
	 * 将数据放入缓存
	 *
	 * @param key     缓存键
	 * @param obj     缓存对象
	 * @param seconds 过期秒数
	 */
	void set(String key, Object obj, int seconds);

	/**
	 * 根据缓存键获得缓存数据
	 *
	 * @param <T> 缓存数据类型
	 * @param key 缓存键
	 * @return 缓存数据
	 */
	<T> T get(String key);

	/**
	 * 根据缓存键获得缓存数据列表
	 *
	 * @param <T>  缓存数据类型
	 * @param keys 缓存键
	 * @return 缓存数据列表
	 */
	<T> List<T> mget(Collection<String> keys);

	/**
	 * 删除缓存
	 *
	 * @param key 缓存键
	 */
	void del(String... key);

	//==================哈希缓存形式(KEY-FIELD-VALUE)=====================

	/**
	 * 将数据放入缓存
	 *
	 * @param key     缓存键
	 * @param field   哈希键
	 * @param obj     缓存数据
	 * @param seconds 过期秒数
	 */
	void hset(String key, String field, Object obj, int seconds);

	/**
	 * 批量将数据放入缓存
	 *
	 * @param key     缓存键
	 * @param objs    Map<哈希键, 缓存数据>
	 * @param seconds 过期秒数
	 */
	void hmset(String key, Map<String, Object> objs, int seconds);

	/**
	 * 得到对应的缓存值
	 *
	 * @param <T>   缓存数据类型
	 * @param key   缓存键
	 * @param field 哈希键
	 * @return 缓存数据
	 */
	<T> T hget(String key, String field);

	/**
	 * 得到对应的缓存值列表
	 *
	 * @param <T>    缓存数据类型
	 * @param key    缓存键列表
	 * @param fields 哈希键
	 * @return 缓存数据
	 */
	<T> List<T> hmget(String key, Collection<String> fields);

	/**
	 * 删除缓存的键值
	 *
	 * @param key    缓存键
	 * @param fields 哈希键
	 */
	void hdel(String key, String... fields);

	/**
	 * 返回哈希键的数量
	 *
	 * @param key 缓存键
	 * @return 缓存个数
	 */
	Long hlen(String key);

	/***
	 * 过期时间设置
	 * @param key 缓存键
	 * @param seconds 过期秒数
	 */
	void expire(String key, int seconds);

	/**
	 * 将key中存储的数值增num
	 *
	 * @param key 缓存建
	 * @param num 每次增加的值
	 */
	Long incr(String key, int num);

	/**
	 * 将key中存储的数值自减num
	 *
	 * @param key
	 * @param num 每次减少的值
	 */
	Long decr(String key, int num);

	/**
	 * 将k1中存储的数值减num，将k2中存储的数值增num
	 *
	 * @param k1  自减key
	 * @param k2  自增key
	 * @param num 每次变化的值
	 * @return
	 */
	List<String> numberCount(String k1, String k2, int num);


	/**
	 * 执行lua脚本  返回String
	 *
	 * @param redisScript   lua脚本
	 * @param keyList  KEYS
	 * @param args   ARGV
	 * @return
	 */
	<T> T execute(DefaultRedisScript<T> redisScript, List<Object> keyList, Object... args);




	/**
	 * zRangeWithScores
	 *
	 * @param key  key
	 * @param start start
	 * @param end  end
	 * @return
	 */
	Set<ZSetOperations.TypedTuple<Object>> zRangeWithScores(String key, int start, int end);

	/**
	 * zRange
	 *
	 * @param key  key
	 * @param start start
	 * @param end  end
	 * @return
	 */
	Set<Object> zRange(String key, int start, int end);


	/**
	 * zCard
	 * @param key
	 * @return
	 */
	Long zCard(String key);


	/**
	 * hset获取手动次数
	 * @param key
	 * @param filed
	 * @param num
	 * @return
	 */
	Long hincr(String key, String filed, Integer num);


	/**
	 * zadd
	 * @param score
	 * @param key
	 * @param member
	 * @return
	 */
	Boolean zadd(String key, String menber, double zsetScore);

	/**
	 * zrank
	 * @param key
	 * @param member
	 * @return
	 */
	Long zrank(String key, String member);



	/**
	 * zscore
	 * @param key
	 * @param account
	 * @return
	 */
	Double zscore(String key, String account);
}
