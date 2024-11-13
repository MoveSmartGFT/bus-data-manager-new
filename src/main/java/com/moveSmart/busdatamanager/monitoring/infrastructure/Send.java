package com.moveSmart.busdatamanager.monitoring.infrastructure;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Send {

    private static final String QUEUE_NAME = "hello";

    public static void main(String[] argv) throws Exception {

        String uri = "amqps://niduuxqx:64q4XK7jFyAF_cigL6W2SXwevqZsx0hJ@kangaroo.rmq.cloudamqp.com/niduuxqx";

        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri(uri);
        factory.setConnectionTimeout(30000);

        try (Connection connection = factory.newConnection();
                Channel channel = connection.createChannel()) {

            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            String message = "jaja lol xd";
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println(" [x] Sent '" + message + "'");
        }
    }
}
