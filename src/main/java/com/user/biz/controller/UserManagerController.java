package com.user.biz.controller;

import com.user.base.controller.BaseController;
import com.user.biz.bean.User;
import com.user.biz.sevice.impl.UserManagerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

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
//@MapperScan("com.user.biz.mapper.UserManagerMapper")
public class UserManagerController extends BaseController {
    static final String BEAN_NAME = "com.user.biz.controller.UserManagerController";

    private UserManagerService userManagerService;

    public UserManagerController(UserManagerService userManagerService) {
        this.userManagerService = userManagerService;
    }

    @RequestMapping("/login")
    public List<User> login(){
        return userManagerService.userLogin();
    }


    @RequestMapping("/register")
    public List<User> register(){
        return userManagerService.selectall();
    }


    //添加使用Redis
    @RequestMapping("/addUser")
    public void exportExcel(@RequestParam("user") User user) {
        User bean = new User().builder()
                .userName("皮")
                .pwd("hhh")
                .build();

        userManagerService.insertOneUser(bean);
    }
    //添加使用Redis
    @RequestMapping("/importUsers")
    public void importUsers(@RequestParam("user") User user) {
        User bean = new User().builder()
                .userName("皮")
                .pwd("hhh")
                .build();

        userManagerService.insertOneUser(bean);
    }
    /**
     * 多线程导入
     * @param file
     * @return
     */
    @PostMapping("/importManyThread")
    public Map<String, Object> importData(MultipartFile file){
        Map<String, Object> map = null;
        try {
            userManagerService.importDataByThread(file);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            map.put("code",501);
            map.put("msg","数据出错");
            return map;
        }
    }



}
