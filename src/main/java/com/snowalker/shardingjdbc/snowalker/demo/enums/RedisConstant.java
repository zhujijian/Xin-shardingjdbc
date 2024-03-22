package com.snowalker.shardingjdbc.snowalker.demo.enums;

/***
 * Redis常量
 */
public class RedisConstant {

	private RedisConstant() {
	}

	/**
	 * 缓存过期时间
	 */
	public static final int TOKEN_EXPIRES_IN = 120;// 过期时间，单位分钟

	public static final int USER_EXPIRES_IN = 30;// 过期时间，单位分钟

	public static final int WX_EXPIRES_IN = 365 * 12 * 60;// 过期时间，单位分钟

	public static final int MYBATIS_EXPIRES_IN = 30;// 过期时间，单位分钟

	public static final int VERTIFY_CODE_LEN = 6;// 验证码长度

	public static final int VERTIFY_CODE_TIME = 60 * 5;// 验证码时间(秒)

	public static final int VERTIFY_CODE_TIME2 = 60 * 2;// 验证码时间(秒)

	public static final int USER_LOGIN_NUM_EXPIRES_IN = 60 * 5; // 用户连续输入密码错误累计间隔

	public static final int BANK_CARD_NUM = 60 * 10;// 银行卡过期时间

	public static final int SINOIOV_TOKEN_NUM = 24 * 60 * 60;// 中交兴路令牌过期时间

	public static final int BELONG_DATE_NUM = 24 * 60 * 60; // 属期接口数据时间

	public static final int G7_EXPIRES_IN = 5 * 60;//过期时间(秒)

	public static final int COMMENT_EXPIRES_IN = 24 * 60 * 60;//过期时间(秒)

	public static final int LBS_LATEST = 24 * 60; //过期时间，单位分钟

	public static final int LBS_LATEST_WARN = 24 * 60; //过期时间，单位分钟

	public static final int LBS_TRACK_HIGH_WARN = 24 * 60; //过期时间，单位分钟

	public static final int LBS_TRACK_LOW_WARN = 24 * 60; //过期时间，单位分钟


	// 所有mybatis二级缓存都应以"mybatis:"开头
	// 如枚举字典为 "mybatis:com.soon.fpl.sys.dao.CodeMapper"
	public static final String PREFIX_MYBATIS = "mybatis:";// Mybatis缓存键

	// 所有spring缓存都应以"spring:"开头
	// 如枚举字典为 "spring:com.soon.fpl.sys.service.CodeService"
	public static final String PREFIX_SPRING = "spring:";// Spring缓存键

	// 令牌缓存
	public static final String PREFIX_TOKEN = "token:";// 访问令牌缓存键

	// 用户缓存
	public static final String PREFIX_USER = "user:";// 用户缓存键

	// 所有短信发送缓存都应以"sms:"开头
	// 如注册短信验证码为 "sms:regist:13764220001"
	public static final String PREFIX_SMS = "sms:";// Sms缓存键

	public static final String APPROVE = "approve";// 印章授权验证码

	public static final String CONTSEALKEY = "contsealkey"; // 合同签章验证码KEY

	public static final String CONTSUPSEALKEY = "contsupsealkey"; // 补充协议签章验证码KEY

	public static final String DELAYKEY = "delaykey";//申请延期验证码key


	// 令牌
	public static final String TOKEN_TAX = "tax:token"; // 天津税务令牌

	public static final String BANK_CARD = "backcard:"; // 银行卡缓存键

	public static final String SINOIOV_TOKEN = "sinoiov:token"; // 中交兴路令牌

	public static final String BELONG_DATE_KEY = "belongdate:"; // 属期接口key,后面还要加上YYYY_MM_DD的当天日期格式

	public static final String G7_BAR_CODE = "g7:bc:";//生成的二维码(G7用)

	public static final String WLHY_TOKEN = "wlhy:token"; // 天津交委

	public static final String COMMENT_TOKEN = "comment:token";//门户网站游客留言评论令牌

	public static final String POR_COUNTER = "por:counter";//门户首页各方统计

	public static final String LBS_GPS_LATEST = "lbs:latest";//lbs GPS最新位置

	public static final String LBS_GPS_LATEST_WARN = "lbs:latest_warn";//lbs GPS最新位置预警处理

	public static final String LBS_GPS_TRACK_HIGH_WARN = "lbs:track_high_warn";//lbs GPS历史轨迹高速预警处理

	public static final String LBS_GPS_TRACK_LOW_WARN = "lbs:track_low_warn";//lbs GPS历史轨迹低速预警处理


	/**
	 * 竞价redis_key
	 */
	public static final String BIDDING_RANKING_KEY = "BR:";  //竞价出价排名key

