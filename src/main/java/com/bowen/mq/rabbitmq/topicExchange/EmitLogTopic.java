package com.bowen.mq.rabbitmq.topicExchange;

import com.bowen.mq.rabbitmq.utils.Contants;
import com.bowen.mq.rabbitmq.utils.RabbitmqHelper;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @AUTHOR zhoubo
 * @CREATE: 2019-05-14 4:19 PM
 */
public class EmitLogTopic {
    private static final String EXCHANGE_NAME = "ex_topic_logs";

    public static void main(String[]  args) throws IOException, TimeoutException {

        Channel channel = RabbitmqHelper.createChannel();

        /** 指定一个topic类型的exchange*/
        channel.exchangeDeclare(EXCHANGE_NAME, Contants.EXCHANEG_TYPES.TOPIC);

        /** 这里拿到routing key*/
        String[] routingKeys = {"kern.critical"," A critical kernel error"};

        String message = "emit log topic";

        for (String routingKey : routingKeys){
            channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes());
            System.out.println(" [x] Sent -->" + routingKey + ":" + message );
        }

    }
}
