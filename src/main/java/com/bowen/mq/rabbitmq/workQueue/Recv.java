package com.bowen.mq.rabbitmq.workQueue;

import com.bowen.mq.rabbitmq.utils.RabbitmqHelper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @AUTHOR zhoubo
 * @CREATE: 2019-04-18 6:05 PM
 */
public class Recv {
    private final static String QUEUE_NAME = "task_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        /**这里怎么打开连接和信道，以及声明用于接收消息的队列，这些步骤与发送端基本上是一样的*/
        Channel channel = RabbitmqHelper.createChannel();

        /**确保队列存在*/
        channel.queueDeclare(QUEUE_NAME,true, false, false, null);
        System.out.println(" [*] Waiting for message. to exit press CTRL+C");


        DeliverCallback deliverCallback = (consumerTag, deliver) ->{
            String message = new String(deliver.getBody(), "utf-8");
            System.out.println(" [x] 1111111111Received-> " + message);
            channel.basicAck(deliver.getEnvelope().getDeliveryTag(), false);
        };
        DeliverCallback deliverCallback2 = (consumerTag, deliver) ->{
            String message = new String(deliver.getBody(), "utf-8");
            System.out.println(" [x] 22222222222Received-> " + message);
            channel.basicAck(deliver.getEnvelope().getDeliveryTag(), false);
        };

        channel.basicQos(0,2, false);
        channel.basicConsume(QUEUE_NAME, false, deliverCallback, consumerTag -> {});
        channel.basicConsume(QUEUE_NAME, false, deliverCallback2, consumerTag -> {});
    }

}
