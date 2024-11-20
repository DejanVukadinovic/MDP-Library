package org.example.MQ;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.GetResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;


public class RabbitMQHandler {

    private static RabbitMQHandler handler = null;
    private ConnectionFactory factory;
    private RabbitMQHandler(){
        factory = new ConnectionFactory();
        factory.setHost("localhost");
    }
    public static RabbitMQHandler getHandler(){
        if(handler == null){
            handler = new RabbitMQHandler();
        }
        return handler;
    }
    public String getMessage(String queueName){
        try(Connection connection = factory.newConnection(); Channel channel = connection.createChannel()){
            channel.queueDeclare("suppliergui", false, false, false, null);
            GetResponse response = channel.basicGet("suppliergui", true);
            if(response!=null){
                return new String(response.getBody(), StandardCharsets.UTF_8);
            }
        }catch (IOException | TimeoutException e) {
        e.printStackTrace();
        }
        return null;
    }

}
