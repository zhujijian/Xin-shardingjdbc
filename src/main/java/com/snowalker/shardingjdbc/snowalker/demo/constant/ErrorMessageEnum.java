package com.snowalker.shardingjdbc.snowalker.demo.constant;


/**
 * @Method_Name : 错误枚举类
 * @Description :
 * * @param null
 * @return
 * @Creation Date : 2020/11/12
 * @Author : fangwenhui
 */
public enum ErrorMessageEnum  {

    ACQUIRE_LOCK_FAIL(10010, "获取分布式锁失败"),
    JOB_ALREADY_EXIST(10020, "JOB信息已存在"),

    ;

    private int code;

    private String info;

    ErrorMessageEnum(int code, String info) {
        this.code = code;
        this.info = info;
    }

    /**
     * 获取异常编码
     *
     * @return 异常码
     */
    public int getCode() {
        return code;
    }

    /**
     * 获取异常信息
     *
     * @return 异常信息
     */
    public String getInfo() {
        return info;
    }
}
