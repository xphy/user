package com.user.biz.controller;

import com.user.base.controller.BaseController;
import com.user.biz.bean.User;
import com.user.biz.sevice.impl.UserManagerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author ：mmzs
 * @date ：Created in 2020/1/3 18:43
 * @description：用户控制类
 * @modified By：
 * @version: 1$
 */
@RestController(UserManagerController.BEAN_NAME)
@RequestMapping("/user")
@Slf4j
public class UserManagerController extends BaseController {
    static final String BEAN_NAME = "com.user.biz.controller.UserManagerController";

    private UserManagerService userManagerService;

    public UserManagerController(UserManagerService userManagerService) {
        this.userManagerService = userManagerService;
    }

//    @Value("${server.port}")
//    public String  foo;
    @RequestMapping("/login")
    public List<User> login(){
        return userManagerService.userLogin();
    }
    @RequestMapping("/register")
    public List<User> register(){
        return userManagerService.selectall();
    }



}
