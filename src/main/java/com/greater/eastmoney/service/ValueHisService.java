package com.greater.eastmoney.service;

import com.greater.eastmoney.common.UrlContant;
import com.greater.eastmoney.httpclient.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Author: yejj
 * @Date: 2021/2/7 11:09
 * @Description:
 **/
@Service
@Slf4j
public class ValueHisService {
    /**
     * f43 收盘
     * 44 最高
     * 45 最低
     * 46 今开
     * 47 成交量
     * 48 成交额
     * 51 涨停价
     * 52 跌停价
     *
     * 57 代码
     * 58 名称
     * 168 换手率
     * 169  变动金额
     * 170  变动比例
     *
     *
     */
    /**
     *
     * 某一只票最近5日的价格
     * @param gCode
     * @return
     */
    public String valueHis5(String gCode){
        String result = HttpClientUtil.get(UrlContant.jfswDetail);
        return result;
    }


}
