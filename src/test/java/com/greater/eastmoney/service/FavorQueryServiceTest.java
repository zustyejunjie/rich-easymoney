package com.greater.eastmoney.service;

import com.greater.eastmoney.TestSupport;
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
       String result = favorQueryService.favorNameList();

       System.out.println(result);
    }




    @Test
    public void valueHis5(){
        valueHisService.valueHis5("");
    }
}
