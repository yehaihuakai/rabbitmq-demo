package com.dion.rabbitmq.util;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ConnectionUtil {

    public static Connection getConnection() throws IOException, TimeoutException {
        // 1.创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        // 主机：默认localhost
        connectionFactory.setHost("localhost");
        // 连接端口；默认5672
        connectionFactory.setPort(5672);
        // 虚拟主机；默认是 /
        connectionFactory.setVirtualHost("/vh1");
        // 用户名；默认guest
        connectionFactory.setUsername("dion");
        // 密码；默认guest
        connectionFactory.setPassword("dion");

        // 2.创建连接
        return connectionFactory.newConnection();
    }
}
