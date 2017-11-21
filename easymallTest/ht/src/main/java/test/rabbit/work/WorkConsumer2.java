package test.rabbit.work;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.QueueingConsumer;
import test.rabbit.utils.ConnectionUtils;

import java.io.IOException;

/**
 * 消费者一
 * Created by tarena on 2016/11/14.
 */
public class WorkConsumer2 {
    private static final String QUEUE_NAME="jt_test_queue";
    public static void main(String[] args) throws IOException, InterruptedException {
        Connection connection= ConnectionUtils.getConnection();
        //打开通道
        Channel channel=connection.createChannel();
        //绑定队列
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        //服务器端一次只发送一条消息到客户端
        channel.basicQos(1);
        //创建消费者
        QueueingConsumer consumer=new QueueingConsumer(channel);
        //监听队列
        channel.basicConsume(QUEUE_NAME,false,consumer);
        //获取消息
        while(true){
            QueueingConsumer.Delivery delivery=consumer.nextDelivery();
            System.out.println(new String(delivery.getBody()));
            Thread.sleep(5);
            //确认返回状态
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
        }
    }
}
