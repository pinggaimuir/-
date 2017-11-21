package test.rabbit.simple;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.QueueingConsumer;
import test.rabbit.utils.ConnectionUtils;

import java.io.IOException;

/**
 * rabbitma简单模式，消费者
 * Created by tarena on 2016/11/14.
 */
public class Consume {
    private static final String QUEUE_NAME="jt_test_queue";
    public static void main(String[] args) throws IOException, InterruptedException {
        //创建连接
        Connection connection= ConnectionUtils.getConnection();
        //绑定通道
        Channel channel=connection.createChannel();
        //声明队列
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        //定义队列的消费值
        QueueingConsumer consumer=new QueueingConsumer(channel);
        //监听队列
        channel.basicConsume(QUEUE_NAME,true,consumer);
        while(true){
            QueueingConsumer.Delivery delivery=consumer.nextDelivery();
            String massage=new String(delivery.getBody());
            System.out.println("simple==="+massage);
        }
    }
}
