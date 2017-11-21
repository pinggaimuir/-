package test.rabbit.simple;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import test.rabbit.utils.ConnectionUtils;

import java.io.IOException;

/**
 * 简单消息队列生产者
 * Created by tarena on 2016/11/14.
 */
public class product {
    private static final String QUEUE_NAME="jt_test_queue";
    public static void main(String[] args) throws IOException {
        //获得rabbitmq链接
        Connection connection = ConnectionUtils.getConnection();
        //创建通道
        Channel channel=connection.createChannel();
        //声明队列
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        for(int i=0;i<100;i++){
            String massage="hello world!"+i;
            channel.basicPublish("",QUEUE_NAME,null,massage.getBytes());
        }
        channel.close();
        connection.close();
    }
}
