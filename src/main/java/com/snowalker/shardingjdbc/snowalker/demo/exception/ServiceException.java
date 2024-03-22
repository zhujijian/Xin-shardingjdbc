package com.snowalker.shardingjdbc.snowalker.demo.exception;


import com.snowalker.shardingjdbc.snowalker.demo.enums.ErrorCodeEnum;
import com.snowalker.shardingjdbc.snowalker.demo.util.MessageUtils;

/***
 * 业务异常基类
 */
public class ServiceException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	private String err;
	private String msg;
	private Object[] args;

	public ServiceException(ErrorCodeEnum err, Object... args) {
		super(MessageUtils.formatMessage(err.getMsg(), args));
		this.err = err.getErr();
		this.msg = err.getMsg();
		this.args = args;
	}

	/***
	 * 构造函数
	 *
	 * @param err     错误代码
	 * @param msg 错误消息
	 * @param args    不定长参数
	 */
	public ServiceException(String err, String msg, Object... args) {
		super(MessageUtils.formatMessage(msg, args));
		this.err = err;
		this.msg = msg;
		this.args = args;
	}

	/***
	 * @return 错误代码
	 */
	public String getErr() {
		return err;
	}

	/***
	 * @return 错误消息
	 */
	public String getMsg() {
		return msg;
	}

	/***
	 * @return 不定长参数
	 */
	public Object[] getArgs() {
		return args;
	}

}
