package com.greater.eastmoney;

import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 * 测试配置
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = RichEastmoneyApplication.class, webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Commit
@WebAppConfiguration
public class TestSupport {

}
