package com.user.rabbit.simpleWork;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.user.rabbit.until.RabbitConnectionUtil;

/**
 * @author ：mmzs
 * @date ：Created in 2020/3/8 17:46
 * @description：生产者发送消息
 * @modified By：简单模式
 * @version: 1$
 */
public class SimpleWorkProvide {
    //定义队列的的名称
    private final static String QUEUE_NAME = "q_test_01";

    public static void main(String[] argv) throws Exception {
        // 获取到连接以及mq通道
        Connection connection = RabbitConnectionUtil.getConnection();
        // 从连接中创建通道
        Channel channel = connection.createChannel();

        // 声明（创建）队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        // 消息内容
        String message = "Hello World!";
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
        System.out.println(" [x] Sent '" + message + "'");
        //关闭通道和连接
        channel.close();
        connection.close();
    }
}
