package com.user.biz.sevice.impl;

import com.user.biz.bean.User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
     //插入一个用户
     Integer insertOneUser(User user);
     /**
      * @description: 批量插入用户
      * @param userList
      * @return: java.lang.Integer
      * @author: Andy
      * @time: 2020/4/17 10:21
      */
     Integer insertAllUser(List<User> userList);
     Integer importDataByThread(MultipartFile file);

}
