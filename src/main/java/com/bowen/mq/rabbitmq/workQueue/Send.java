package com.bowen.mq.rabbitmq.workQueue;

import com.bowen.mq.rabbitmq.utils.RabbitmqHelper;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @AUTHOR zhoubo
 * @CREATE: 2019-04-18 5:55 PM
 */
public class Send {
    private final static String QUEUE_NAME = "task_queue";

    public static void main(String[] args) throws IOException, TimeoutException {

        Channel channel = RabbitmqHelper.createChannel();

        /** 为了发送消息，你必须要声明一个消息队列，然后向该消息队列里面推送消息*/
        /**
         * channel.queueDeclare（
         *             String queue,
         *             boolean durable,
         *             boolean exclusive,
         *             boolean autoDelete
         *             Map<String, Object> arguments）各个参数含义
         * queue：队列名称
         * durable：是否持久化：
         * exclusive：是否排外的，是否独占Queue
         * autoDelete: 不使用时是否自动删除
         * arguments：其它参数*/
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        String[] messages = new String[]{"mess1", "mess2"};
        String message = getMessage(messages);
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
        System.out.println("[x] Sent ->" + message);

    }

    private static String getMessage(String[] strArr){
        if(strArr.length < 1)
            return "Hello World!";
        return joinString(strArr, " ");
    }

    private static String joinString(String[] strArr, String delimiter) {
        int length = strArr.length;
        if (length == 0) return "";
        StringBuilder words = new StringBuilder(strArr[0]);
        for (int i = 1; i < length; i++) {
            words.append(delimiter).append(strArr[i]);
        }
        return words.toString();
    }
}
