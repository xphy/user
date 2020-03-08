package com.user.rabbit.until;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @author ：mmzs
 * @date ：Created in 2020/3/8 17:39
 * @description：rabbit connnection 工具类
 * @modified By：
 * @version: 1$
 */
public class RabbitConnectionUtil {
    public static Connection getConnection() throws Exception {
        //定义连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //设置服务地址
        factory.setHost("localhost");
        //端口
        factory.setPort(5672);
        //设置vhost
        factory.setVirtualHost("/vhost");
        //设置账号信息，用户名、密码
        factory.setUsername("admin");
        factory.setPassword("admin");
        // 通过工程获取连接
        Connection connection = factory.newConnection();
        return connection;
    }
}
