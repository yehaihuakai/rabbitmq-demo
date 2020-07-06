package com.dion.rabbitmq.topic;

import com.dion.rabbitmq.util.ConnectionUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 通配符模式：发送消息
 */
public class Producer {
    /**
     * 交换机名称
     */
    static final String TOPIC_EXCHANGE = "topic_exchange";

    /**
     * 队列名称
     */
    static final String TOPIC_QUEUE_1 = "topic_queue_1";
    static final String TOPIC_QUEUE_2 = "topic_queue_2";

    public static void main(String[] args) throws IOException, TimeoutException {
        //1.创建连接
        Connection connection = ConnectionUtil.getConnection();

        //2.创建频道
        Channel channel = connection.createChannel();

        //3.声明交换机
        /**
         * 参数1：交换机名称
         * 参数2：交换机类型（fanout、direct、topic）
         */
        channel.exchangeDeclare(TOPIC_EXCHANGE, BuiltinExchangeType.TOPIC);

        /*//4.声明队列
         *//**
         * 参数1：队列名称
         * 参数2：是否定义持久化队列（消息会持久化保存在服务器上）
         * 参数3：是否独占本连接
         * 参数4：是否在不适用的时候队列自动删除
         * 参数5：其他参数
         *//*
        channel.queueDeclare(TOPIC_QUEUE_1, true, false, false, null);
        channel.queueDeclare(TOPIC_QUEUE_2, true, false, false, null);

        //5.绑定队列到交换机
        *//**
         * 参数1：队列名称
         * 参数2：交换机名称
         * 参数3：路由key
         *//*
        channel.queueBind(TOPIC_QUEUE_1, TOPIC_EXCHANGE, "item.insert");
        channel.queueBind(TOPIC_QUEUE_1, TOPIC_EXCHANGE, "item.update");
*/
        //6.发送消息
        for (int i = 1; i < 16; i++) {
            String routingKey = "";
            if (i <= 5) {
                routingKey = "item.insert";
            } else if (i > 10) {
                routingKey = "item.delete";
            } else {
                routingKey = "item.update";
            }

            String message = "hello dion , 通配符模式 --- " + i + "routingKey为" + routingKey;
            /**
             * 参数1：交换机名称；如果没有则指定空字符串（表示使用默认的交换机）
             * 参数2：路由key，简单模式中可以使用队列名称
             * 参数3：消息其他属性
             * 参数4：消息内容
             */
            channel.basicPublish(TOPIC_EXCHANGE, routingKey, null, message.getBytes());
            System.out.println("已发送消息：" + message);
        }

        // 6.关闭资源
        channel.close();
        connection.close();
    }
}
