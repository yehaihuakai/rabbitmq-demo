package com.dion.rabbitmq.publishsubscribe;

import com.dion.rabbitmq.util.ConnectionUtil;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 发布订阅模式：消费者接收消息
 * 此消费者与Consumer2监听的同一个队列，二者之间属于work模式
 */
public class Consumer3 {

    public static void main(String[] args) throws IOException, TimeoutException {
        // 1.创建连接
        Connection connection = ConnectionUtil.getConnection();

        // 2.创建频道
        Channel channel = connection.createChannel();

        // 3.声明交换机
        channel.exchangeDeclare(Producer.FANOUT_EXCHANGE, BuiltinExchangeType.FANOUT);

        // 4.声明队列
        /**
         * 参数1：队列名称
         * 参数2：是否定义持久化队列（消息会持久化保存在服务器上）
         * 参数3：是否独占本连接
         * 参数4：是否在不适用的时候队列自动删除
         * 参数5：其他参数
         */
        channel.queueDeclare(Producer.FANOUT_QUEUE_1, true, false, false, null);

        // 5.绑定队列到交换机
        channel.queueBind(Producer.FANOUT_QUEUE_1, Producer.FANOUT_EXCHANGE, "");

        // 6.创建消费者
        DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                //路由key
                System.out.println("路由key为: " + envelope.getRoutingKey());
                //交换机
                System.out.println("交换机为: " + envelope.getExchange());
                //消息id
                System.out.println("消息id: " + envelope.getDeliveryTag());
                //接收到的消息
                System.out.println("消费者3---接收到的消息: " + new String(body, "utf-8"));
            }
        };

        // 6.队列监听
        /**
         * 参数1：队列名
         * 参数2：是否要自动确认；设置为true表示消息接收到自动向MQ回复接收到了，MQ则会将消息从队列中删除；如果设置为false，则需要手动确认。
         * 参数3：消息消费者
         */
        channel.basicConsume(Producer.FANOUT_QUEUE_1, true, defaultConsumer);
    }
}
