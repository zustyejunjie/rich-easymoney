package com.greater.eastmoney.proxy;

import com.greater.eastmoney.TestSupport;
import org.junit.Test;

/**
 * @Author: yejj
 * @Date: 2021/3/24 16:52
 * @Description:
 **/
public class TestProxy extends TestSupport {

    @Test
    public void test(){
        IUserDao target = new UserDao();
        System.out.println(target.getClass());  //输出目标对象信息
        IUserDao proxy = (IUserDao) new UserProxyFactory(target).getProxyInstance();
        System.out.println(proxy.getClass());  //输出代理对象信息
        proxy.delete();  //执行代理方法
    }
}
