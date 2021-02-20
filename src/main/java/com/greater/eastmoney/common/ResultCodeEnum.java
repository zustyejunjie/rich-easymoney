package com.greater.eastmoney.common;

/**
 *
 */
public enum ResultCodeEnum {

    /** 请求处理成功 */
    SUCCESS("000000", "请求处理成功"),
    /** 系统错误 */
    SYSTEM_ERROR("999999", "系统错误");
    private final String code;
    private final String desc;

    ResultCodeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

}
