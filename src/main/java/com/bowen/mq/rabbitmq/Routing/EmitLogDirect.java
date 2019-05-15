package com.bowen.mq.rabbitmq.Routing;

import com.bowen.mq.rabbitmq.utils.Contants;
import com.bowen.mq.rabbitmq.utils.RabbitmqHelper;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @AUTHOR zhoubo
 * @CREATE: 2019-05-15 10:38 AM
 */
public class EmitLogDirect {

    private static final String EXCHANGE_NAME = "ex_direct_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitmqHelper.createChannel();

        /** 声明direct类型的exchange*/
        channel.exchangeDeclare(EXCHANGE_NAME, Contants.EXCHANEG_TYPES.DIRECT);

        /** routingKey*/
        String routingKey = "info";

        /** messages*/
        String messages = "emit log direct message";

        /** 指定routing key, 发送消息*/
        channel.basicPublish(EXCHANGE_NAME, routingKey, null, messages.getBytes());

        System.out.println(" [x] Sent -->" + routingKey + ":" + messages);
    }
}
