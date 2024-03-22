package com.snowalker.shardingjdbc.snowalker.demo.util;

import com.snowalker.shardingjdbc.snowalker.demo.enums.MsgTempEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 移动端消息通知列表
 */
@Data
@ApiModel(value = "移动端消息通知放入消息队列的内容")
public class MqMsgNotifyReq implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "消息类型(枚举【MST_TYPE】)", required = true)
	private String msgType;

	@ApiModelProperty(value = "消息来源(枚举【PLAT_APP】)", required = true)
	private String msgApp;

	@ApiModelProperty(value = "消息通知对象", required = true)
	private String msgTo;

	@ApiModelProperty(value = "消息标题模板", required = true)
	private MsgTempEnum subjectTemp;

	@ApiModelProperty(value = "消息内容模板", required = true)
	private MsgTempEnum bodyTemp;

	@ApiModelProperty(value = "消息标题")
	private Object[] subjectKeyWords;

	@ApiModelProperty(value = "消息内容")
	private Object[] bodyKeyWords;

	@ApiModelProperty(value = "托运人")
	private String consignor;


}
