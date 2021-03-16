package com.greater.eastmoney.superresponse;

/**
 * @Author: yejj
 * @Date: 2021/3/16 10:00
 * @Description:
 **/
public enum CommonResultCodeEnum {

    //成功
    SUCCESS("000000", "成功"),
    // 校验
    PARAM_ILLEGAL("100001", "参数校验未通过"),
    // 系统异常
    SYSTEM_TIMEOUT("999998", "服务调用超时"),
    SYSTEM_ERROR("999999", "系统错误");

    private final String code;

    private final String desc;

    CommonResultCodeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static CommonResultCodeEnum getResultCode(String code) {
        for (CommonResultCodeEnum resultCode : values()) {
            if (resultCode.getCode().equals(code)) {
                return resultCode;
            }
        }
        return SYSTEM_ERROR;
    }
}
