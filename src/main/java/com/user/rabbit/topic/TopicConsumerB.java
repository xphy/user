package com.user.rabbit.topic;

import com.rabbitmq.client.*;
import com.user.rabbit.until.RabbitConnectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author ：mmzs
 * @date ：Created in 2020/3/8 20:54
 * @description：topic模式下的消费者
 * @modified By：
 * @version: 1.0$
 */
public class TopicConsumerB {
    private static Logger logger = LoggerFactory.getLogger(TopicConsumerB.class);
    private static final String QUEUE_NAME = "topic_queue_name2";
    private static final String EXCHANGE_NAME = "exchange_topic";
    //binding key
    private static final String EXCHANGE_ROUTE_KEY = "news.#";

    public static void main(String[] args) {
        try {
            //获取MQ连接对象
            Connection connection = RabbitConnectionUtil.getConnection();
            //创建消息通道对象
            final Channel channel = connection.createChannel();
            //创建队列
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            //将队列绑定到交换机上,并且指定routing_key
            channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, EXCHANGE_ROUTE_KEY);

            channel.basicQos(1);

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

            //使用公平分发必须关闭自动应答
            boolean autoAck = false;
            //监听消息队列
            channel.basicConsume(QUEUE_NAME, autoAck, consumer);
        } catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
