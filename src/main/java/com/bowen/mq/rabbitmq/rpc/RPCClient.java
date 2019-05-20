package com.bowen.mq.rabbitmq.rpc;

import com.bowen.mq.rabbitmq.utils.RabbitmqHelper;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

/**
 * @AUTHOR zhoubo
 * @CREATE: 2019-05-15 5:40 PM
 */
public class RPCClient {

    private Channel channel;
    private String requestQueueName = "rpc_queue";
    private String replyQueueName;
    private String response;

    public RPCClient() throws IOException, TimeoutException {
        channel = RabbitmqHelper.createChannel();
    }

    public void call(String message) throws IOException {
        final String[] response = {"0"};
        String corrId = UUID.randomUUID().toString(); //拿到一个UUID
        /** 拿到一个匿名（并非真的匿名， 拿到了一个随机生成的队列名）的队列， 作为replyQueue*/
        replyQueueName = channel.queueDeclare().getQueue();

        /** 封装correlationId 和 replyQueue*/
        AMQP.BasicProperties props = new AMQP.BasicProperties
                .Builder()
                .correlationId(corrId)
                .replyTo(replyQueueName)
                .build();

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            if (delivery.getProperties().getCorrelationId().equals(corrId)) {
                response[0] = new String(delivery.getBody(), "utf-8");
                System.out.println("result is " + response[0]);
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            }
        };


        /** 推消息，并加上之前封装好的属性*/
        channel.basicPublish("", requestQueueName, props, message.getBytes());

        channel.basicConsume(replyQueueName, false, deliverCallback, (consumerTag) -> {
        });

    }

    public void close() throws IOException, TimeoutException {
        RabbitmqHelper.closeAll();
    }

    public static void main(String[] args) throws IOException, TimeoutException {
        RPCClient fibonacciRpc = null;
        String response;

        try {
            fibonacciRpc = new RPCClient();
            System.out.println(" [x] Requesting fib(4)");
            fibonacciRpc.call("5");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }finally {
           //fibonacciRpc.close();
        }
    }
}
