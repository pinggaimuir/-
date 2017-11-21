package queue.myqueue;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by gao on 2016/11/28.
 */
public class Consumer implements Runnable {
    private static final int DEFAULT_SLEEP_TIME=1000;
    private BlockingQueue<String> queue;

    public Consumer(BlockingQueue<String> queue){
        this.queue=queue;
    }

    public void run() {
        System.out.println("消费者线程启动！");
        Random r=new Random();
        boolean isRunning=true;
        try{
            while(isRunning){
                System.out.println("正在从队列获取数据!");
                String data=queue.poll(2, TimeUnit.SECONDS);
                if(null!=data){
                    System.out.println("正在消费数据"+data);
                    Thread.sleep(r.nextInt(DEFAULT_SLEEP_TIME));
                }else{
                    //超过两秒还没有数据，认为所有的生产线程都已经退出，自动退出消费线程
                    isRunning=false;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }finally{
            System.out.println("退出消费线程");
        }
    }
}
