package test.rabbit.routing;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.QueueingConsumer;
import test.rabbit.utils.ConnectionUtils;

import java.io.IOException;

/**
 * routing模式消费之1
 * Created by tarena on 2016/11/14.
 */
public class RoutingConsumer2 {
    private static final String EXCHANGE_NAME="test_exchange";
    private static final String QUEUE_NAME="test";

    public static void main(String[] args) throws IOException, InterruptedException {
        //获取连接及通道
        Connection connection= ConnectionUtils.getConnection();
        Channel channel=connection.createChannel();
        //声明队列
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        //绑定到队列交换机
        String routingKey="routing2";
        channel.exchangeBind(QUEUE_NAME,EXCHANGE_NAME,routingKey);
        //设置每次服务器端向主机端发送一个消息
        channel.basicQos(1);
        //监听队列
        QueueingConsumer consumer=new QueueingConsumer(channel);
        channel.basicConsume(QUEUE_NAME,false,consumer);
        //获取消息
        while (true){
            QueueingConsumer.Delivery delivery=consumer.nextDelivery();
            System.out.println("routing2"+delivery.getBody());
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
        }
    }
}
