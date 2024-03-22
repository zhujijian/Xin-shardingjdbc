package com.snowalker.shardingjdbc.snowalker.demo.enums;

public enum MsgTempEnum {

	/** 抢单成功标题 */
	MSG_TYPE_TMS001_S("MSG_TYPE_TMS001_S", "抢单成功, 单号【{0}】"),
	/** 抢单成功内容 */
	MSG_TYPE_TMS001_B("MSG_TYPE_TMS001_B", "抢单已成功,单号为:【{0}】,成功于:【{1}】"),
	/** 运单支付标题 */
	MSG_TYPE_TMS002_S("MSG_TYPE_TMS002_S", "运单支付, 单号【{0}】"),
	/** 运单支付内容 */
	MSG_TYPE_TMS002_B("MSG_TYPE_TMS002_B", "运单已支付,单号为:【{0}】,支付于:【{1}】,支付状态:【{2}】"),


	// 结束
	;

	private String key;
	private String msg;

	MsgTempEnum(String key, String msg) {
		this.key = key;
		this.msg = msg;
	}

	public String getKey() {
		return key;
	}

	public String getMsg() {
		return msg;
	}

}
