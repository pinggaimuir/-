package rabbit;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.QueueingConsumer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * 等待爬取的url队列
 * Created by gao on 2016/11/25.
 */
public class NotCrawlUrlQueue {
    private final Logger logger= LoggerFactory.getLogger(this.getClass());
    //所有url
    private Set<String> allUrl=new HashSet<String>();
    //队列名称
    private static final String QUEUE_NAME="notCrawlUrlQueue";
    public void addUrl(String url){
        //如果url没有爬取过
        if(!allUrl.contains(url)) {
            allUrl.add(url);
            //获得rabbit链接
            try {
                Connection connection = RabbitMQUtil.getConnection();
                Channel channel = connection.createChannel();
                //声明队列
                channel.queueDeclare(QUEUE_NAME, true, false, false, null);
                if (!StringUtils.isEmpty(url)) {
                    //发布url
                    channel.basicPublish("", QUEUE_NAME, null, url.getBytes());
                }
                //关闭通道和链接
                channel.close();
                connection.close();
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }
    public String getUrl(){
        try {
            Connection connection=RabbitMQUtil.getConnection();
            Channel channel=connection.createChannel();
            //声明队列
            channel.queueDeclare(QUEUE_NAME,true,false,false,null);
            //定义队列消费者
            QueueingConsumer consumer=new QueueingConsumer(channel);
            //坚挺队列
            channel.basicConsume(QUEUE_NAME,false,consumer);
            //设置每次只能获取一个消息
            channel.basicQos(1);
            QueueingConsumer.Delivery delivery=consumer.nextDelivery();
            String message= new String(delivery.getBody());
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
            if(!StringUtils.isEmpty(message)){
                return message;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
