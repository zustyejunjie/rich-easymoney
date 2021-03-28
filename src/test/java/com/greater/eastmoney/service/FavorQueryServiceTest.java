package com.greater.eastmoney.service;

import com.greater.eastmoney.TestSupport;
import com.greater.eastmoney.service.favor.FavorQueryService;
import com.greater.eastmoney.service.favor.favorRequest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author: yejj
 * @Date: 2021/2/6 16:27
 * @Description:
 **/
public class FavorQueryServiceTest extends TestSupport {

    @Autowired
    private FavorQueryService favorQueryService;
    @Autowired
    private ValueHisService valueHisService;

    @Test
    public void favor(){
        favorRequest request = new favorRequest();
        request.setUserId("1");
        request.setUserName("2");
        String result = favorQueryService.favorNameList(request);

        System.out.println(result);
    }




    @Test
    public void valueHis5(){
        valueHisService.valueHis5("");
    }
}
