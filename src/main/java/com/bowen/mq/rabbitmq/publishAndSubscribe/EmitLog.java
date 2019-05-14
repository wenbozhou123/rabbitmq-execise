package com.bowen.mq.rabbitmq.publishAndSubscribe;

import com.bowen.mq.rabbitmq.utils.RabbitmqHelper;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @AUTHOR zhoubo
 * @CREATE: 2019-05-14 11:17 AM
 */
public class EmitLog {
    private static final String EXCHANGE_NAME = "ex_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitmqHelper.createChannel();

        /** 声明exchange名字以及类型*/
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

        String messages = "publish and subscribe message";

        /** 指定exchange的名字*/
        channel.basicPublish(EXCHANGE_NAME,"",null, messages.getBytes());

        System.out.println("[x] Sent -->" + messages);

        RabbitmqHelper.closeAll();
    }
}
