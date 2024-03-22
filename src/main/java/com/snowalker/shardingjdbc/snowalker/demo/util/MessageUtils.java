package com.snowalker.shardingjdbc.snowalker.demo.util;


import com.snowalker.shardingjdbc.snowalker.demo.enums.MsgTempEnum;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;
import java.util.Locale;

/***
 * 消息工具类
 */
public class MessageUtils {

	/**
	 * 格式化输出消息
	 * 
	 * @param msg  消息内容
	 * @param args 消息参数
	 * @return 格式后的消息
	 */
	public static String formatMessage(String msg, Object... args) {
		String ret = msg;
		if (ret != null && args != null) {
			for (Object obj : args) {
				String str = obj != null ? obj.toString() : "";
				ret = ret.replaceFirst("\\{\\}", str);
			}
		}
		return ret;
	}

	/**
	 * 格式化消息通知的标题
	 * 
	 * @param msgNotify 封装了具体内容的消息通知类
	 * @return
	 */
	public static String formatMstBody(MqMsgNotifyReq msgNotify) {
		// 获取消息内容模板
		String bodyTemplate = gainBodyTemplate(msgNotify);

		// 获取消息内容
		Object[] bodyKeyWords = msgNotify.getBodyKeyWords();

		// 转换并返回
		return convertRet(bodyTemplate, bodyKeyWords);

	}

	/**
	 * 格式化消息通知的标
	 *
	 * @param msgNotify 封装了具体内容的消息通知类
	 * @return
	 */
	public static String formatMstBody(MqMsgBatchNotifyReq msgNotify) {
		// 获取消息内容模板
		String bodyTemplate = gainBodyTemplate(msgNotify);

		// 获取消息内容
		Object[] bodyKeyWords = msgNotify.getBodyKeyWords();

		// 转换并返回
		return convertRet(bodyTemplate, bodyKeyWords);

	}

	private static String gainBodyTemplate(MqMsgNotifyReq msgNotify) {
		MsgTempEnum bodyTemp = msgNotify.getBodyTemp();
		String bodyTemplate = bodyTemp == null ? "" : bodyTemp.getMsg();
		return bodyTemplate;
	}

	private static String gainBodyTemplate(MqMsgBatchNotifyReq msgNotify) {
		MsgTempEnum bodyTemp = msgNotify.getBodyTemp();
		String bodyTemplate = bodyTemp == null ? "" : bodyTemp.getMsg();
		return bodyTemplate;
	}

	private static String convertRet(String ret, Object[] keyWords) {
		if (!StringUtils.isEmpty(ret) && keyWords != null && keyWords.length > 0) {
			MessageFormat mf = new MessageFormat(ret, Locale.CHINA);
			ret = mf.format(keyWords);
		}
		return ret;
	}

	/**
	 * 格式化消息通知的标题
	 * 
	 * @param msgNotify 封装了具体内容的消息通知类
	 * @return
	 */
	public static String formatMsgSubject(MqMsgNotifyReq msgNotify) {
		// 获取消息标题枚举模板
		String subjectTemplate = gainSubjectTemplate(msgNotify);
		
		// 获取标题words
		Object[] subjectKeyWords = msgNotify.getSubjectKeyWords();
		
		return convertRet(subjectTemplate, subjectKeyWords);

	}

	/**
	 * 格式化消息通知的标题2
	 *
	 * @param msgNotify 封装了具体内容的消息通知类
	 * @return
	 */
	public static String formatMsgSubject(MqMsgBatchNotifyReq msgNotify) {
		// 获取消息标题枚举模板
		String subjectTemplate = gainSubjectTemplate(msgNotify);

		// 获取标题words
		Object[] subjectKeyWords = msgNotify.getSubjectKeyWords();

		return convertRet(subjectTemplate, subjectKeyWords);

	}

	private static String gainSubjectTemplate(MqMsgNotifyReq msgNotify) {
		MsgTempEnum subjectTemp = msgNotify.getSubjectTemp();
		return subjectTemp == null ? "" : subjectTemp.getMsg();
	}

	private static String gainSubjectTemplate(MqMsgBatchNotifyReq msgNotify) {
		MsgTempEnum subjectTemp = msgNotify.getSubjectTemp();
		return subjectTemp == null ? "" : subjectTemp.getMsg();
	}

}
