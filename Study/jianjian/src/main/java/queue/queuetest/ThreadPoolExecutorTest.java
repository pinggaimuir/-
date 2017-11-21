package queue.queuetest;

import java.util.concurrent.*;

/**
 * 核心线程数量，当有用户请求过来之后，会创建新的核心线程，知道达到指定的数量为止，
 *  在没有达到核心数量之前，即使池子里有闲置的线程，也不会去复用
 *
 *  当核心线程满了之后，并且没有闲置的线程，当再有新的请求线程来了之后，会把请求先放到队列当中，
 *    等有空闲线程之后，再从队列总去出执行，当队列没有满时不会创建新的线程
 *
 *  当核心线程和队列满了之后,此时有新的请求来时，会创建新的线程。这种线程叫做临时线程，
 *      即： 核心线程+临时线程《=最大线程，当临时线程存活时间到了，还没有空闲线程时，会移除请求
 *
 *   当核心线程和队列和临时线程都满了之后，可以将请求交给拒绝服务器助手来处理RejectedExecutorHandler(接口)
 * Created by gao on 2016/11/29.
 */
public class ThreadPoolExecutorTest {
    /*
        第一个参数： 核心线程的数量
        第二个参数： 最大线程数量
        第三个参数： 临时线程的存活时间
        第四个参数： 线程存活时间的单位
        第五个参数： 线程队列
        第六个参数： 线程拒绝服务器
    */
    ExecutorService pool=new ThreadPoolExecutor(5, 10, 3000, TimeUnit.SECONDS,
            new ArrayBlockingQueue<Runnable>(5), new RejectedExecutionHandler() {
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            System.out.println("你先等待一会");
        }
    });
}
