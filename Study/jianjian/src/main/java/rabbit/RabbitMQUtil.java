package rabbit;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;

/**
 * 获得rabbitConnection
 * Created by gao on 2016/11/25.
 */
public class RabbitMQUtil {
    public static Connection getConnection() throws IOException {
        ConnectionFactory factory=new ConnectionFactory();
        factory.setHost("192.168.43.138");
        factory.setPort(5672);
        factory.setVirtualHost("/test");
        factory.setUsername("test");
        factory.setPassword("test");
        return factory.newConnection();
    }
}
