package com.snowalker.shardingjdbc.snowalker.demo.util.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.ArrayList;
import java.util.List;

/**
 * JSON辅助类
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class JsonUtils {

    // 私有构造方法
    private JsonUtils() {
    }

    /**
     * 将对象转换为JSON字符串
     * @param object 对象
     * @return JSON字符串
     */
    public static final String toJSON(Object object) {
        return JSON.toJSONString(object);
    }

    /**
     * 将对象转换为JSON字符串
     * @param object 对象
     * @param prettyFormat 是否格式化
     * @return JSON字符串
     */
    public static final String toJSON(Object object, boolean prettyFormat) {
        return JSON.toJSONString(object, prettyFormat);
    }

    /**
     * 将对象转换为JSON字符串
     * @param object 对象
     * @param prettyFormat 是否格式化
     * @return JSON字符串
     */
    public static final String toJSON(Object object, boolean prettyFormat, boolean outputNull) {
        List<SerializerFeature> features = new ArrayList<SerializerFeature>();
        if (prettyFormat) {
            features.add(SerializerFeature.PrettyFormat);
        }
        if (outputNull) {
            features.add(SerializerFeature.WriteNullStringAsEmpty);
            features.add(SerializerFeature.WriteNullNumberAsZero);
            features.add(SerializerFeature.WriteNullBooleanAsFalse);
            features.add(SerializerFeature.WriteNullListAsEmpty);
            features.add(SerializerFeature.WriteMapNullValue);
        }

        return JSON.toJSONString(object, features.toArray(new SerializerFeature[0]));
    }

    /**
     * 将JSON字符串转换为对象
     * @param text JSON字符串
     * @param clazz 类
     * @return 对象
     */
    public static final <T> T parseJSON(String text, Class clazz) {
        if (text.startsWith("[")) {
            return (T) JSON.parseArray(text, clazz);
        } else {
            return (T) JSON.parseObject(text, clazz);
        }
    }

    /**
     * 将JSON字符串转换为对象
     * @param text JSON字符串
     * @return 对象
     */
    public static final <T> T parseJSON(String text) {
        if (text.startsWith("[")) {
            return (T) JSON.parseArray(text);
        } else {
            return (T) JSON.parseObject(text);
        }
    }

}
