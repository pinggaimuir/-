package queue.myqueue;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Created by gao on 2016/11/29.
 */
public class SemaphoreTest {
    public static void main(String[] args) {
        final SemaphoreTest st=new SemaphoreTest();
        final BoundedHashSet<String> set=st.getSet();
        final ExecutorService pool = Executors.newCachedThreadPool();

        for (int i = 0; i < 5; i++) {
            pool.execute(new Runnable() {
                public void run() {
                    try {
                        set.add(Thread.currentThread().getName());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });

            for (int j = 0; j <5 ; j++) {
                pool.execute(new Runnable() {
                    public void run() {
                        set.remove(Thread.currentThread().getName());
                    }
                });
            }
        }

    }
    //返回一个同时只能有两个线程访问的set
    public BoundedHashSet<String> getSet(){
        return new BoundedHashSet<String>(2);
    }
    class BoundedHashSet<T>{
        private final Set<T> set;
        private final Semaphore semaphore;
        public BoundedHashSet(int bound){
            this.set= Collections.synchronizedSet(new HashSet<T>());
            semaphore=new Semaphore(bound,true);
        }

        public void add(T t) throws InterruptedException {
            System.out.println("即将添加"+t);
            semaphore.acquire();//判断是否到达边界，如果到了，就阻塞
            set.add(t);
            System.out.println("成功添加"+t);
        }

        public void remove(T t){
            System.out.println("即将移除"+t);
            if(set.remove(t)){
                System.out.println("成功移除"+t);
                semaphore.release();//释放掉一个信号量
            }
        }
    }
}
