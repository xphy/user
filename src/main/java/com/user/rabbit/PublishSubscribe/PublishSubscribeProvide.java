package com.user.rabbit.PublishSubscribe;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.user.rabbit.until.RabbitConnectionUtil;

import java.io.IOException;

/**
 * @author ：mmzs
 * @date ：Created in 2020/3/8 19:54
 * @description：发布订阅模式生产者
 * @modified By：
 * @version: 1.0$
 */
public class PublishSubscribeProvide {
    private static final String PUBLISH_SUBSCRIBE_EXCHANGE_NAME = "publish_subscribe_exchange_fanout";
    //类型：分发
    private static final String PUBLISH_SUBSCRIBE_EXCHANGE_TYPE = "fanout";

    public static void main(String[] args) {
        //获取MQ连接
        Connection connection = null;
        //从连接中获取Channel通道对象
        Channel channel = null;
        try {
            //获取MQ连接
            connection = RabbitConnectionUtil.getConnection();
            channel = connection.createChannel();
            //创建交换机对象,并改变类型为分发
            channel.exchangeDeclare(PUBLISH_SUBSCRIBE_EXCHANGE_NAME, PUBLISH_SUBSCRIBE_EXCHANGE_TYPE);
            //发送消息到交换机exchange上
            String msg = "hello world!!!";
            channel.basicPublish(PUBLISH_SUBSCRIBE_EXCHANGE_NAME, "", null, msg.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
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
