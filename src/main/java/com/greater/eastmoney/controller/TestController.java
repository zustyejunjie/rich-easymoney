package com.greater.eastmoney.controller;

import com.alibaba.fastjson.JSON;
import com.greater.eastmoney.request.TestReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
public class TestController {

    /**
     * get请求  前端可以通过http://127.0.0.1:8080/testapi?s="1" 访问
     * @param s
     * @return
     */
    @RequestMapping(value = "/testapi")
    public String testapi(String s){
        log.info("testapi:{}",s);
        return "test success";
    }

    /**
     * 参数必须以json格式传入  否则在解析RequestBody时，会抛出异常
     * 接收json 参数
     * @param testReq
     * @return
     */
    @RequestMapping(value = "/testJSON")
    @ResponseBody
    public String testJSON(@RequestBody  TestReq testReq){
        log.info("testJSON:{}",JSON.toJSONString(testReq));
        return JSON.toJSONString(testReq);
    }

    /**
     * 参数以字符串传入即可
     * 接收非json参数
     * @param testReq
     * @return
     */
    @RequestMapping(value = "/testPost")
    @ResponseBody
    public String testPost(TestReq testReq){
        log.info("testPost:{}",JSON.toJSONString(testReq));
        return JSON.toJSONString(testReq);
    }

    /**
     * 调用方式同testPost   调用时参数都是 a="" ，b=""的方式传入
     * @param request
     * @return
     */
    @RequestMapping(value = "testHttpServletRequest")
    public String testHttpServletRequest(HttpServletRequest request){
        String id = request.getParameter("id");
        String name = request.getParameter("name");
        log.info("id:{},name:{}",id,name);
        return JSON.toJSONString(id);
    }
}

