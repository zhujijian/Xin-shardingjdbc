package com.snowalker.shardingjdbc.snowalker.demo.reds.delay.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Lenovo
 * @Title: ConsumerTypeEnum
 * @Description: 枚举
 * @date 2020/11/12
 */
public enum ConsumerTypeEnum {

	BIDPUSH("竞价发布"),
	BIDSTART("竞价开始"),
	BIDEND("竞价结束"),
	BIDDEFINE("竞价定价"),
	RMBIDRKEY("删除redis key"),
	BIDSPECSTART("竞价专场开始"),
	BIDSPECEND("竞价专场结束"),
	;


	private String topicDesc;

	ConsumerTypeEnum(String topicDesc) {
		this.topicDesc = topicDesc;
	}


	public static ConsumerTypeEnum formatEnum(String value) {
		if (StringUtils.isBlank(value)) {
			return null;
		}
		try {
			return ConsumerTypeEnum.valueOf(value);
		} catch (Exception e) {
			return null;
		}
	}

	public String getTopicDesc() {
		return topicDesc;
	}

	public void setTopicDesc(String topicDesc) {
		this.topicDesc = topicDesc;
	}
}
