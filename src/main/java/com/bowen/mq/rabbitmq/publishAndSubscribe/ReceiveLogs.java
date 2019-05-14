package com.bowen.mq.rabbitmq.publishAndSubscribe;

import com.bowen.mq.rabbitmq.utils.RabbitmqHelper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @AUTHOR zhoubo
 * @CREATE: 2019-05-14 11:33 AM
 */
public class ReceiveLogs {

    private static final String EXCHANGE_NAME = "ex_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitmqHelper.createChannel();

        /** 声明消息路由的名称和类型*/
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

        /** 声明一个随机消息队列*/
        String queueName = channel.queueDeclare().getQueue();

        /** 绑定消息队列和消息路由*/
        channel.queueBind(queueName, EXCHANGE_NAME, "");
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, deliver) -> {
            String message = new String(deliver.getBody(),"utf-8");
            System.out.println(" [x] Received -->" + message);
            channel.basicAck(deliver.getEnvelope().getDeliveryTag(), false);
        };

        channel.basicConsume(queueName, false, deliverCallback, (consumerTag)->{});

    }
}
