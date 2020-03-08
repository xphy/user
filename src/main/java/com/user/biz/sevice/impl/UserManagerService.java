package com.user.biz.sevice.impl;

import com.user.biz.bean.User;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ：mmzs
 * @date ：Created in 2020/1/3 18:43
 * @description：用户业务层接口
 * @modified By：
 * @version: 1$
 */
public interface UserManagerService {
    String BEAN_NAME = "com.user.biz.sevice.impl.UserLoginServiceImpl";
     boolean userRegister();
     List<User> userLogin();
     int deleteByPrimaryKey(Integer id);
     //筛选出所有的用户
     List<User> selectall();

}
