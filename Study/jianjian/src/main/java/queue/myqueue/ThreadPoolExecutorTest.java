package queue.myqueue;

import java.util.concurrent.*;

/**
 * Created by gao on 2016/11/29.
 */
public class ThreadPoolExecutorTest {
    //如果使用该队列，同时只能有一个任务存在，否则就会跑出异常
    private static BlockingQueue<Runnable> queue=new SynchronousQueue<Runnable>();

    private static final ExecutorService threadPool=new ThreadPoolExecutor(1,1,0,
            TimeUnit.MICROSECONDS,queue,new RejectedExecutionHandler(){

        public void rejectedExecution(Runnable r,ThreadPoolExecutor executor){
            System.out.println("上一个任务还没结束，新任务添加失败！");
        }
    });
//    ExecutorService pool=Executors.newFixedThreadPool();
    public static void addTask(){
        threadPool.submit(new Runnable() {
            public void run() {
                try {
                    System.out.println("start task...");
                    TimeUnit.SECONDS.sleep(5);
                    System.out.println("end task...");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void main(String[] args) {
        //当上一个任务没有执行完毕就添加下一个任务，会跑出异常，而不是进入等待
        addTask();
        addTask();
        addTask();
    }
}
