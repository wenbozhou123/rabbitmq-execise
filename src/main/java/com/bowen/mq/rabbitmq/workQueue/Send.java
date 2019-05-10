package com.bowen.mq.rabbitmq.workQueue;

import com.bowen.mq.rabbitmq.utils.RabbitmqHelper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @AUTHOR zhoubo
 * @CREATE: 2019-04-18 5:55 PM
 */
public class Send {
    private final static String QUEUE_NAME = "task_queue";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {

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
        String messages = "message";
        for(int i = 0;i<10;i++){
            channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, (i + messages).getBytes());
            System.out.println("[x] Sent ->" + (i + messages));
            //Thread.sleep(1000);
            messages = joinString(messages, ".");
        }
        System.out.println("[x] Sent -> Done");
    }

    private static String joinString(String str, String delimiter){
        StringBuilder word = new StringBuilder(str);
        return word.append(delimiter).toString();
    }
}
