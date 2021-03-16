package com.greater.eastmoney.superresponse;

import lombok.Data;

/**
 * 泛型类  接口返回值全部使用该类   dubbo接口和api接口
 * @Author: yejj
 * @Date: 2021/3/16 9:56
 * @Description:
 **/
@Data
public class ResultModel<T> {

    private static final long serialVersionUID = 1L;

    /**
     * 返回数据总数量
     */
    private Integer count;

    /**
     * 接口返回对象
     */
    private T result;

    /**
     * 接口返回code
     */
    private String code;
    /**
     * 接口返回msg
     */
    private String msg;


    public ResultModel(String code, String msg) {
        this.code= code;
        this.msg = msg;
    }

    public ResultModel(T result,String code,String msg) {
        this.result = result;
        this.count = 1;
        this.code= code;
        this.msg = msg;
    }

    public ResultModel(int count,T result,String code,String msg) {
        this.count = count;
        this.result = result;
        this.code= code;
        this.msg = msg;
    }

    public ResultModel<T> setSuccess(T result){
        this.result = result;
        this.msg = CommonResultCodeEnum.SUCCESS.getDesc();
        this.code= CommonResultCodeEnum.SUCCESS.getCode();
        return this;
    }

    public ResultModel<T> setSuccess(){
        return this.setSuccess(null);
    }

    public boolean isSuccess(){
        if(CommonResultCodeEnum.SUCCESS.getCode().equals(code)){
            return true;
        }else{
            return false;
        }
    }
}
