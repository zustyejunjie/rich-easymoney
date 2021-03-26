package com.greater.eastmoney.aop;

import java.lang.annotation.*;

/**
 * Dubbo注解
 * 
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Dubbo {

    // 操作名称
    String value() default "未知接口";

}
