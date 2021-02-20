package com.greater.eastmoney.http;

import com.greater.eastmoney.TestSupport;
import com.greater.eastmoney.httpclient.HttpClientUtil;
import com.greater.eastmoney.request.TestReq;
import org.junit.Assert;
import org.junit.Test;

/**
 * 测试post方法
 * 服务器为本地服务
 * @Author: yejj
 * @Date: 2021/2/6 15:00
 * @Description:
 **/
public class HttpClientTest extends TestSupport {

    @Test
    public void testapi(){
        String result = HttpClientUtil.post("http://127.0.0.1:8080/testapi","");
        Assert.assertEquals("test success",result);
    }

    @Test
    public void testPost(){
        TestReq testReq = new TestReq();
        testReq.setId("1");
        testReq.setName("2");
        TestReq result = HttpClientUtil.post("http://127.0.0.1:8080/testPost",testReq,TestReq.class);
        Assert.assertEquals(result.getId(),testReq.getId());
    }

    @Test
    public void testJSON(){
        TestReq testReq = new TestReq();
        testReq.setId("1");
        testReq.setName("2");
        TestReq result = HttpClientUtil.postJSON("http://127.0.0.1:8080/testJSON",testReq,TestReq.class);
        Assert.assertEquals(result.getId(),testReq.getId());
    }


    @Test
    public void testHttpServletRequest(){
        TestReq testReq = new TestReq();
        testReq.setId("1");
        testReq.setName("2");
        TestReq result = HttpClientUtil.post("http://127.0.0.1:8080/testHttpServletRequest",testReq,TestReq.class);
        Assert.assertEquals(result.getId(),testReq.getId());
    }
}
