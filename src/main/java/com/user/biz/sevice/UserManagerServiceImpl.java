package com.user.biz.sevice;

import com.user.biz.bean.User;
import com.user.biz.mapper.UserManagerMapper;
import com.user.biz.sevice.impl.UserManagerService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ：mmzs
 * @date ：Created in 2020/1/3 19:06
 * @description：用户登陆业务层
 * @modified By：
 * @version: 1$
 */
@Service(UserManagerService.BEAN_NAME)
public class UserManagerServiceImpl implements UserManagerService {

    private UserManagerMapper userManagerMapper;

    public UserManagerServiceImpl(UserManagerMapper userManagerMapper){
        this.userManagerMapper = userManagerMapper;
    }
    @Override
    public boolean userRegister() {
        return false;
    }

    @Override
    public List<User> userLogin() {
        return userManagerMapper.login();
    }

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return userManagerMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<User> selectall() {
        return userManagerMapper.selectAll();
    }
}
