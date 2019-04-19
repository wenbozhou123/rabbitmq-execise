package com.bowen.mq.rabbitmq.simple;

import com.bowen.mq.rabbitmq.utils.RabbitmqHelper;
import com.rabbitmq.client.Channel;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @AUTHOR zhoubo
 * @CREATE: 2019-04-18 5:55 PM
 */
public class Send {
    private final static String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {

        Channel channel = RabbitmqHelper.createChannel();

        /** 为了发送消息，你必须要声明一个消息队列，然后向该消息队列里面推送消息*/
        channel.queueDeclare(QUEUE_NAME,false, false, false, null);
        String message = "hello World";
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
        System.out.println("[x] Sent ->" + message);

    }
}
