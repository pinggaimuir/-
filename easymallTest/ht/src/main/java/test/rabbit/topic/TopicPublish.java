package test.rabbit.topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import test.rabbit.utils.ConnectionUtils;

import java.io.IOException;

/**
 *主题模式
 * Created by tarena on 2016/11/14.
 */
public class TopicPublish {
    private static final String EXCHANGE_NAME="test_exchange";

    public static void main(String[] args) throws IOException {
        Connection connection= ConnectionUtils.getConnection();
        Channel channel=connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME,"direct");
        channel.basicPublish(EXCHANGE_NAME,"cn_gao",null,"hello topic".getBytes());
        channel.close();
        connection.close();
    }
}
