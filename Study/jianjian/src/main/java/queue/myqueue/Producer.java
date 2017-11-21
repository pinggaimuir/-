package queue.myqueue;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 生产者线程
 * Created by gao on 2016/11/28.
 */
public class Producer implements Runnable {

    private BlockingQueue queue;

    private volatile boolean isRuning=true;

    private static AtomicInteger count =new AtomicInteger();

    private static final int DEFAULT_SLEEP_TIME=1000;
    public Producer(BlockingQueue queue){
        this.queue=queue;
    }
    public void run() {
        String data=null;
        Random r=new Random();
        System.out.println("启动生产者线程！");
        try{
            while(isRuning){
                System.out.println("正在生产数据！");
                Thread.sleep(r.nextInt(DEFAULT_SLEEP_TIME));
                data="data:"+count.incrementAndGet();
                System.out.println("将数据"+data+"放入队列。。。");
                if(!queue.offer(data,2, TimeUnit.SECONDS)){
                    System.out.println("放入数据失败！");
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }finally{
            System.out.println("退出生产者线程");
        }
    }

    public void stop(){
        isRuning=false;
    }
}
