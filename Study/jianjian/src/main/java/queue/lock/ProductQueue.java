package queue.lock;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by gao on 2016/11/29.
 */
public class ProductQueue<T> {
    private final T[] items;
    private final Lock lock=new ReentrantLock();
    private Condition notFull=lock.newCondition();
    private Condition notEmpty=lock.newCondition();
    //生命队列的头部尾部下标
    private int head,tail,count;
    public  ProductQueue( int maxSize){
        items= (T[]) new Object[maxSize];
    }
    public ProductQueue(){
        this(10);
    }

    public void put(T t) throws InterruptedException {
        lock.lock();
        try{
            //如果当前队列已满，则线程放弃锁进入挂起状态，等待唤醒
            while(count==getCapacity()){
                notFull.await();
            }
            items[tail]=t;
            //当队列满后，队尾重新置为0，以便填充新的元素
            if(++tail==getCapacity()){
                tail=0;
            }
            count++;
            //唤醒所有挂起的线程
            notEmpty.signalAll();
        }finally{
            lock.unlock();
        }
    }

    public T take() throws InterruptedException {
        lock.lock();
        try{
            //当count==0，代表队列中的元素为0，不能再获取，挂起线程
            while(count==0){
                notEmpty.await();
            }
            T t=items[head];
            if(++head==getCapacity()){
                head=0;
            }
            count--;
            notFull.signalAll();
            return t;
        }finally {
            lock.unlock();
        }
    }

    public int getCapacity(){
        return items.length;
    }

    public int size(){
        lock.lock();
        try{
            return count;
        }finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ProductQueue<String> queue=new ProductQueue<String>(10);
        ExecutorService pool= Executors.newCachedThreadPool();
        pool.execute(new Product(queue));
        pool.execute(new Consumer(queue));
    }

}
class Consumer implements Runnable{
    private ProductQueue<String> queue;
    public Consumer (ProductQueue queue){
        this.queue=queue;
    }
    AtomicInteger ato=new AtomicInteger();
    public void run() {
        while(true){
            try {
                String data="data"+ato.getAndIncrement();
                queue.put(data);
                System.out.println("向队列填充数据"+data);
            } catch (InterruptedException e) {
                System.out.println("队列已满");
            }
        }
    }
}
class Product implements Runnable{
    private ProductQueue<String> queue;
    public Product (ProductQueue queue){
        this.queue=queue;
    }
    Random r=new Random();
    public void run() {
        while(true){
            try {
                String data=queue.take();
                System.out.println("从队列获取数据"+data);
                Thread.currentThread().sleep(5);
            } catch (InterruptedException e) {
                System.out.println("队列为空");
            }
        }
    }
}
