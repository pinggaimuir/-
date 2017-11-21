package queue.queuetest;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by gao on 2016/11/29.
 */
public class CallableTest {
    public static void main(String[] args) throws Exception {
        ExecutorService pool= Executors.newCachedThreadPool();
        Future<String> str=pool.submit(new CallRunner());
        System.out.println(str.get());
    }
}
class CallRunner implements Callable<String>{

    public String call() throws Exception {
        System.out.println("线程正在运行");
        Thread.sleep(3000);
        return "执行完毕";
    }
}
