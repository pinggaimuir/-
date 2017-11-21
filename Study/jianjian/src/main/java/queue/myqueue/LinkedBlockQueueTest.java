package queue.myqueue;


import bio.selector.Protocol;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by gao on 2016/11/28.
 */
public class LinkedBlockQueueTest {
    public static void main(String[] args) throws InterruptedException {
        //声明一个容量为10的缓存队列
        BlockingQueue<String> queue=new LinkedBlockingQueue<String>(10);

        Producer producer1=new Producer(queue);
        Producer producer2=new Producer(queue);
        Producer producer3=new Producer(queue);
        Consumer consumer=new Consumer(queue);

        ExecutorService service= Executors.newCachedThreadPool();

        service.execute(producer1);
        service.execute(producer2);
        service.execute(producer3);
        service.execute(consumer);

        Thread.sleep(10*1000);
        producer1.stop();
        producer1.stop();
        producer1.stop();

        Thread.sleep(2000);

        service.shutdown();
    }
}
