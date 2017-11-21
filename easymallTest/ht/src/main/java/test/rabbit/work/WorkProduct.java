package test.rabbit.work;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import test.rabbit.utils.ConnectionUtils;

import java.io.IOException;

/**
 * 工作模式生产者
 * Created by tarena on 2016/11/14.
 */
public class WorkProduct {
    public static final String QUEUE_NAME="jt_test_queue";
    public static void main(String[] args) throws IOException {
        Connection connection= ConnectionUtils.getConnection();
        //创建通道
        Channel channel=connection.createChannel();
        //声明队列
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        for (int i = 0; i < 100; i++) {
            channel.basicPublish("",QUEUE_NAME,null,("work=="+i).getBytes());
        }
    }
}

