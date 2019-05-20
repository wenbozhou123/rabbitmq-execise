package com.bowen.mq.rabbitmq.rpc;

import com.bowen.mq.rabbitmq.utils.RabbitmqHelper;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @AUTHOR zhoubo
 * @CREATE: 2019-05-15 4:59 PM
 */

/**
 * 建立连接，信道，声明队列
 * 为了能把任务压力平均的分配到各个worker上，我们在方法channel.basicQos里设置prefetchCount的值。
 * 我们使用basicConsume来接收消息，并等待任务处理，然后发送响应。
 * */
public class RPCServer {

    private static final String RPC_QUEUE_NAME = "rpc_queue";

    private static int fib(int n){
        if (n==0) return 0;
        if (n==1) return 1;
        return fib(n-1) +fib(n-2);
    }
    public static void main(String[] args){
        try {
            Channel channel = RabbitmqHelper.createChannel();
            channel.queueDeclare(RPC_QUEUE_NAME, false, false, false, null);

            /** 每次只接受一条消息*/
            channel.basicQos(1);

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String response = null;
                AMQP.BasicProperties props = delivery.getProperties();
                AMQP.BasicProperties replyProps = new AMQP.BasicProperties
                        .Builder()
                        .correlationId(props.getCorrelationId())
                        .build();

                try {
                    String message = new String(delivery.getBody(), "utf-8");
                    int n = Integer.parseInt(message);

                    System.out.println(" [.] fib(" + n + ")");

                    response = "" + fib(n);
                }catch (Exception e){
                    System.out.println(" [.] " + e.toString());
                    response ="";
                }finally {
                    /** 拿到replyQueue, 并绑定为routing key, 发送消息*/
                    channel.basicPublish("", props.getReplyTo(), replyProps, response.getBytes("utf-8"));

                    /** 返回消息确认信息*/
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                }

            };
            channel.basicConsume(RPC_QUEUE_NAME, false, deliverCallback, (consumerTag)->{});
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
