package com.greater.eastmoney.user;

import lombok.Data;

/**
 * 登陆用户信息，登陆后初始化该对象，存放在threadlocal中
 * @Author: yejj
 * @Date: 2021/3/16 10:38
 * @Description:
 **/
@Data
public class LoginUser {

    private String userId;
    private String token;
}
