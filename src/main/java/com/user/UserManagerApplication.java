package com.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * @author ：mmzs
 * @date ：Created in 2020/1/3 18:05
 * @description：用户管理系统启动类
 * Provider项目 生产者
 * @modified By：
 * @version: 1$
 */
@SpringBootApplication
@EnableCaching
public class UserManagerApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserManagerApplication.class);
    }
}
