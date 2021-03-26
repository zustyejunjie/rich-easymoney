package com.greater.eastmoney.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 动态代理对象
 * @Author: yejj
 * @Date: 2021/3/24 16:45
 * @Description:
 **/
public class UserProxyFactory {

    private Object target;
    public UserProxyFactory(Object o){
        this.target = o;
    }
    public Object getProxyInstance(){
        return Proxy.newProxyInstance(target.getClass().getClassLoader(),
                target.getClass().getInterfaces(), new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        System.out.println("执行的方法名称"+method.getName());
                        method.invoke(target,args);
                        System.out.println("执行结束，方法"+method.getName());
                        return null;
                    }
                });
    }
}
