package com.movesmart.busdatamanager.vehicle;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Generated
@AllArgsConstructor
public class Send {

    private static final String QUEUE_NAME = "hello";
    private final ConnectionFactory factory;

    public Send() throws Exception {
        String uri = "";
        factory = new ConnectionFactory();
        factory.setUri(uri);
        factory.setConnectionTimeout(30000);
    }

    public void sendMessage(String message) {
        try (Connection connection = factory.newConnection();
                Channel channel = connection.createChannel()) {

            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            log.info("Message sent: {}", message);
        } catch (Exception e) {
            log.error("Failed to send message: {}", message, e);
        }
    }
}
