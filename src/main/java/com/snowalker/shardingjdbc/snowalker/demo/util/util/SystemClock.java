package com.snowalker.shardingjdbc.snowalker.demo.util.util;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;


/**
 * 时间工具类
 */
public final class SystemClock {
	
	/** 系统时钟 */
	private static Clock instance = Clock.systemDefaultZone();
	
    private SystemClock() {
    }

    /***
     * 当前时间毫秒数
     */
    public static long getTimeInMillis() {
    	return instance.millis();
    }
    
    /**
     * 本地日期
     * @return java.time.LocalDate类型
     */
    public static LocalDate getLocalDate() {
    	return LocalDate.now(instance);
    }
    
    /***
     * 本地时间
     * @return java.time.LocalTime类型
     */
    public static LocalTime getLocalTime() {
        return LocalTime.now(instance);
    }
    
    /***
     * 本地日期时间
     * @return java.time.LocalDateTime类型
     */
    public static LocalDateTime getLocalDateTime() {
        return LocalDateTime.now(instance);
    }
    
    /***
     * 返回当前日期时间
     * @return java.util.Date类型
     */
    public static Date getDate() {
        return new Date(instance.millis());
    }
    
    /***
     * 返回当前日期时间
     * @return java.util.Calendar类型
     */
    public static Calendar getCalendar() {
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTimeInMillis(instance.millis());
        return calendar;
    }

    /***
     * 返回当前日期时间
     * @return java.util.Calendar类型
     */
    public static Date getDateBeforeDays(Integer days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(instance.millis());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND,0);
        calendar.add(Calendar.DATE, - days);
        return calendar.getTime();
    }

    /***
     * 返回传入日期后几天
     * @return java.util.Calendar类型
     */
    public static Date getDateAfterDays(Date date, Integer days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date.getTime());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND,0);
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }
    public static Date getLastWeekMonday(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getThisWeekMonday(date));
        cal.add(Calendar.DATE, -7);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND,0);
        return cal.getTime();
    }

    public static Date getThisWeekMonday(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        // 获得当前日期是一个星期的第几天
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        // 获得当前日期是一个星期的第几天
        int day = cal.get(Calendar.DAY_OF_WEEK);
        // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND,0);
        return cal.getTime();
    }

    /**
     * 获取昨天
     * @return String
     * */
    public static Date getYestoday(){
        Calendar cal=Calendar.getInstance();
        cal.add(Calendar.DATE,-1);
        return cal.getTime();
    }

    public static Date getDateDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date.getTime());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND,0);
        return calendar.getTime();
    }
}
