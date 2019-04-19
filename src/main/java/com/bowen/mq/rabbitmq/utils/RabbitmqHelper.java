package com.bowen.mq.rabbitmq.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @AUTHOR zhoubo
 * @CREATE: 2019-04-19 2:58 PM
 */
public class RabbitmqHelper {

    private static ConnectionFactory factory;
    private static Connection connection;
    private static Channel channel;

    public static Channel createChannel() throws IOException, TimeoutException {
        factory = factory == null ? new ConnectionFactory() : factory;
        factory.setHost(Contants.hostname);
        factory.setPort(5672);
        factory.setUsername("admin");
        factory.setPassword("admin123");
        factory.setVirtualHost("/rabbitmq_bowen");
        connection = connection == null ? factory.newConnection() : connection;
        channel = connection.createChannel();
        return channel;
    }

    public static void closeAll() throws IOException, TimeoutException {
        if( channel != null) channel.close();
        if( connection != null) connection.close();
    }


}
