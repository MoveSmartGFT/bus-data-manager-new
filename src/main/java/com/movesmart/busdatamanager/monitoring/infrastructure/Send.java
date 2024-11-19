package com.movessmart.busdatamanager.monitoring.infrastructure;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.Generated;

@Generated
public class Send {

    private static final String QUEUE_NAME = "hello";

    public static void main(String[] argv) throws Exception {

        String uri = "";

        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri(uri);
        factory.setConnectionTimeout(30000);

        try (Connection connection = factory.newConnection();
                Channel channel = connection.createChannel()) {

            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            String message = "Hello world";
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println(" [x] Sent '" + message + "'");
        }
    }
}
