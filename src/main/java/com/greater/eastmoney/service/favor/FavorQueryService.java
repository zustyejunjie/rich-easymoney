package com.greater.eastmoney.service.favor;

import com.greater.eastmoney.aop.Dubbo;
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
    @Dubbo("查询自选列表")
    public String favorNameList(favorRequest request){
//        String result = HttpClientUtil.get(UrlContant.favorList);
        String result = "11111";
        return result;
    }

}
