package test.rabbit.routing;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import test.rabbit.utils.ConnectionUtils;

import java.io.IOException;

/**
 * 路由模式
 * Created by tarena on 2016/11/14.
 */
public class RoutingPublish {
    private static final String EXCHANGE_NAME="test_exchange";

    public static void main(String[] args) throws IOException {
        Connection connection= ConnectionUtils.getConnection();
        Channel channel=connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME,"direct");
        String message="direct=====";
        String routingKey="cn_gao";
        channel.basicPublish(EXCHANGE_NAME,routingKey,null,message.getBytes());
        channel.close();
        connection.close();
    }
}
