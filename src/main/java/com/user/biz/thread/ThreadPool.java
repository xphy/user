package com.user.biz.thread;

import com.user.biz.sevice.impl.UserManagerService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author ：mmzs
 * @date ：Created in 2020/4/17 14:05
 * @description：创建线程池
 * @modified By：
 * @version: 1$
 */
@Slf4j
public class ThreadPool {
    public static int createPoll(List<Map<String,Object>> list){
        //创建线程池
        Integer row = 1;
        Integer successCount = 0;
        // 一个线程处理50条数据
        int count = 50;
        // 数据集合大小
        int listSize = list.size();
        // 开启的线程数
        int runThreadSize = (listSize / count) + 1;
        // 存放每个线程的执行数据
        List<Map<String, Object>> newlist = null;
        //设置一个信号量为5的信号量，限制同时运行的线程数量最大为5
        Semaphore semaphore = new Semaphore(10);
        // 创建一个线程池，数量和开启线程的数量一样
        ExecutorService executor = Executors.newFixedThreadPool(runThreadSize);

        // 创建两个个计数器
        CountDownLatch begin = new CountDownLatch(1);
        CountDownLatch end = new CountDownLatch(runThreadSize);
        // 循环创建线程
        for (int i = 0; i < runThreadSize; i++) {
            if ((i + 1) == runThreadSize) {
                int startIndex;
                startIndex = (i * count);
                int endIndex = list.size();
                newlist = list.subList(startIndex, endIndex);
            } else {
                int startIndex = (i * count);
                int endIndex = (i + 1) * count;
                newlist = list.subList(startIndex, endIndex);
            }

            //线程类，处理数据持久化
            UserInfoThread userInfoThread = new UserInfoThread(newlist,begin,end,semaphore);
            //executor.execute(userInfoThread);
            Future<Integer> submit = executor.submit(userInfoThread);
            try {
                //提交成功的次数
                successCount += submit.get();
            } catch (InterruptedException | ExecutionException e1) {
                log.error("ElAgencyPositionUserServiceImpl的saveUserInfoList方法error"+e1.getMessage(),e1);
            }
        }
        try{
            begin.countDown();
            end.await();
            //执行完关闭线程池
            executor.shutdown();
        }catch (Exception e) {
            log.error("ElAgencyPositionUserServiceImpl的saveUserInfoList方法error"+e.getMessage(),e);
        }
        return successCount;
    }
}
