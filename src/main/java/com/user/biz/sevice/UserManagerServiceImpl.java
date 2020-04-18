package com.user.biz.sevice;

import com.user.base.util.StringUtil;
import com.user.biz.bean.User;
import com.user.biz.mapper.UserManagerMapper;
import com.user.biz.sevice.impl.UserManagerService;
import com.user.biz.thread.ThreadPool;
import com.user.biz.thread.UserInfoThread;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author ：mmzs
 * @date ：Created in 2020/1/3 19:06
 * @description：用户登陆业务层
 * @modified By：
 * @version: 1$
 */
@Slf4j
@Service(UserManagerService.BEAN_NAME)
public class UserManagerServiceImpl implements UserManagerService {

    private UserManagerMapper userManagerMapper;
//    private RedisUntil redisUntil;
//    public UserManagerServiceImpl(UserManagerMapper userManagerMapper,
//                                  RedisUntil redisUntil){
//        this.userManagerMapper = userManagerMapper;
//        this.redisUntil = redisUntil;
//    }
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
    @Override
    public Integer insertOneUser(User user){
        int insertNum = userManagerMapper.insert(user);
        if(insertNum > 0){
           // redisUntil.set("1", user);
        }
        return insertNum;
    }
    @Override
    public Integer insertAllUser(List<User> userList){
        int num = userManagerMapper.insertList(userList);
        return num;
    }
    @Override
    public Integer importDataByThread(MultipartFile file) {

        //解析excel
        List<Map<String, Object>> maps = Util.importExcel(file);
        //创建线程池
        return ThreadPool.createPoll(maps);
    }
}
