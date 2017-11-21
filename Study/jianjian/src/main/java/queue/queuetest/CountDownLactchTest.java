package queue.queuetest;

import java.util.concurrent.CountDownLatch;
import java.util.regex.Matcher;

/**
 * 案例： 做饭
 * 需求: 买菜线程先执行，然后买锅线程执行，然后才能做饭线程
 * Created by gao on 2016/11/29.
 */
public class CountDownLactchTest {
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch countDown=new CountDownLatch(2);
        new Thread(new MaiCaiRunner(countDown)).start();
        new Thread(new MaiGuoRunner(countDown)).start();
        //await方法会产生阻塞，当闭锁的初始计数器变为0的时候放开
        countDown.await();
        System.out.println("做饭");
    }
}
class MaiCaiRunner implements Runnable{
    private CountDownLatch latch;
    public MaiCaiRunner(CountDownLatch latch){
        this.latch=latch;
    }
    public void run() {
        System.out.println("买菜");
        latch.countDown();
    }
}
class MaiGuoRunner implements Runnable{
    private CountDownLatch latch;
    public MaiGuoRunner(CountDownLatch latch){
        this.latch=latch;
    }
    public void run() {
        System.out.println("买过");
        latch.countDown();
    }
}