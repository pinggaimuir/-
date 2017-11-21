package test.rabbit.topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.QueueingConsumer;
import test.rabbit.utils.ConnectionUtils;

import java.io.IOException;

/**
 * 主题模式消费者
 * Created by tarena on 2016/11/14.
 */
public class TopicConsumer {
    private static final String EXCHANGE_NAME="test_exchange";
    private static final String QUEUE_NAME="test";

    public static void main(String[] args) throws IOException, InterruptedException {
        Connection connection= ConnectionUtils.getConnection();
        Channel channel=connection.createChannel();
        //声明队列
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        //绑定交换机
        String routingKey="cn*";
        channel.exchangeBind(QUEUE_NAME,EXCHANGE_NAME,routingKey);
        //创建一个消费者
        QueueingConsumer consumer=new QueueingConsumer(channel);
        //监听队列
        channel.basicConsume(QUEUE_NAME,false,consumer);
        //设置每次服务器端只向客户端发送一条消息
        channel.basicQos(1);
        //获取消息
        while(true){
            QueueingConsumer.Delivery delivery=consumer.nextDelivery();
            System.out.println("cn_gao1"+delivery.getBody());
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
        }
    }
}
