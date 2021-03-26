package com.greater.eastmoney.proxy;

/**
 * @Author: yejj
 * @Date: 2021/3/24 16:43
 * @Description:
 **/
public class UserDao implements IUserDao {

    @Override
    public void save() {
        System.out.println("实现类执行保存操作");
    }


    @Override
    public void delete() {
        System.out.println("实现类执行删除操作");
    }
}