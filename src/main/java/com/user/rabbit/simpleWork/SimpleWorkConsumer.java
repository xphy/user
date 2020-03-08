package com.user.rabbit.simpleWork;

import com.rabbitmq.client.*;
import com.user.rabbit.until.RabbitConnectionUtil;

import java.io.IOException;

/**
 * @author ：mmzs
 * @date ：Created in 2020/3/8 18:16
 * @description：创建消费者
 * @modified By：
 * @version: 1.0$
 */
public class SimpleWorkConsumer {
    //定义队列名称
    private final static String QUEUE_NAME = "q_test_01";

    public static void main(String[] argv) throws Exception {

        // 获取到连接以及mq通道
        Connection connection = RabbitConnectionUtil.getConnection();
        // 从连接中创建通道
        Channel channel = connection.createChannel();
        // 声明队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        //创建消费者对象
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.err.println("-----------consume message----------");
                System.err.println("consumerTag: " + consumerTag);
                System.err.println("envelope: " + envelope);
                System.err.println("properties: " + properties);
                //消息消费者获取消息
                System.err.println("body: " + new String(body));
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    //消费完一条消息需要自动发送确认消息给MQ
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            }
        };



        // 监听队列
        channel.basicConsume(QUEUE_NAME, true, consumer);


    }
}
