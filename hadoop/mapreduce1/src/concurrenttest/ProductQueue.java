package concurrenttest;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by gao on 2017/1/10.
 */
public class ProductQueue<T> {
    private final T[] items;
    private final Lock lock=new ReentrantLock();
    private Condition notfull=lock.newCondition();
    private Condition notEmpty=lock.newCondition();

    private int head,tail,count;

    public ProductQueue(int maxSize) {
        this.items = (T[]) new Object[maxSize];
    }
    public ProductQueue(){
        this(10);
    }
}
