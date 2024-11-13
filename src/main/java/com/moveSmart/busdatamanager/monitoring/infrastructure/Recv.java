package com.moveSmart.busDataManager.monitoring.infrastructure;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class Recv {

    private static final String QUEUE_NAME = "hello";

    public static void main(String[] argv) throws Exception {
        String uri = "amqps://niduuxqx:64q4XK7jFyAF_cigL6W2SXwevqZsx0hJ@kangaroo.rmq.cloudamqp.com/niduuxqx";

        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri(uri);
        factory.setConnectionTimeout(30000);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(
                    String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) {
                String message = new String(body);
                System.out.println(" [x] Received '" + message + "'");
            }
        };

        channel.basicConsume(QUEUE_NAME, true, consumer);
    }
}
