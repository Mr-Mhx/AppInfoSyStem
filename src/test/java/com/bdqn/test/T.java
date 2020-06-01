package com.bdqn.test;

import com.bdqn.service.DevUserService;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@ContextConfiguration("classpath:applictionContext.xml")
public class T {

    @Resource
    private DevUserService devUserService;

  /*  @Test
    public void login() {
        DevUser devUser = devUserService.login("test001", "e10adc3949ba59abbe56e057f20f883e");
        System.out.println("devUser = " + devUser);
    }*/
}


