package queue.lock;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by gao on 2016/11/29.
 */
public class SynchronizedLockTest {
    public static volatile String name="李磊";
    public static volatile String sex="男";

    public static void main(String[] args) {
        ReentrantLock rt=new ReentrantLock();
        ExecutorService pool= Executors.newCachedThreadPool();
        pool.execute(new Re1Runner(rt));
        pool.execute(new Re2Runner(rt));
    }
}
class Re1Runner implements Runnable{
    private ReentrantLock rt;
    public Re1Runner(ReentrantLock rt){
        this.rt=rt;
    }
    public void run() {
            while(true){
                rt.lock();
                if (SynchronizedLockTest.name.equals("李磊")) {
                    SynchronizedLockTest.name = "韩梅梅";
                    SynchronizedLockTest.sex = "女";
                } else {
                    SynchronizedLockTest.name = "李磊";
                    SynchronizedLockTest.sex = "男";
                }
                rt.unlock();
            }
    }
}
class Re2Runner implements Runnable{
    private ReentrantLock rt;
    public Re2Runner(ReentrantLock rt){
        this.rt=rt;
    }
    public void run() {
            while(true){
                 rt.lock();
                System.out.println(SynchronizedLockTest.name + ":" + SynchronizedLockTest.sex);
                rt.unlock();
            }
    }
}
