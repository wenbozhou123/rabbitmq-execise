package com.bowen.mq.rabbitmq.Routing;

import com.bowen.mq.rabbitmq.utils.Contants;
import com.bowen.mq.rabbitmq.utils.RabbitmqHelper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @AUTHOR zhoubo
 * @CREATE: 2019-05-15 1:25 PM
 */
public class ReceiveLogsDirect {

    private static final String EXCHANGE_NAME = "ex_direct_logs";

    public static void main(String[]  args) throws IOException, TimeoutException {
        Channel channel = RabbitmqHelper.createChannel();

        /** 声明direct类型的exchange*/
        channel.exchangeDeclare(EXCHANGE_NAME, Contants.EXCHANEG_TYPES.DIRECT);
        String queueName = channel.queueDeclare().getQueue();

        String[] logLevels = {"info", "warning", "error"};

        for(String logLevel : logLevels){
            channel.queueBind(queueName, EXCHANGE_NAME, logLevel);
        }

        System.out.println("[*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, deliver)->{
            String message = new String(deliver.getBody());
            String routingKey = deliver.getEnvelope().getRoutingKey();
            System.out.println(" [x] Received -->" + routingKey + ":" + message);
            channel.basicAck(deliver.getEnvelope().getDeliveryTag(), false);
        };

        channel.basicConsume(queueName, false, deliverCallback, (consumerTag)->{});

    }
}
