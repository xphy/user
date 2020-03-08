package com.user.rabbit.topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.user.rabbit.until.RabbitConnectionUtil;

import java.io.IOException;

/**
 * @author ：mmzs
 * @date ：Created in 2020/3/8 20:45
 * @description：topic模式的生产者
 * @modified By：
 * @version: 1.0$
 *
 *   说明：
 *   #: 代表一个或者多个
 *   *: 代表一个
 *
 *   举例：
 *   比如发送消息的时候指定了routing key为news.insert,
 *   如果消费者指定binding key 为news.* 或者news.#都能接收到该消息;
 *
 */
public class TopicProvide {
    private static final String EXCHANGE_NAME = "exchange_topic";
    //交换机类型：topic 类似正则匹配模式
    private static final String EXCHANGE_TYPE = "topic";
    //指定routing key
    private static final String EXCHANGE_ROUTE_KEY = "news.insert";

    public static void main(String[] args) {
        //获取MQ连接
        Connection connection = null;
        //从连接中获取Channel通道对象
        Channel channel = null;
        try {
            connection = RabbitConnectionUtil.getConnection();
            channel = connection.createChannel();
            //创建交换机对象
            channel.exchangeDeclare(EXCHANGE_NAME, EXCHANGE_TYPE);
            //发送消息到交换机exchange上
            String msg = "hello world!!!";
            channel.basicPublish(EXCHANGE_NAME, EXCHANGE_ROUTE_KEY, null, msg.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }finally {
            if (null != channel) {
                try {
                    channel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != connection) {
                try {
                    connection.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
