package com.greater.eastmoney.usercontext;

import com.greater.eastmoney.user.LoginUser;

/**
 * 用户信息上下文  （当前操作线程下）
 * @Author: yejj
 * @Date: 2021/3/16 10:12
 * @Description:
 **/
public class UserContextUtil {

    private static final ThreadLocal<LoginUser> userContext = new ThreadLocal<LoginUser>();


    /**
     * 获取当前登录用户信息
     *
     * @return
     */
    public static LoginUser getUserContext() {
        return userContext.get();
    }


    /**
     * 设置当前登录用户上下文
     *
     * @param loginUser
     */
    public static void setUserContext(LoginUser loginUser) {
        userContext.set(loginUser);
    }


    /**
     * 清除当前登录用户上下文
     */
    public static void removeUserContext() {
        userContext.remove();
    }

}