	public static final String AUTO_RANKING_KEY = "ARK:";  //自动报价名单

	public static final String AUTO_BIDDING_TIME_KEY = "ABT:";  //自动出价次数

	public static final String MANUAL_BIDDING_TIME_KEY = "MBT:";  //手动出价次数

	public static final String TOTAL_BIDDING_TIME_KEY = "TT:";  //最大出价次数

	public static final String STOP_QUOTE_KEY = "SQK:";  //停止报价标记

	public static final String FS_QUOTE_KEY = "FS:";  //第一次KPI和时间的SCORE

	public static final int BID_DB = 1;// 竞价的db下标

	public static final String ENQ_SPEC_RANKING_KEY = "ESR:";  //竞价专场排名key

	/**
	 * 最大并发登录
	 */
	public static final String CONCURRENT_LOGIN_TOKENS = "CLTS:";//并发用户token

	public static final String WX_CONCURRENT_LOGIN_TOKENS = "WXCLTS:";//微信并发用户token

	/**
	 * 契约锁ticket
	 */
	public static final int QYS_TICKET_TIME = 10;// 验证码时间(秒)
	public static final String QYS_TICKET = "QYS_TICKET:";//契约锁ticket


	/**
	 *
	 */
	public static final String CDP_KEY = "CDP_KEY:";// 数据宝key

	public static final String JUHE_KEYS = "JUHE_KEYS:";// 聚合数据key


	/**
	 * 附近车辆缓存key
	 */
	public static final String NEAR_CAR_INFO_KEY = "NCI_KEY";// 附近车辆缓存

	// 用户缓存
	public static final String PREFIX_CAPTCHA = "captcha:";// 验证码缓存键

	public static final int TRANS_REPORT_TIME = 60*30;// 验证码时间(秒)
	public static final String TRANS_REPORT_KEY = "TRANS_REPORT_KEY:";//契约锁ticket


	public static final String PREFIX_ENQ_SPEC_REFLUSH_RANKING = "ESRK:";//专场报价单排名刷新间隔限制
	public static final int PREFIX_ENQ_SPEC_REFLUSH_RANKING_INTERVAL = 3;//专场报价单排名刷新间隔
	public static final String PREFIX_ENQ_SPEC_QUOTE_LOCK = "ESQLK:";//专场报价锁定key  一个时间内只能发生一次提交，防止重复提交情况出现
	public static final String PREFIX_ENQ_SPEC_UPLOAD_FILE_LOCK = "ESUFLK:";//专场报价上传锁定key 一个时间内只能发生一次提交，防止重复提交情况出现

	public static final String SHENTONG_ACCESSTOKEN="SHENTONG:ACCESSTOKEN:";
	public static final String SHENTONG_VEHICLE_DATE="SHENTONG:VEHICLE:DATE:";
	public static final String SHENTONG_BID_NO="SHENTONG:BID_NO:";

	/**
	 *  合并key
	 */
	public static final String HD_PICK_MERGE_KM_KEY = "HD_PICK_MKK";//
	public static final String HD_SIGN_MERGE_KM_KEY = "HD_SIGN_MKK";//

	/**
	 * method——lock 锁
	 * */
	public static final String METHOD_LOCK = "METHOD_LOCK:";//


	public static final String SCREEN_V2_STRUCK_PRJ_AMT_KEY = "SCREEN_V2:STRUCK_AMT_KEY";// 大屏V2 -结构性降费
	public static final String SCREEN_V2_STRUCK_MARGE_KEY = "SCREEN_V2:STRUCK_MARGE_KEY";// 大屏V2 - 合并非合并
	public static final String SCREEN_V2_STRUCK_TOP_KEY = "SCREEN_V2:STRUCK_TOP_KEY"; //大屏V2 -降费排行
	public static final String SCREEN_V2_TASK_FLY_KEY = "SCREEN_V2:TASK_FLY_KEY"; //大屏V2 -提单飞线
	public static final String SCREEN_V2_STOCK_COUNT_KEY = "SCREEN_V2:STOCK_COUNT_KEY"; //大屏V2 -仓库统计
	public static final String SCREEN_V2_DRIVER_STAR_KEY = "SCREEN_V2:DRIVER_STAR_KEY"; //大屏V2 -司机星级
	public static final String SCREEN_V2_TASK_FLY_TOP_KEY = "SCREEN_V2:TASK_FLY_TOP_KEY"; //大屏V2 -提单飞线和排名

	public static final String INF_DING_TALK_KEY = "DING_TALK:DING_TALK_KEY"; //大屏V2 -提单飞线

}
