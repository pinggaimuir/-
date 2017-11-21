package queue.queuetest;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * 案例：赛马
 * 需求：两匹赛马线程，必须都到达栅栏后，才能开始一起跑
 * Created by gao on 2016/11/29.
 */
public class CyclicTest {
    public static void main(String[] args) {
        //创建一个栅栏
        CyclicBarrier barrier=new CyclicBarrier(2);
        new Thread(new Horse1(barrier)).start();
        new Thread(new Horse2(barrier)).start();
    }
}
class Horse1 implements Runnable{
    private CyclicBarrier barrier;
    public Horse1(CyclicBarrier barrier) {
        this.barrier=barrier;
    }

    public void run() {
        System.out.println("赛马1到达栅栏");
        try {
            //await（）每当调用此意计数器减一
            //此方法会造成阻塞，当计数器为0的时候执行
            barrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
        System.out.println("赛马1开始跑");
    }
}
class Horse2 implements Runnable{
    private CyclicBarrier barrier;
    public Horse2(CyclicBarrier barrier) {
        this.barrier=barrier;
    }

    public void run() {
        System.out.println("赛马2肚子疼，上洗手间！");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("赛马2到达栅栏");
        try {
            barrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
        System.out.println("赛马2开始跑");
    }
}