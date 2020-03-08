package com.user.biz.mapper;

import com.user.base.mapper.BaseMapper;
import com.user.biz.bean.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author ：mmzs
 * @date ：Created in 2020/1/3 19:27
 * @description：用户管理dao层
 * @modified By：
 * @version: 1$
 */
@Mapper
@Repository(UserManagerMapper.BEAN_NAME)
public interface UserManagerMapper extends BaseMapper<User> {
    String BEAN_NAME = "com.user.biz.mapper.UserManagerMapper";


    //用户登陆
    List<User> login();

}
