package com.greater.eastmoney.service;

import com.greater.eastmoney.common.UrlContant;
import com.greater.eastmoney.httpclient.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Author: yejj
 * @Date: 2021/2/6 14:10
 * @Description:
 **/
@Service
@Slf4j
public class FavorQueryService {

    /**
     * 查询自选列表
     * @return
     */
    public String favorNameList(){
        String result = HttpClientUtil.get(UrlContant.favorList);
        return result;
    }

}
