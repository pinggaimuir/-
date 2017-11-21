package queue.myqueue;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by gao on 2016/11/29.
 */
public class CyclicBarrierTest {
    public static void main(String[] args) {
        ExecutorService executor= Executors.newCachedThreadPool();
        final CyclicBarrier barrier=new CyclicBarrier(4, new Runnable() {
            public void run() {
                System.out.println("大家都到齐了，开始去happy");
            }
        });

        for (int i = 0; i <4 ; i++) {
            executor.execute(new Runnable() {
                public void run() {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    System.out.println(Thread.currentThread().getName()+"到了，其他人呢？");
                    try {
                        barrier.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }

                    System.out.println(Thread.currentThread().getName()+"准备去happy");
                }
            });
        }
        executor.shutdown();
    }
}
