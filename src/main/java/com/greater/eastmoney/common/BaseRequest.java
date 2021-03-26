package com.greater.eastmoney.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 请求参数基类
 */
@Data
public abstract class BaseRequest implements Serializable {

    /** 序列号 */
    private static final long serialVersionUID = 3848884122968364361L;

    // 日志追踪号
    public String             correlationId;

    // 接口版本号
    public String             version;

    // 请求IP
    public String             requestIp;

    // 请求时间
    public String             requestTime;

    public abstract String validateLogic();

}
