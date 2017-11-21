package queue.queuetest;

import org.junit.Test;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by gao on 2016/11/29.
 */
public class BlockingQueueTest {
    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<String> queue=new ArrayBlockingQueue(10);
        Random r=new Random();
        AtomicInteger atomic=new AtomicInteger();
        for (int i = 0; i < 11; i++) {
            queue.offer("data:"+atomic.incrementAndGet());
        }
        System.out.println(queue.take());
    }
    @Test
    public void stetPrioriryQueue() throws InterruptedException {
        BlockingQueue<String> queue=new PriorityBlockingQueue<String>();
        queue.put("gaojian");
        queue.put("zhengfeng");
        queue.put("haoyu");
        for (int i = 0; i <queue.size() ; i++) {
            System.out.println(queue.take());
        }
        System.out.println(queue.take());
    }
}
