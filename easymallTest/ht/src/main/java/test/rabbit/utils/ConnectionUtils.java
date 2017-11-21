package test.rabbit.utils;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;

/**
 * Created by tarena on 2016/11/14.
 */
public class ConnectionUtils {
    public static Connection getConnection() throws IOException {
        ConnectionFactory factory=new ConnectionFactory();
        factory.setHost("172.16.8.69");
        factory.setPort(5672);
        factory.setVirtualHost("/jt");
        factory.setUsername("test");
        factory.setPassword("test");
        return factory.newConnection();
    }

}
