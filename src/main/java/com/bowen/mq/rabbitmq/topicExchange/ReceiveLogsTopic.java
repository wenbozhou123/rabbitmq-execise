package com.bowen.mq.rabbitmq.topicExchange;

import com.bowen.mq.rabbitmq.utils.Contants;
import com.bowen.mq.rabbitmq.utils.RabbitmqHelper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @AUTHOR zhoubo
 * @CREATE: 2019-05-15 2:56 PM
 */
public class ReceiveLogsTopic {

    private static final String EXCHANGE_NAME = "ex_topic_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitmqHelper.createChannel();

        /** 指定一个topic类型的exchange*/
        channel.exchangeDeclare(EXCHANGE_NAME, Contants.EXCHANEG_TYPES.TOPIC);
        String queueName = channel.queueDeclare().getQueue();

        //String[] logLevels = {"#"};
        String[] logLevels2 = {"kern.*"};
        //String[] logLevels3 = {"kern.*", "*.critical"};

        for (String logLevel : logLevels2) {
            channel.queueBind(queueName, EXCHANGE_NAME, logLevel);
        }
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, delivery)->{
            String message = new String(delivery.getBody());
            String routingKey = delivery.getEnvelope().getRoutingKey();
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            System.out.println(" [x] Received -->" + routingKey + ":" + message );
        };

        channel.basicConsume(queueName, false, deliverCallback, (consumerTag)->{});
    }
}
