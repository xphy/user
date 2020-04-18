package com.user.biz.thread;

import com.user.base.util.SpringUtil;
import com.user.biz.bean.User;
import com.user.biz.sevice.impl.UserManagerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

/**
 * @author ：mmzs
 * @date ：Created in 2020/4/16 15:52
 * @description：
 * @modified By：
 * @version: $
 */
@Slf4j
public class UserInfoThread implements Callable {
    //导入的数据
    private List<Map<String, Object>> list;

    private CountDownLatch begin;

    private CountDownLatch end;

//    private Set<String> names;

    private UserManagerService userManagerService;


    private Semaphore semaphore;

    public UserInfoThread(List<Map<String, Object>> list, CountDownLatch begin, CountDownLatch end,
                           Semaphore semaphore) {
        this.list = list;
        this.begin = begin;
        this.end = end;
        this.userManagerService = (UserManagerService) SpringUtil.getBean("UserManagerService");;
        this.semaphore = semaphore;
    }


    @Override
    public Object call() throws Exception {
        log.info("线程ID：<{}>开始运行,begin:{},end:{}",Thread.currentThread().getId(),begin,end);
        semaphore.acquire();
        log.info("消耗了一个信号量，剩余信号量为:{}",semaphore.availablePermits());
        int row = 0;
        try {
            List<User> userList = new ArrayList<>();
            if(CollectionUtils.isEmpty(userList)){
                A:for (int i = 0; i < list.size(); i++) {
                    Map<String,Object> map = list.get(i);
                    String id = map.get("id").toString().trim();
                    String uname = map.get("uname").toString().trim();
                    String lname = map.get("lname").toString().trim();
                    String pwd = map.get("pwd").toString().trim();
                    String state = map.get("state").toString().trim();
                    //导入的人员信息字段有一个是为空的，就不持久化数据库。
                    if(StringUtils.isBlank(id)){
                        continue A;
                    }
                    if(StringUtils.isBlank(uname)){
                        continue A;
                    }
                    if(StringUtils.isBlank(lname)){
                        continue A;
                    }
                    if(StringUtils.isBlank(pwd)){
                        continue A;
                    }
                    if(StringUtils.isBlank(state)){
                        continue A;
                    }
                    User user = new User();
                    user.setUid(id);
                    user.setUserName(uname);
                    user.setLoginName(lname);
                    user.setPwd(pwd);
                    user.setState(Integer.parseInt(state));
                    userList.add(user);
                }
                if(CollectionUtils.isNotEmpty(userList)){
                    row = userManagerService.insertAllUser(userList);
                }
                //执行完让线程直接进入等待
                // begin.await();
            }
        }catch (Exception e){
            log.error("elPositionInfoServiceImpl的UserInfoThread方法error"+e.getMessage(),e);
        }finally {
            //这里要主要了，当一个线程执行完了计数要减一不要这个线程会被一直挂起，
            // end.countDown()，这个方法就是直接把计数器减一的
            end.countDown();
        }
        return row;
    }
}
