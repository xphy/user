package com.user.rabbit.Routing;

import com.rabbitmq.client.*;
import com.user.rabbit.until.RabbitConnectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author ：mmzs
 * @date ：Created in 2020/3/8 20:36
 * @description：路由模式的生产者
 * @modified By：
 * @version: 1.0$
 */
public class RoutingConsumerA {
    private static Logger logger = LoggerFactory.getLogger(RoutingConsumerA.class);
    private static final String QUEUE_NAME = "routing_direct_queue_name";
    private static final String EXCHANGE_NAME = "publish_subscribe_exchange_direct";
    //binding key
    private static final String EXCHANGE_ROUTE_KEY = "error";

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
            // 同一时刻服务器只会发一条消息给消费者
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
